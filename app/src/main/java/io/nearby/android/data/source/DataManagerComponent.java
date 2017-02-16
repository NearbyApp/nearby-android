package io.nearby.android.data.source;

import javax.inject.Singleton;

import dagger.Component;
import io.nearby.android.ApplicationModule;

/**
 * Created by Marc on 2017-02-16.
 */
@Singleton
@Component(modules={DataManagerModule.class, ApplicationModule.class})
public interface DataManagerComponent {

    DataManager getDataManager();
}
