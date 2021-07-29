package com.arbo.oracoes.data.di


import com.arbo.oracoes.data.repository.*
import com.arbo.oracoes.domain.repository.*
import com.arbo.oracoes.domain.usecase.SignInAnonymouslyUseCase
import com.arbo.oracoes.presentation.util.NetworkStateRepository
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val repositoryModule = Kodein.Module("RepositoryModule") {
    bind<AuthRepository>() with singleton { AuthDataRepository(instance()) }

    bind<SignInAnonymouslyUseCase>() with singleton {
        SignInAnonymouslyUseCase(
            instance()
        )
    }
    bind<AudioCategoryRepository>() with singleton { AudioCategoryDataRepository(instance()) }
    bind<AudioRepository>() with singleton { AudioDataRepository(instance()) }
    bind<StorageRepository>() with singleton { StorageDataRepository(instance(), instance()) }
    bind<TextCategoryRepository>() with singleton { TextCategoryDataRepository(instance()) }
    bind<TextRepository>() with singleton { TextDataRepository(instance()) }
    bind<PreferencesRepository>() with singleton { PreferencesDataRepository(instance()) }
    bind<ReminderSchedulerRepository>() with singleton { ReminderSchedulerDataRepository(instance()) }
    bind<AnalyticsRepository>() with singleton { AnalyticsDataRepository(instance()) }
    bind<NetworkStateRepository>() with singleton { NetworkStateRepository(instance()) }
    bind<RemoteConfigRepository>() with singleton { RemoteConfigDataRepository(instance()) }
}