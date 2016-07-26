package edu.com.app.injection.module;

import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import dagger.Module;
import dagger.Provides;
import edu.com.app.MyApplication;
import edu.com.app.data.DbOpenHelper;
import edu.com.app.injection.scope.PerActivity;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/7/21.
 * Class Note:
 * provide instance used in Database ，mainly use{@link SqlBrite}which support Rx observables
 */
@Module
public final class DbModule {

    @Provides
    @PerActivity
    SQLiteOpenHelper provideOpenHelper(MyApplication application) {
        return new DbOpenHelper(application);
    }

    @Provides
    @PerActivity
    SqlBrite provideSqlBrite(){
        return SqlBrite.create(new SqlBrite.Logger() {
            @Override public void log(String message) {
                Timber.tag("Database").v(message);
            }
        });
    }

    @Provides
    @PerActivity
    BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(true);
        return db;
    }
}
