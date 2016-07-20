package edu.com.app.data.rx;

import android.content.Context;

import javax.inject.Inject;

import edu.com.app.data.bean.News;
import edu.com.app.injection.scope.ApplicationContext;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rx.Observable;

/**
 * Created by Anthony on 2016/5/23.
 * Class Note:
 * @deprecated use SqlBrite instead
 */

public class RxRealm {
    private Context mContext;
    private Realm realm;
    RealmConfiguration mRealmConfiguration;

    @Inject
    public RxRealm(@ApplicationContext Context context) {
        this.mContext = context;
        mRealmConfiguration = new RealmConfiguration.Builder(context)
                .name("RxRealm")
                .schemaVersion(7)
                .build();
        realm = Realm.getInstance(mRealmConfiguration);
    }

/*    public Realm getInstance() {
        if (realm == null) {
            throw new IllegalArgumentException("Initialize RxRealm First");
        }
        return realm;
    }*/
    //    public Observable<RealmResults<Souvenir>> getSouvenirALl() {
//        return realm.where(Souvenir.class).findAllAsync().asObservable();
//    }
//
//    public void saveSouvenList(List<Souvenir> list) {
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(list);
//        realm.commitTransaction();
//    }


//    public void saveUser(User user) {
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(user);
//        realm.commitTransaction();
//    }

//    public void saveSouvenir(Souvenir souvenir) {
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(souvenir);
//        realm.commitTransaction();
//
//    }

//    public Observable<User> getUser(String id) {
//        return realm.where(User.class).equalTo("ID", id).findFirst().asObservable();
//
//    }

//    public RealmResults<Gallery> getAllGallery() {
//        return realm.where(Gallery.class).findAll();
//    }
//
//    public void saveGalleryList(final List<Gallery> list) {
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.copyToRealmOrUpdate(list);
//            }
//        });
//
//    }
//
//    public void saveGallery(Gallery gallery) {
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(gallery);
//        realm.commitTransaction();
//
//    }

    public void saveNews(News news) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(news);
        realm.commitTransaction();

    }

    public Observable<RealmResults<News>> getAllNews() {
        return realm.where(News.class).findAllSorted("time").asObservable();
    }

    public void Close() {
        realm.close();
    }
}
