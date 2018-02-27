package skankhunt.com.piccmirror;

import android.app.Activity;
import android.app.Application;



import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

import skankhunt.com.piccmirror.di.AppInjector;
import timber.log.Timber;


/**
 * Created by skankhunt on 2018/1/19.
 */

public class GithubApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();


        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        AppInjector.init(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
