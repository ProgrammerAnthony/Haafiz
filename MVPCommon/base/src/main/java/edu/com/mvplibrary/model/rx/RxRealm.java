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
 *
 */
public class RxRealm {
    private Context mContext;
    private Realm mRealm;
    RealmConfiguration mRealmConfiguration;
    private static volatile RxRealm rxRealm;

    /**
     * initialize first
     */
    public void init(Context mContext) {
        if (rxRealm == null) {
            synchronized (RxRealm.class){
                if(rxRealm ==null){
                    rxRealm=new RxRealm(mContext);
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
        mRealm = Realm.getInstance(mRealmConfiguration);
    }

    public Realm getInstance(){
        return mRealm;
    }
    //    public Observable<RealmResults<Souvenir>> getSouvenirALl() {
//        return mRealm.where(Souvenir.class).findAllAsync().asObservable();
//    }
//
//    public void saveSouvenList(List<Souvenir> list) {
//        mRealm.beginTransaction();
//        mRealm.copyToRealmOrUpdate(list);
//        mRealm.commitTransaction();
//    }


//    public void saveUser(User user) {
//        mRealm.beginTransaction();
//        mRealm.copyToRealmOrUpdate(user);
//        mRealm.commitTransaction();
//    }

//    public void saveSouvenir(Souvenir souvenir) {
//        mRealm.beginTransaction();
//        mRealm.copyToRealmOrUpdate(souvenir);
//        mRealm.commitTransaction();
//
//    }

//    public Observable<User> getUser(String id) {
//        return mRealm.where(User.class).equalTo("ID", id).findFirst().asObservable();
//
//    }

//    public RealmResults<Gallery> getAllGallery() {
//        return mRealm.where(Gallery.class).findAll();
//    }
//
//    public void saveGalleryList(final List<Gallery> list) {
//        mRealm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.copyToRealmOrUpdate(list);
//            }
//        });
//
//    }
//
//    public void saveGallery(Gallery gallery) {
//        mRealm.beginTransaction();
//        mRealm.copyToRealmOrUpdate(gallery);
//        mRealm.commitTransaction();
//
//    }

    public void saveNews(News news) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(news);
        mRealm.commitTransaction();

    }

    public Observable<RealmResults<News>> getAllNews() {
        return mRealm.where(News.class).findAllSorted("time").asObservable();
    }

    public void Close() {
        mRealm.close();
    }
}
