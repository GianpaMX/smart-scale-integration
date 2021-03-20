package io.github.gianpamx.shscale.app

import dagger.BindsInstance
import dagger.Component
import io.github.gianpamx.di.AppScope
import io.github.gianpamx.di.FactoriesModule

@Component(
    modules = [
        AppModule::class,
        FactoriesModule::class,
        BindingModule::class,
        DomainModule::class,
        TestModule::class
    ]
)
@AppScope
interface TestComponent : AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(testApp: TestApp): Builder

        fun build(): TestComponent
    }
}
