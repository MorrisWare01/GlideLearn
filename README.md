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
1. decodeJob中通过ResourceCacheGenerator、DataCacheGenerator、SourceGenerator加载model
1. 各种DataFetcherGenerator会从Glide构造时注册的Registry中查找modelLoader
1.

### Glide缓存机制

1. 从target的getRequest获取上一次的Request
1. Engine.load()中先构造EngineKey
1. 从Engine的activeResources中根据EngineKey获取EngineResource（activeResources中维持一个HashMap）
1. 从Glide.memoryCache中根据EngineKey获取EngineResource（memoryCache实现了LruCache）
1. 内存缓存获取失败后，构造engineJob和decodeJob开始从磁盘缓存和网络请求获取
1. decodeJob的run方法会从ResourceCacheGenerator、DataCacheGenerator、SourceGenerator中获取Resource
1. ResourceCacheGenerator中构造ResourceCacheKey从本地DiskLruCache中获取（
Glide默认位置为context.getCacheFile（"image_manager_disk_cache"）,最大大小为250MB）
1.


### Glide如何确定target的宽高

### Glide线程池

### GlideModule

### LruCache实现原理

### DiskLruCache实现原理




