package com.arbo.oracoes.data.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.storage.FirebaseStorage
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val firebaseModule = Kodein.Module("Firebase Module") {
    bind<FirebaseAuth>() with singleton { FirebaseAuth.getInstance() }
    bind<FirebaseFirestore>() with singleton { FirebaseFirestore.getInstance() }
    bind<FirebaseStorage>() with singleton { FirebaseStorage.getInstance() }
    bind<FirebaseAnalytics>() with singleton { FirebaseAnalytics.getInstance(instance()) }
    bind<FirebaseRemoteConfig>() with singleton { Firebase.remoteConfig }
}