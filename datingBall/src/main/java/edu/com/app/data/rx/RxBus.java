package edu.com.app.data.rx;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Anthony on 2016/5/23.
 * Class Note:
 * use RxJava to implement EventBus
 * (Sticky event @see{http://reactivex.io/documentation/subject.html})
 */
@Singleton
public class RxBus {

    private static volatile RxBus defaultInstance;
    // 主题
    private final Subject bus;
    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    @Inject
    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    // 提供了一个新的事件
    @SuppressWarnings("unchecked")
    public void post (Object o) {
        bus.onNext(o);
    }
    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    @SuppressWarnings("unchecked")
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }
}
