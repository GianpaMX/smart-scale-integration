package io.github.gianpamx.shscale.app

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import io.github.gianpamx.di.AppScope
import io.github.gianpamx.shscale.background.BackgroundModel
import io.github.gianpamx.shscale.domain.ObserveSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel

@Module
class AppModule {
    @Provides
    @AppScope
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @AppScope
    fun provideApplicationContext(application: Application): Context = application

    @Provides
    @AppScope
    fun provideNotificationManager(context: Context) =
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @AppScope
    fun provideErrorChannel() = BroadcastChannel<Throwable>(Channel.CONFLATED)

    @Provides
    @AppScope
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences("internal", MODE_PRIVATE)

    @Provides
    @AppScope
    fun provide(
        observeSettings: ObserveSettings,
        errorChannel: BroadcastChannel<Throwable>,
        defaultDispatcher: CoroutineDispatcher
    ) = BackgroundModel(
        observeSettings,
        errorChannel,
        defaultDispatcher
    )
}
