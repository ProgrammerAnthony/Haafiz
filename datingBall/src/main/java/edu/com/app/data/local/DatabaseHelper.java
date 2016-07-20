package edu.com.app.data.local;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.com.app.data.bean.Menu;
import rx.Observable;


@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        mDb = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper);
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public void setMenu(Observable<List<Menu>> menus) {

    }

    public Observable<List<Menu>> getMenu() {
        return null;
    }

   /* public Observable<Friends> setFriends(final Collection<Friends> newFriends) {
        return Observable.create(new Observable.OnSubscribe<Friends>() {
            @Override
            public void call(Subscriber<? super Friends> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.FriendsProfileTable.TABLE_NAME, null);
                    for (Friends friends : newFriends) {
                        long result = mDb.insert(Db.FriendsProfileTable.TABLE_NAME,
                                Db.FriendsProfileTable.toContentValues(friends.profile),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) subscriber.onNext(friends);
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Friends>> getFriends() {
        return mDb.createQuery(Db.FriendsProfileTable.TABLE_NAME,
                "SELECT * FROM " + Db.FriendsProfileTable.TABLE_NAME)
                .mapToList(new Func1<Cursor, Friends>() {
                    @Override
                    public Friends call(Cursor cursor) {
                        return new Friends(Db.FriendsProfileTable.parseCursor(cursor));
                    }
                });
    }
*/
}
