package data.network.tinder

import dagger.Module
import dagger.Provides
import data.network.NetworkModule
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = arrayOf(NetworkModule::class))
internal class TinderApiModule {
    @Provides
    @Singleton
    fun retrofit(retrofit: Retrofit) = retrofit.create(TinderApi::class.java)
}