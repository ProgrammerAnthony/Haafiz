package com.anthony.app.common.data;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 用于管理RxBus的事件和Rxjava相关代码的生命周期处理
 * Created by hpw on 16/10/27.
 */

public class RxManager {


//    private Map<String, Observable<?>> mObservables = new HashMap<>();// 管理观察源
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();// 管理订阅者者




    public void add(Subscription m) {
        mCompositeSubscription.add(m);
    }

    public void clear() {
        mCompositeSubscription.unsubscribe();// 取消订阅

    }

}