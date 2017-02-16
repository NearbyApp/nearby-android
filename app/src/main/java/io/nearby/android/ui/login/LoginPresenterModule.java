package io.nearby.android.ui.login;

import dagger.Provides;

/**
 * Created by Marc on 2017-02-16.
 */
public class LoginPresenterModule {

    private final LoginContract.View mView;

    public LoginPresenterModule(LoginContract.View view){
        mView = view;
    }

    @Provides
    LoginContract.View provideLoginContractView(){
        return mView;
    }
}
