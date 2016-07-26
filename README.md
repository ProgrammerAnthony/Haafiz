#this project is redo using new techs , not instructive these days
#本项目目前改版重构中，暂时可能出现编译问题。loading 。。。。。。。。

# MVPCommon
**a common Architecture and project build with blogs releasing based on MVP，integrates many Open Source Projects ,to make your developing quicker and easier.**

**一个集成了众多开源项目的Android MVP框架以及应用搭建，全程博客更新。**




## Blog

[【从零开始搭建android框架系列】](http://www.jianshu.com/notebooks/3767449/latest)

## Display Gif

![](http://upload-images.jianshu.io/upload_images/1833901-54cbfa2b27652e3f.gif?imageMogr2/auto-orient/strip)

Functionality & Libraries
-------

>toEdit !!!!  in progress!!!!

##### 1 MVP
和google官网类似的MVP设计
##### 2 Materil Design
##### 3 Dagger2
Dagger2注入（等待引入）
##### 4 Realm
project's build.gradle 

	dependencies {
        classpath "io.realm:realm-gradle-plugin:0.91.0"
    }
application levele build.gradle

	apply plugin: 'realm-android'
Realm实现本地数据存储。

##### 5 RxJava,RxAndroid
    //RxAndroid
    compile 'io.reactivex:rxandroid:1.2.0'
    //RxJava
    compile 'io.reactivex:rxjava:1.1.5'
    //RxPermission
    compile 'com.tbruyelle.rxpermissions:rxpermissions:0.7.0@aar'
使用RxJava实现EventBus(RxBus)
##### 6 easemob(环信)
使用环信作为即时通讯（目前还是阿里的OpenIM，todo）
##### 7 LeanCloud
使用LeanCloud作为后端存储。关于LeanCloud集成和使用，请更多参考[LeanCloud官网](https://leancloud.cn/)

	// LeanCloud 基础包
    compile ('cn.leancloud.android:avoscloud-sdk:v3.+')

    // 推送与实时聊天需要的包
    compile ('cn.leancloud.android:avoscloud-push:v3.+@aar'){transitive = true}

    // LeanCloud 统计包
    compile ('cn.leancloud.android:avoscloud-statistics:v3.+')

    // LeanCloud 用户反馈包
    compile ('cn.leancloud.android:avoscloud-feedback:v3.+@aar')

    // avoscloud-sns：LeanCloud 第三方登录包
    compile ('cn.leancloud.android:avoscloud-sns:v3.+@aar')
    compile ('cn.leancloud.android:qq-sdk:1.6.1-leancloud')
    // 目前新浪微博官方只提供 jar 包的集成方式
    // 请手动下载新浪微博 SDK 的 jar 包，将其放在 libs 目录下进行集成

    // LeanCloud 应用内搜索包
    compile ('cn.leancloud.android:avoscloud-search:v3.+@aar')

##### 8 BaseRecyclerViewAdapterHelper
	compile com.github.CymChad:BaseRecyclerViewAdapterHelper:v1.7.0
##### 9 Logger
	compile 'com.orhanobut:logger:1.11'
使用Logger进行日志打印，比较直观。封装到工具类LogUtils中进行初始化。
##### 10 materialish-progress
	compile 'com.pnikosis:materialish-progress:1.7'
##### 11 Glide
    compile 'com.github.bumptech.glide:glide:3.7.0'
使用glide封装的ImageLoader，作为图片加载工具。
##### 12 swipemenulistview
    compile 'com.baoyz.swipemenulistview:library:1.3.0'
##### 13 umeng alalysis
    compile 'com.umeng.analytics:analytics:latest.integration'
##### 14 ButterKnife
    compile 'com.jakewharton:butterknife:7.0.1'
使用ButterKnife
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
登陆注册界面下方文字闪动效果。
##### 21 okHttp
	compile 'com.squareup.okhttp3:okhttp:3.2.0'
利用OkHttp封装的HttpUtil进行访问网络。
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
    compile project(':ultraptrwithloadmore')
    compile project(':viewpagerindicator')
    compile project(':sweetDialog')
    compile files('libs/okhttp-2.7.0.jar')
    compile files('libs/okhttputils-2_3_4.jar')

利用阿里巴巴的OpenImSDK作为即时通讯组件(聊天列表界面和联系人列表界面来自OpenIM)。

sweetDialog 提供统一化的加载dialog效果。

##### 28 其他

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
