package com.arbo.oracoes.data.di

import com.arbo.oracoes.domain.usecase.*
import org.kodein.di.Kodein
import org.kodein.di.android.x.AndroidLifecycleScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

val useCaseModule = Kodein.Module("UseCaseModule") {
    bind<AudioCategoryFindAllUseCase>() with scoped(AndroidLifecycleScope).singleton {
        AudioCategoryFindAllUseCase(instance())
    }
    bind<AudioFindByIdsUseCase>() with scoped(AndroidLifecycleScope).singleton {
        AudioFindByIdsUseCase(instance())
    }
    bind<DownloadAudioUseCase>() with scoped(AndroidLifecycleScope).singleton {
        DownloadAudioUseCase(instance(), instance())
    }
    bind<TextCategoryFindAllUseCase>() with scoped(AndroidLifecycleScope).singleton {
        TextCategoryFindAllUseCase(instance())
    }
    bind<TextFindByIdsUseCase>() with scoped(AndroidLifecycleScope).singleton {
        TextFindByIdsUseCase(instance())
    }
    bind<DailyTextUseCase>() with scoped(AndroidLifecycleScope).singleton {
        DailyTextUseCase(instance())
    }
    bind<WeeklyAudioUseCase>() with scoped(AndroidLifecycleScope).singleton {
        WeeklyAudioUseCase(instance(), instance())
    }
}