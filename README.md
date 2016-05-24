# MVPCommon
**a common Architecture and project build with blogs releasing based on MVP，integrates many Open Source Projects ,to make your developing quicker and easier.**

**一个集成了众多开源项目的Android MVP框架以及应用搭建，全程博客更新。**




## Blog

[【从零开始搭建android框架系列】](http://www.jianshu.com/notebooks/3767449/latest)

## Display Gif

![](http://upload-images.jianshu.io/upload_images/1833901-54cbfa2b27652e3f.gif?imageMogr2/auto-orient/strip)



## Functionality

1 BaseActivity & BaseFragment for mostly common operation.

BaseActivity 和BaseFragment 初始化大多数操作。

2 swipe back with gesture in activity.

支持activity滑动返回。

3 many utils classes for developing

开发中常用的工具类

4 MVP design from google example

和google官网类似的MVP设计

5 network status observer

网络状态监听

6 interface of loading view,empty view ,error view for all views.

为所有界面添加了加载界面，错误界面以及空界面的接口

7 ImageLoader encapsulation using Glide

使用glide封装的ImageLoader，作为图片加载工具

8 HttpUtil to load network using OkHttp

利用OkHttp封装的HttpUtil访问网络

9 Instant Messaging(IM) support with OpenImSDK from Alibaba(using Conversation list &Contacts list from OpenIM)

利用阿里巴巴的OpenImSDK作为即时通讯组件(聊天列表界面和联系人列表界面来自OpenIM)

9 News Info from (m.hupu.com)，Inject JavaScript to modify

新闻资讯内容来自于虎扑手机网页版，通过注入JavaScript的方式做了网页的修改。

10 Setting pages using PreferenceFragment to support Android 3.0 or higher

设置页面使用PreferencFragment（支持安卓3.0及以上）

11 LoginActivity to login in OpenIMSDK with one key

新增LoginActivity，一键登录
Development environment & Libraries
-------
>toEdit !!!!  in progress!!!!
##### 1 MVP
MVP design from google example

和google官网类似的MVP设计
##### 2 Materil Design
##### 3 Dagger2
##### 4 Realm
project's build.gradle 

	dependencies {
        classpath "io.realm:realm-gradle-plugin:0.91.0"
    }
application levele build.gradle

	apply plugin: 'realm-android'
##### 5 RxJava,RxAndroid
    //RxAndroid
    compile 'io.reactivex:rxandroid:1.2.0'
    //RxJava
    compile 'io.reactivex:rxjava:1.1.5'
    //RxPermission
    compile 'com.tbruyelle.rxpermissions:rxpermissions:0.7.0@aar'
##### 6 easemob(环信)
##### 7 LeanCloud
##### 8 BaseRecyclerViewAdapterHelper
	compile com.github.CymChad:BaseRecyclerViewAdapterHelper:v1.7.0
##### 9 Logger
	compile 'com.orhanobut:logger:1.11'
##### 10 materialish-progress
	compile 'com.pnikosis:materialish-progress:1.7'
##### 11 Glide
    compile 'com.github.bumptech.glide:glide:3.7.0'
##### 12 swipemenulistview
    compile 'com.baoyz.swipemenulistview:library:1.3.0'
##### 13 umeng alalysis
    compile 'com.umeng.analytics:analytics:latest.integration'
##### 14 ButterKnife(todo new is 8.0.1,but not support Zelezny,so using 7.0.1)
    compile 'com.jakewharton:butterknife:7.0.1'
##### 15 beautiful loading view
    compile 'com.wang.avi:library:1.0.1'
##### 16 about page util
    compile 'com.github.medyo:android-about-page:1.0.8'
##### 17 photoview
    compile 'com.github.chrisbanes.photoview:library:1.2.4'
##### 18 likeButton
    compile 'com.github.jd-alexander:LikeButton:0.2.0'
##### 19 Materil-login view
    compile 'com.github.shem8:material-login:1.4.0'
##### 20 shimmer
    compile 'com.facebook.shimmer:shimmer:0.1.0@aar'
##### 21 okHttp
	compile 'com.squareup.okhttp3:okhttp:3.2.0'
##### 22 EasyImage
    //Library for picking pictures from gallery or camera
    compile 'com.github.jkwiecien:EasyImage:1.2.3'
##### 23 ucrop
    //img crop library
    compile 'com.yalantis:ucrop:1.5.0'
##### 24 blurry
    compile 'jp.wasabeef:blurry:2.0.2'
##### 25 recyclerview animation
    compile 'jp.wasabeef:recyclerview-animators:2.2.3'
##### 26 fabprogresscircle
    //Material progress circle around any FloatingActionButton.
    compile 'com.github.jorgecastilloprz:fabprogresscircle:1.01@aar'
##### 27 others
    compile project(':openimSDK')
    compile project(':pagerslidingtabstrip')
    compile project(':pulltorefreshhandmark')
    compile project(':slidingmenu')
    compile project(':ultraptrwithloadmore')
    compile project(':viewpagerindicator')
    compile project(':sweetDialog')
    compile files('libs/okhttp-2.7.0.jar')
    compile files('libs/okhttputils-2_3_4.jar')

Download
--------
![](http://upload-images.jianshu.io/upload_images/1833901-f02604a7325a2b19.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

扫描二维码下载
或用手机浏览器输入这个网址: http://fir.im/j6nb

License
-------

    Copyright 2016 CameloeAnthony

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
