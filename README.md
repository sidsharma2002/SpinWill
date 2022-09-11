# SpinWill

A modern and highly customisable library to implement spinwheel feature in your android app which comes packed with inbuilt local database support that allows you to periodically _fetch and cache_ the reward items 🎁.

![WhatsApp Image 2022-09-11 at 12 36 04](https://user-images.githubusercontent.com/53833109/189518316-f446ea0e-18d7-453c-811e-4b1c8300a2e2.jpeg)


### How to use fetch And Catch Mechanism: 

create and save this injector instance (recommended to be application scoped)

`private val injector by lazy {
        SpinWillInjector<SpinWillItem>()  // We are assuming SpinWillItem is the reward item model class.
    }`

provide the following dependencies to the injector : 
  1. Application context
  2. SpinWillRemoteDatabase
  3. SpinWillLocalDatabase (requires SpinWillDbActions incase you are using default local db impl)
  4. SpinWillBitmapLoadUseCase
  
example : 

```
injector.init(this, RemoteDatabaseImpl()) // impl of SpinWheelRemoteDatabase

// provide localDatabase
injector.setLocalDatabase(
  // predefined impl provided by the library
  SpinWillLocalDbImpl(daoActions) // provide daoActions to the local db
 )

// provide bitmap load usecase
injector.setBitmapLoadUseCase(
            // predefined impl provided by the library
            SpinWillBitmapLoadUseCaseImpl(
                this.applicationContext,
                // provide the ItemAdapter
                object : WillItemAdapter<SpinWillItem> {
                    override fun getRewardImageUrl(item: SpinWillItem): String {
                        return item.rewardImage
                    }

                    override fun setRewardBitmap(item: SpinWillItem, bitmap: Bitmap) {
                        item.rewardBitmap = bitmap
                    }
                })
        )
    }
```

### Trigger Periodic Work:
```
// customise according to your usecase
val workReq = PeriodicWorkRequest = PeriodicWorkRequest.Builder(
            SpinWillWorker::class.java, x, TimeUnit.MINUTES
        )

Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(SpinWillWorkerFactory(injector.getRepository()))
            .build()        

WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "spinwill_item_update",
                ExistingPeriodicWorkPolicy.REPLACE,
                workReq
            )        
```

### Show Wheel UI: 

``` 
// add SpinWillView1
val willView = SpinWillView1<SpinWillItem>(this)
someLayout.addView(willView)

// run on background thread
injector.getRepository().fetchAndUpdateWheelItem()
val result = injector.getRepository().loadBitmapAndSave()

if (result is Resource.Success && result.data != null) {
        runOnUiThread {
                    // set data items
                    willView.setItems(result.data!!)

                    // provide adapter for the items
                    willView.setItemAdapter(object : WillItemUiAdapter<SpinWillItem> {
                        override fun getRewardText(item: SpinWillItem): String {
                            return item.rewardText
                        }

                        override fun getOverlayText(item: SpinWillItem): String {
                            return item.rewardText
                        }

                        override fun getRewardBitmap(item: SpinWillItem): Bitmap? {
                            return item.rewardBitmap
                        }
                    })

                    // invalidate the view
                    willView.invalidate()
                }
}
```

### Contributions are welcomed

