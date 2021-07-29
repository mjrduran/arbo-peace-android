# arbo-peace-android

This is an app to deliver text and audio religious content.
It is monetized with ads, also giving the user an option to remove all ads with a single purchase.
It is available in Play Store. Click [here](https://play.google.com/store/apps/details?id=com.arbo.oracoes) to download.

## Tech choices 
It has been developed with the following frameworks:

* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): This is important to manage view state while respecting Android lifecycle restrictions.
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata): This was useful to observe data changes and update UI components.
* [Navigation](https://developer.android.com/guide/navigation): It was used to manage bottom bar and app navigation.
* [Work Manager](https://developer.android.com/topic/libraries/architecture/workmanager): In this project, it has been used to schedule reminder local notifications.
* [Coroutines](https://developer.android.com/kotlin/coroutines): Coroutines were used to handle asynchronous requests in background threads.
* [ExoPlayer](https://exoplayer.dev/): It is Media Player framework. In this project, it is used to play audio content. 
* [Kodein](https://kodein.org/di/): Dependency injection framework
* [Firebase FireStore](https://firebase.google.com/docs/firestore): NoSQL cloud database, this is used to store app content and synchronize data with the server.
* [Firebase Storage](https://firebase.google.com/docs/storage): File storage service used to store media content.






 
