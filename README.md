# MVPCommon
**a common Architecture for Android Applications developing based on MVP，integrates many Open Source Projects ,to make your developing quicker and easier.**

**一个集成了众多开源项目的Android MVP快速开发框架。**


blog update with all of the process

**全程博客更新：**

1 [开篇介绍和工程目录结构](http://www.jianshu.com/p/d0fee882a0fe)

2 [Android架构合集](http://www.jianshu.com/p/1f21e1d375aa)

3 [google官方架构MVP解析与实战](http://www.jianshu.com/p/569ab68da482)

4 [网络图片加载的封装](http://www.jianshu.com/p/e26130a93289)

5 [谈谈WebView的使用](http://www.jianshu.com/p/e3965d3636e7)

**目前效果图：**

![](http://upload-images.jianshu.io/upload_images/1833901-54cbfa2b27652e3f.gif?imageMogr2/auto-orient/strip)

**目前功能**

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