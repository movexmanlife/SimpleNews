package com.robot.simplenews.event;

import android.support.annotation.NonNull;

import com.robot.simplenews.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * 总线通信Rxjava的实现
 */
public final class RxBus {
    private static final String TAG = RxBus.class.getSimpleName();
    private volatile static RxBus sInstance;

    private RxBus() {
    }

    public static RxBus get() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }

    /**
     * 存储某个标签的Subject集合
     */
    private ConcurrentMap<Object, List<Subject>> mSubjectMapper = new ConcurrentHashMap<>();

    /**
     * 注册事件
     *
     * @param tag   标签
     * @param clazz 类
     * @param <T>   类型
     * @return 被观察者
     */
    public <T> Observable<T> register(@NonNull String tag, @NonNull Class<T> clazz) {
        LogUtil.e(TAG, "register:" + Thread.currentThread().getName());
        List<Subject> subjectList = mSubjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            mSubjectMapper.put(tag, subjectList);
        }

        Subject<T, T> subject;
        // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
        subjectList.add(subject = PublishSubject.create());
        return subject;
    }

    /**
     * 取消注册事件
     *
     * @param tag        标签
     * @param observable 被观察者
     */
    public void unregister(@NonNull String tag, @NonNull Observable observable) {
        final List<Subject> subjectList = mSubjectMapper.get(tag);
        if (null != subjectList) {
            subjectList.remove(observable);
            if (subjectList.isEmpty()) {
                mSubjectMapper.remove(tag);
            }
        }
    }

    /**
     * 发送事件
     *
     * @param tag     标签
     * @param content 发送的内容
     */
    @SuppressWarnings("unchecked")
    public void post(@NonNull String tag, @NonNull Object content) {
        final List<Subject> subjectList = mSubjectMapper.get(tag);
        if (null != subjectList && !subjectList.isEmpty()) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
            }
        }
    }
}