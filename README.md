## Glide

### Glide加载图片流程

1. 先通过Glide.with(context)获取RequestManager
1. 再通过RequestManager.load(model)传入model获取RequestBuilder
1. 在RequestBuilder对象中可以添加图片显示属性RequestOptions,transition.listener等等
1. 最后调用into(target),构造Request,并通知RequestManager的requestTracker调用runRequest(request)触发request.begin()
1. request.begin()会先确定target的宽高，再将model等参数传递给Glide.engine的load方法
1. engine.load()先根据model等参数生成EngineKey，再从activeResources、MemoryCache内存缓存中获取EngineKey指向的Resource
1. 如果没有获取到，先查询Jobs中是否已经存在EngineKey的engineJob，有的话添加ResourceCallback等待任务完成，没有的话
构造engineJob和decodeJob开始model加载任务
1. decodeJob中通过run(ResourceCacheGenerator/DataCacheGenerator/SourceGenerator)加载model返回sourceKey和data(例如InputSteam)
1. ResourceCacheGenerator和DataCacheGenerator分别构造ResourceCacheKey和DataCacheKey获取ResourceCache和DataCache
1. SourceGenerator先获取loadData获取Data，再调用cacheData()缓存{DataCache}到DiskLruCache
1. 获取data完成后，decodeJob将根据data的class类型获取LoadPath，调用LoadPath.load()
1. LoadPath.load遍历DecodePath.decode()将data转成Resource
1. DecodePath.decode()将调用decoder将data转成Resource，再调用transform转化Resource
1. Resource加载完成后，decodeJob根据diskCacheStrategy缓存{ResourceCache}到DiskLruCache
1. 最终Resource会回调到SingleRequest的onResourceReady，执行监听器回调和动画

### Glide缓存机制

*Glide对图片数据使用了内存缓存和磁盘缓存。
内存缓存：只缓存处理过的Resource。缓存位置：target缓存的Request、Engine.ActiveResources对象中的HashMap、Glide全局实现了MemoryCache接口的LruResourceCache。
磁盘缓存：使用了DiskLruCache，默认文件位置为context.getCacheFile（"image_manager_disk_cache"）,最大大小为250MB。
通过构造ResourceCacheKey和DataCacheKey缓存处理过的Resource对象和Data原始数据*

1. 从target的getRequest获取上一次的Request
1. Engine.load()中先构造EngineKey
1. 从Engine的activeResources中根据EngineKey获取EngineResource（activeResources中维持一个HashMap）
1. 从Glide.memoryCache中根据EngineKey获取EngineResource（memoryCache实现了LruCache）
1. 内存缓存获取失败后，构造engineJob和decodeJob开始从磁盘缓存和网络请求获取
1. decodeJob的run方法会从ResourceCacheGenerator、DataCacheGenerator、SourceGenerator中获取Resource
1. ResourceCacheGenerator中构造ResourceCacheKey从本地DiskLruCache中获取
1. DataCacheGenerator中构造DataCacheKey从本地DiskLruCache中获取
1. SourceGenerator中会将获取到的Data数据缓存到DiskLruCache
1. DecodePath最终decode完Resource后，在decodeJob中将Resource缓存到DiskLruCache

##### 注

1. 在Glide.memoryCache中获取的EngineResource会被添加到Engine的activeResources中
1. 在Resource被释放后，会将Resource缓存到memoryCache

### Glide生命周期

* Glide的RequestManager绑定了Fragment的生命周期和CONNECTIVITY_ACTION广播。
* 当fragment的onStart回调时会触发requestManager内部resumeTracker恢复请求。
* 当fragment的onStop回调时会触发requestManager内部pauseTracker停止请求。
* 当接收到CONNECTIVITY_ACTION广播同时网络是连接状态时触发requestManager内部requestTracker重新请求数据

### Glide如何确定target的宽高

* target的宽高是在SingleRequest.begin()时确定的
* 当RequestOptions配置了overrideWidth和overrideHeight并且符合条件，那么宽高就确定了
* 不符合条件时，会调用target.getSize(callback)方式获取宽高
* 当ViewTarget的getSize()发生在View还没计算完成时，获取的宽高会不符合条件，这时
ViewTarget会调用View.getViewObserver().addOnPreDrawListener()获取宽高

### Glide线程池

* Glide中维持了DiskCacheExecutor,SourceExecutor,SourceUnlimitedExecutor,AnimationExecutor4种线程池
* DiskCacheExecutor负责读取ResourceCache和DataCache，默认个数为1个，采用PriorityBlockingQueue
* SourceExecutor负责获取图片数据和写入ResourceCache和DataCache，默认个数为处理器个数~4个，采用PriorityBlockingQueue
* SourceUnlimitedExecutor线程数无上限，采用SynchronousQueue
* AnimationExecutor线程数为1~2个，采用PriorityBlockingQueue
* SourceUnlimitedExecutor和AnimationExecutor的作用和SourceExecutor一样，但是默认不工作，需要配置标识位

### GlideModule

* GlideModule提供了applyOptions和registerComponents
* applyOptions用于自定义GlideBuilder参数
* registerComponents用于注册组件到Registry

### Glide如何处理列表图片错位问题

* 错误原因：类似RecyclerView控件的item在离开屏幕后会被重新使用达到复用效果，这时因加载网络图片有延时，
就有可能导致图片加载的imageView控件已经离开屏幕被复用了，所以会发生错误效果
* 解决方案：在加载时设置tag，显示时判断tag。
* glide解决方案：在RequestBuilder.into(target)方法中，先获取target.getRequest上一次请求，如何与构造的新请求不同则清除请求。

### LruCache实现原理（Least Recently Used最近最少使用算法）



### DiskLruCache实现原理




