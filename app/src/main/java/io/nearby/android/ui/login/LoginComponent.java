package io.nearby.android.ui.login;

import dagger.Component;
import io.nearby.android.data.source.DataManagerComponent;

@Component(dependencies = DataManagerComponent.class, modules = LoginPresenterModule.class)
public interface LoginComponent {

    void inject(LoginActivity loginActivity);
}
