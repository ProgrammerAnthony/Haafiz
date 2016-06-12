package edu.com.app.injection.module;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.com.app.MyApplication;
import edu.com.app.data.DataManager;
import edu.com.base.model.local.DatabaseHelper;
import edu.com.base.model.local.DbOpenHelper;
import edu.com.base.model.local.PreferencesHelper;
import edu.com.base.model.remote.RibotsService;
import edu.com.app.injection.scope.ContextLife;
import edu.com.base.model.rx.RxBus;
import edu.com.base.model.rx.RxLeanCloud;
import edu.com.base.model.rx.RxRealm;
import edu.com.base.ui.widget.EventPosterHelper;


@Module
public class ApplicationModule {
    private MyApplication mApplication;
    private PreferencesHelper mPreferencesHelper;
    private RxRealm mRxRealm;
    private RxLeanCloud mRxLeanCloud;
    private RxBus mRxBus;
    private DbOpenHelper mDbOpenHelper;
    private DatabaseHelper mDatabaseHelper;
    private EventPosterHelper mEventPosterHelper;


    public ApplicationModule(MyApplication application) {
        mApplication = application;
        mPreferencesHelper = new PreferencesHelper(mApplication.getApplicationContext());
        mRxRealm = new RxRealm(mApplication.getApplicationContext());
        mRxLeanCloud = new RxLeanCloud(mApplication.getApplicationContext());
        mRxBus = new RxBus();
        mDbOpenHelper =new DbOpenHelper(mApplication.getApplicationContext());
        mDatabaseHelper =new DatabaseHelper(mDbOpenHelper);
        mEventPosterHelper=new EventPosterHelper(new Bus());

    }

    @Provides
    @Singleton
    @ContextLife("Application")
    public Context provideContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    public RxRealm provideRealm() {
        return mRxRealm;

    }

    @Provides
    @Singleton
    public RxLeanCloud provideRxLeanCloud() {
        return mRxLeanCloud;
    }

    @Provides
    @Singleton
    public RxBus provideRxBus() {
        return mRxBus;
    }

    @Provides
    @Singleton
    Bus provideEventBus() {
        return new Bus();
    }

    @Provides
    @Singleton
    RibotsService provideRibotsService() {
        return RibotsService.Creator.newRibotsService();
    }

    @Provides
    @Singleton
    DatabaseHelper provideDatabaseHelper(){
      return  mDatabaseHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(){
        return  mPreferencesHelper;
    }

    @Provides
    @Singleton
    EventPosterHelper provideEventPosterHelper(){
        return  mEventPosterHelper;
    }



}
