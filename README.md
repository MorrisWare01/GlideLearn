## Glide

### Glide加载图片流程

1. 先通过Glide.with(context)获取RequestManager
1. 再通过RequestManager.load(model)传入model获取RequestBuilder
1. 在RequestBuilder对象中可以添加图片显示属性RequestOptions,transition.listener等等
1. 最后调用into(target),构造Request,并通知RequestManager的requestTracker调用runRequest(request)触发request.begin()
1. request.begin()会先确定target的宽高，再将资源传递给Glide.engine的load方法
1. engine.load()先根据model等参数生成EngineKey，再从activeResources、MemoryCache内存缓存中获取EngineKey指向的Resource
1. 如果没有获取到，先查询Jobs中是否已经存在EngineKey的engineJob，有的话添加ResourceCallback等待任务完成，没有的话
构造engineJob和decodeJob开始资源model任务
1. decodeJob中通过run(ResourceCacheGenerator/DataCacheGenerator/SourceGenerator)加载model返回sourceKey和data(例如InputSteam)
1. ResourceCacheGenerator和DataCacheGenerator分别构造ResourceCacheKey和DataCacheKey获取Data
1. SourceGenerator先获取loadData获取Data，再调用cacheData()缓存{DataCache}到DiskLruCache
1. 根据data的class类型获取LoadPath，调用LoadPath.load(),遍历DecodePath.decode()将data转成Resource
1. DecodePath.decode将调用decoder将data转成Resource，再调用transform转化Resource
1. Resource加载完成后，decodeJob根据diskCacheStrategy缓存{ResourceCache}到DiskLruCache
1. 最终Resource会回调到SingleRequest的onResourceReady，执行监听器回调和动画

### Glide缓存机制

*Glide对图片数据使用了内存缓存和磁盘缓存。
内存缓存：只缓存处理过的Resource，包括target缓存的Request、Engine.ActiveResources对象中的HashMap、Glide全局实现了MemoryCache接口的LruResourceCache。
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


### Glide如何确定target的宽高

### Glide线程池

### GlideModule

### LruCache实现原理（Least Recently Used最近最少使用算法）


### DiskLruCache实现原理




