package edu.com.mvplibrary.model.rx;

import android.content.Context;

import edu.com.mvplibrary.model.bean.News;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rx.Observable;

/**
 * Created by Anthony on 2016/5/23.
 * Class Note:
 */
public class RxRealm {
    private Context mContext;
    private Realm mInstance;
    RealmConfiguration mRealmConfiguration;
    private static volatile RxRealm rxRealm;

    /**
     * initialize first
     */
    public void init(Context mContext) {
        if (rxRealm == null) {
            synchronized (RxRealm.class) {
                if (rxRealm == null) {
                    rxRealm = new RxRealm(mContext);
                }
            }
        }
    }

    public RxRealm(Context mContext) {
        this.mContext = mContext;
        mRealmConfiguration = new RealmConfiguration.Builder(mContext)
                .name("RxRealm")
                .schemaVersion(7)
                .build();
        mInstance = Realm.getInstance(mRealmConfiguration);
    }

    public Realm getInstance() {
        if (mInstance == null) {
            throw new IllegalArgumentException("Initialize RxRealm First");
        }
        return mInstance;
    }
    //    public Observable<RealmResults<Souvenir>> getSouvenirALl() {
//        return mInstance.where(Souvenir.class).findAllAsync().asObservable();
//    }
//
//    public void saveSouvenList(List<Souvenir> list) {
//        mInstance.beginTransaction();
//        mInstance.copyToRealmOrUpdate(list);
//        mInstance.commitTransaction();
//    }


//    public void saveUser(User user) {
//        mInstance.beginTransaction();
//        mInstance.copyToRealmOrUpdate(user);
//        mInstance.commitTransaction();
//    }

//    public void saveSouvenir(Souvenir souvenir) {
//        mInstance.beginTransaction();
//        mInstance.copyToRealmOrUpdate(souvenir);
//        mInstance.commitTransaction();
//
//    }

//    public Observable<User> getUser(String id) {
//        return mInstance.where(User.class).equalTo("ID", id).findFirst().asObservable();
//
//    }

//    public RealmResults<Gallery> getAllGallery() {
//        return mInstance.where(Gallery.class).findAll();
//    }
//
//    public void saveGalleryList(final List<Gallery> list) {
//        mInstance.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.copyToRealmOrUpdate(list);
//            }
//        });
//
//    }
//
//    public void saveGallery(Gallery gallery) {
//        mInstance.beginTransaction();
//        mInstance.copyToRealmOrUpdate(gallery);
//        mInstance.commitTransaction();
//
//    }

    public void saveNews(News news) {
        mInstance.beginTransaction();
        mInstance.copyToRealmOrUpdate(news);
        mInstance.commitTransaction();

    }

    public Observable<RealmResults<News>> getAllNews() {
        return mInstance.where(News.class).findAllSorted("time").asObservable();
    }

    public void Close() {
        mInstance.close();
    }
}
