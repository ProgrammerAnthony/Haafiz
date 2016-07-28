package edu.com.app.injection.component;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.otto.Bus;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Component;
import edu.com.app.MyApplication;
import edu.com.app.injection.module.ApplicationModule;
import edu.com.app.injection.scope.ApplicationContext;
import edu.com.app.widget.ViewDisplay;

/**
 * Created by Anthony on 2016/6/13.
 * Class Note:
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MyApplication application);

    /**
     * instance expose to children,
     * because children are in different Scope {@link edu.com.app.injection.scope.PerActivity}
     * ,so if this is field children wanna load , we must expose here
     */
    @ApplicationContext
    Context context();


    MyApplication application();

    //third part lib Otto used here
    Bus eventBus();


    BriteDatabase briteDb();

    SQLiteOpenHelper sqOpenHelper();

    SqlBrite sqlBrite();

    ViewDisplay viewDisplay();

//    EventPosterHelper eventPosterHelper();



//    LeanCloudHelper rxLeanCLoud();

//    RxBus rxBus();

//    FriendsService ribotService();

//    PreferencesHelper preferencesHelper();

//    DatabaseHelper databaseHelper();

//    DataManager dataManager();




}
