package com.planis.johannes.intelmeme;

import android.app.Application;

import com.planis.johannes.intelmeme.utils.DataManager;

/**
 * Created by JOHANNES on 3/29/2016.
 */
public class App extends Application {

    private DataManager dataManager;
    private static App instance = new App();

    @Override
    public void onCreate() {

        super.onCreate();
    }

    public DataManager getDataManager(){
        if (null == dataManager){
            dataManager = new DataManager();
        }
        return dataManager;
    }

    public static App getInstance(){
        return instance;
    }

}
