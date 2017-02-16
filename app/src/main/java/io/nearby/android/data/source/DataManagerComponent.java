package io.nearby.android.data.source;

import javax.inject.Singleton;

import dagger.Component;
import io.nearby.android.ApplicationModule;

@Singleton
@Component(modules={DataManagerModule.class, ApplicationModule.class})
public interface DataManagerComponent {

    DataManager getDataManager();
}
