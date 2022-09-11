# SpinWill

A modern and highly customisable library to implement spinwheel feature in your android app which comes packed with inbuilt local database support that allows you to periodically _fetch and cache_ the reward items 🎁.

### How to use: 

create and save this injector instance (recommended to be application scoped)

`private val injector by lazy {
        SpinWillInjector<SpinWillItem>()
    }`

provide the following dependencies to the injector : 
  1. Application context
  2. SpinWillRemoteDatabase
  3. SpinWillLocalDatabase (requires SpinWillDbActions incase you are using default local db immpl)
  4. SpinWillBitmapLoadUseCase
  
example : 

NOTE : We are assuming SpinWillItem is the reward item model class.

```
injector.init(this, RemoteDatabaseImpl())

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




