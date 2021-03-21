package io.github.gianpamx.shscale.app

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import io.github.gianpamx.di.AppScope
import io.github.gianpamx.di.FactoriesModule
import io.github.gianpamx.shscale.MainActivity
import io.github.gianpamx.shscale.background.BackgroundService

@AppScope
@Component(
    modules = [
        AppModule::class,
        FactoriesModule::class,
        BindingModule::class,
        DomainModule::class,
        ApiModule::class
    ]
)
interface AppComponent {
    fun inject(app: App)

    fun inject(activity: MainActivity)

    fun inject(service: BackgroundService)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
