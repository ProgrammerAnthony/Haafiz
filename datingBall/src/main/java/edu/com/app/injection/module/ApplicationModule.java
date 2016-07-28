package edu.com.app.injection.module;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.otto.Bus;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.com.app.MyApplication;
import edu.com.app.data.DbOpenHelper;
import edu.com.app.injection.scope.ApplicationContext;
import edu.com.app.widget.ViewDisplay;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/6/13.
 * Class Note:
 * Application Module，
 */
@Module
public class ApplicationModule {
    protected final MyApplication mApplication;

    public ApplicationModule(MyApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    MyApplication provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    @Singleton
    Context provideContext() {
        return mApplication;
    }


    /**
     * third part lib must provided in module
     * if children with different scope also want to get this instantce
     * exposing in the {@link edu.com.app.injection.component.ApplicationComponent} is needed
     *
     * @return
     */
    @Provides
    @Singleton
    Bus provideEventBus() {
        return new Bus();
    }


    @Provides
    @Singleton
    BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(true);
        return db;
    }

    @Provides
    @Singleton
    SQLiteOpenHelper provideOpenHelper(MyApplication application) {
        return new DbOpenHelper(application);
    }

    @Provides
    @Singleton
    SqlBrite provideSqlBrite() {
        return SqlBrite.create(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
                Timber.tag("Database").v(message);
            }
        });
    }

    @Provides
    @Singleton
    ViewDisplay provideViewDisplay(@ApplicationContext Context context) {
        return new ViewDisplay(context);
    }


}
