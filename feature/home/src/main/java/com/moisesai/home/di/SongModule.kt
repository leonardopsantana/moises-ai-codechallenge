package com.moisesai.home.di

import com.moisesai.home.data.SongsRepository
import com.moisesai.home.data.SongsRepositoryImpl
import com.moisesai.home.data.remote.SongsService
import com.moisesai.home.domain.usecase.GetSongsUseCase
import com.moisesai.home.domain.usecase.GetSongsUseCaseImpl
import com.moisesai.home.presentation.ui.songs.SongsViewModel
import com.moisesai.home.domain.usecase.GetSongsByAlbumUseCase
import com.moisesai.home.domain.usecase.GetSongsByAlbumUseCaseImpl
import com.moisesai.home.presentation.ui.album.AlbumViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import retrofit2.Retrofit

@Module
@ComponentScan("com.moiseai.home")
class SongModule {
    val songsModule = module {
        factory<SongsService> { provideSongsService() }
        factory<SongsRepository> {
            SongsRepositoryImpl(
                get()
            )
        }
        factory<GetSongsUseCase> {
            GetSongsUseCaseImpl(
                get()
            )
        }
        factory<GetSongsByAlbumUseCase> {
            GetSongsByAlbumUseCaseImpl(
                get()
            )
        }
        viewModel { SongsViewModel(get()) }
        viewModel {
            AlbumViewModel(
                get(),
                get()
            )
        }
    }

    @Factory
    fun provideSongsService(): SongsService {
        val koin = GlobalContext.get()
        val retrofit = koin.get<Retrofit>()
        return retrofit.create(SongsService::class.java)
    }
}
