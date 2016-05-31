package edu.com.base.model.rx;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.avos.avoscloud.SignUpCallback;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.com.base.model.bean.Gallery;
import edu.com.base.model.bean.Souvenir;
import edu.com.base.model.bean.User;
import edu.com.base.model.dao.GalleryDao;
import edu.com.base.model.dao.NewsDao;
import edu.com.base.model.dao.SouvenirDao;
import edu.com.base.model.dao.UserDao;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Anthony on 2016/5/23.
 * Class Note:
 */
public class RxLeanCloud {
    private static volatile RxLeanCloud mInstance;
    private Context mContext;


    public RxLeanCloud getInstance() {
        if (mInstance == null) {
            throw new IllegalArgumentException("Initialize PreferenceManager First");
        }
        return mInstance;
    }

    public RxLeanCloud init(Context mContext) {
        if (mInstance == null) {
            synchronized (RxLeanCloud.class) {
                if (mInstance == null) {
                    mInstance = new RxLeanCloud(mContext);
                }
            }
        }
        return mInstance;
    }

    public RxLeanCloud(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 日记本存储到LeanCloud
     *
     * @param souvenir
     * @return
     */
    public Observable<Souvenir> saveSouvenirByLeanCloud(final Souvenir souvenir) {
        return Observable.create(new Observable.OnSubscribe<Souvenir>() {
            @Override
            public void call(final Subscriber<? super Souvenir> subscriber) {
                souvenir.setFetchWhenSave(true);
                souvenir.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(souvenir);
                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 存储用户到LeanCloud
     *
     * @param user
     * @return
     */
    public Observable<AVUser> SaveUserByLeanCloud(final AVUser user) {
        return Observable.create(new Observable.OnSubscribe<AVUser>() {
            @Override
            public void call(final Subscriber<? super AVUser> subscriber) {

                user.setFetchWhenSave(true);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(user);
                        } else {
                            subscriber.onError(e);
                            Logger.e(e.getMessage());
                        }
                        subscriber.onCompleted();
                    }
                });

            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 通过id获取LeanCloud上的用户
     *
     * @param objectId
     * @return
     */
    public Observable<AVUser> GetUserByLeanCloud(final String objectId) {

        return Observable.create(new Observable.OnSubscribe<AVUser>() {
            @Override
            public void call(Subscriber<? super AVUser> subscriber) {

                AVQuery<AVUser> query = AVUser.getQuery();

                try {
                    AVUser user = query.include(UserDao.AVATARURL).get(objectId);
                    subscriber.onNext(user);

                } catch (AVException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 通过用户名获取LeanCloud的用户信息
     * @param username
     * @return
     */
    public Observable<AVUser> GetUserByUsername(final String username) {
        return Observable.create(new Observable.OnSubscribe<AVUser>() {
            @Override
            public void call(Subscriber<? super AVUser> subscriber) {
                AVQuery<AVUser> query = AVUser.getQuery();
                try {
                    AVUser user = query.whereEqualTo(UserDao.USERNAME, username).getFirst();
                    subscriber.onNext(user);
                } catch (AVException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 获取所有日记本
     * @param authorId
     * @param loverid
     * @param size
     * @param page
     * @return
     */
    public Observable<List<Souvenir>> GetALlSouvenirByLeanCloud(final String authorId, final String loverid, final int size, final int page) {
        return Observable.create(new Observable.OnSubscribe<List<Souvenir>>() {
            @Override
            public void call(final Subscriber<? super List<Souvenir>> subscriber) {

                AVQuery<Souvenir> query = AVQuery.getQuery(SouvenirDao.TABLENAME);
                query.whereEqualTo(SouvenirDao.SOUVENIR_AUTHORID, authorId);
                AVQuery<Souvenir> query1 = AVQuery.getQuery(SouvenirDao.TABLENAME);
                query1.whereEqualTo(SouvenirDao.SOUVENIR_AUTHORID, loverid);
                List<AVQuery<Souvenir>> queries = new ArrayList<>();
                queries.add(query);
                queries.add(query1);
                AVQuery<Souvenir> mainquery = AVQuery.or(queries);
                mainquery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
                mainquery.setLimit(size);
                mainquery.setSkip(size * page);
                mainquery.include(SouvenirDao.SOUVENIR_AUTHOR);
                mainquery.orderByDescending("createdAt");

                mainquery.findInBackground(new FindCallback<Souvenir>() {
                    @Override
                    public void done(List<Souvenir> list, AVException e) {
                        if (e == null) {
                            subscriber.onNext(list);
                        } else {
                            subscriber.onError(e);
                            Logger.e(e.getMessage());
                        }
                        subscriber.onCompleted();

                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    /**
     *上传文件
     * @param file
     * @return
     */
    public Observable<AVFile> UploadFile(final AVFile file) {
        return Observable.create(new Observable.OnSubscribe<AVFile>() {
            @Override
            public void call(final Subscriber<? super AVFile> subscriber) {
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(file);
                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();

                    }
                });
            }
        });
    }

    /**
     * 上传
     * @param avFile
     * @return
     */
    public Observable<String> UploadPicture(final AVFile avFile) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                avFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(avFile.getUrl());
                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 获取所有图片
     * @param authorId
     * @param theotherone
     * @param isFirst
     * @param size
     * @param page
     * @return
     */
    public Observable<List<Gallery>> FetchAllPicture(final String authorId, final String theotherone, final boolean isFirst, final int size, final int page) {
        return Observable.create(new Observable.OnSubscribe<List<Gallery>>() {
            @Override
            public void call(final Subscriber<? super List<Gallery>> subscriber) {
                AVQuery<Gallery> query = AVQuery.getQuery(Gallery.class);
                query.whereEqualTo(GalleryDao.AUTHORID, authorId);
                AVQuery<Gallery> query1 = AVQuery.getQuery(Gallery.class);
                query1.whereEqualTo(GalleryDao.AUTHORID, theotherone);
                List<AVQuery<Gallery>> queries = new ArrayList<>();
                queries.add(query);
                queries.add(query1);
                AVQuery<Gallery> mainquery = AVQuery.or(queries);
                mainquery.orderByDescending("createdAt");
                mainquery.include(GalleryDao.AUTHOR);
                if (isFirst) {
                    mainquery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
                } else {
                    mainquery.setLimit(size);
                    mainquery.skip(size * page);
                    mainquery.setCachePolicy(AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
                }
                mainquery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
                mainquery.findInBackground(new FindCallback<Gallery>() {

                    @Override
                    public void done(List<Gallery> list, AVException e) {
                        if (e == null) {
                            subscriber.onNext(list);

                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();

                    }
                });

            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 保存相册
     * @param gallery
     * @return
     */
    public Observable<Gallery> saveGallery(final Gallery gallery) {
        return Observable.create(new Observable.OnSubscribe<Gallery>() {
            @Override
            public void call(final Subscriber<? super Gallery> subscriber) {
                gallery.setFetchWhenSave(true);
                gallery.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(gallery);
                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 推送消息
     * @param content
     * @param action
     * @return
     */
    public Observable<Boolean> PushToLover(final String content, final int action) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                AVPush push = new AVPush();
                Map<String, Object> map = new HashMap<>();
                String installationId = User.getCurrentUser(User.class).getLoverInstallationId();
                map.put(NewsDao.CONTENT, content);
                map.put(NewsDao.ACTION, "com.ozj.baby.Push");
                map.put(NewsDao.AVATARURL, User.getCurrentUser(User.class).getAvatar());
                map.put(NewsDao.INSTALLATIONIID,
                        installationId);
                map.put(NewsDao.TITLE, action == 0 ? "Moment" : "相册");
                map.put(NewsDao.TIME, System.currentTimeMillis());
                push.setData(map);
                push.setMessage(content);
                push.setCloudQuery("select * from _Installation where installationId ='" + installationId
                        + "'");
//                push.setCloudQuery("select * from _Installation where installationId ='" + installationId
//                        + "'");
                push.sendInBackground(new SendCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(true);
                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();

                    }
                });

            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 版本号储存到LeanCloud
     * @return
     */
    public Observable<String> SaveInstallationId() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(AVInstallation.getCurrentInstallation().getInstallationId());

                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 注册到LeanCloud
     * @param username
     * @param passwd
     * @return
     */
    public Observable<User> login(final String username, final String passwd) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(final Subscriber<? super User> subscriber) {

                AVUser.logInInBackground(username, passwd, new LogInCallback<User>() {
                    @Override
                    public void done(User user, AVException e) {
                        if (e == null) {
                            subscriber.onNext(user);
                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }

                }, User.class);

            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 注册到LeanCloud
     * @param username
     * @param passwd
     * @return
     */
    public Observable<User> register(final String username, final String passwd) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(final Subscriber<? super User> subscriber) {

                final User user = new User();
                user.setUsername(username);
                user.setPassword(passwd);
                user.setNick(username);
                user.setFetchWhenSave(true);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(user);
                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        });

    }

    /**
     * 注册到环信
     * @param username
     * @param passwd
     * @return
     */
    public Observable<Boolean> HXRegister(final String username, final String passwd) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
/*                try {
                    EMClient.getInstance().createAccount(username, passwd);

                    subscriber.onNext(true);

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                subscriber.onCompleted();*/
            }
        });
    }

    /**
     * 登陆到环信
     * @param username
     * @param passwd
     * @return
     */
    public Observable<Boolean> HXLogin(final String username, final String passwd) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
/*                EMClient.getInstance().login(username, passwd, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    }

                    @Override



                    public void onError(int i, String s) {
                        subscriber.onError(new Throwable(s));
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });*/
            }
        });

    }

}

