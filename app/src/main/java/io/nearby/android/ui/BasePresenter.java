package io.nearby.android.ui;

import io.nearby.android.data.source.SpottedDataSource;

/**
 * Created by Marc on 2017-03-09.
 */

public class BasePresenter {

    /**
     * Manages some commun errors
     * @param errorType
     * @return true if the error was handled
     */
    public static boolean manageError(BaseView view, SpottedDataSource.ErrorType errorType){
        boolean handled = false;

        if(errorType == SpottedDataSource.ErrorType.DisabledUser){
            handled = true;
            view.onUserAccountDisabled();
        }
        else if(errorType == SpottedDataSource.ErrorType.UnauthorizedUser){
            handled = true;
            view.onUserUnauthorized();
        }

        return handled;
    }
}
