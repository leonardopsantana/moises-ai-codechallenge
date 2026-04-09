package com.moisesai.player.di

import com.moisesai.player.domain.usecase.GetTrackByIdUseCase
import com.moisesai.player.domain.usecase.GetTrackByIdUseCaseImpl
import com.moisesai.player.data.TrackRepository
import com.moisesai.player.data.TrackRepositoryImpl
import com.moisesai.player.data.remote.PlayerService
import com.moisesai.player.media.MediaPlayerWrapper
import com.moisesai.player.presentation.ui.PlayerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import retrofit2.Retrofit

@Module
@ComponentScan("com.moiseai.player")
class PlayerModule {
    val playerModule = module {
        factory<PlayerService> { providePlayerService() }
        factory<TrackRepository> { TrackRepositoryImpl(get()) }
        factory<GetTrackByIdUseCase> { GetTrackByIdUseCaseImpl(get()) }
        factory<MediaPlayerWrapper> { MediaPlayerWrapper(androidContext()) }
        viewModel { PlayerViewModel(get(), get(), get(), get()) }
    }

    @Factory
    fun providePlayerService(): PlayerService {
        val koin = GlobalContext.get()
        val retrofit = koin.get<Retrofit>()
        return retrofit.create(PlayerService::class.java)
    }
}