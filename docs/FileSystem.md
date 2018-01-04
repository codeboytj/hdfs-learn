# FileSystem

FileSystem是Hadoop中文件系统的抽象类。它的实现类很多，对于单击模式使用的本地文件系统，有LocalFileSystem；对于hdfs，有DistributedFileSystem。

---

## FileStem.get(Configuration conf)

这个静态工厂方法，用来根据配置获得相应的FileSystem实现。它背后干了些什么事呢：

* 读取fs.defaultFs配置，如`hdfs://localhost:9000`
    * 注意：在使用`Configuration conf=new Configuration()`初始化Configuration实例的时候，并没有真正加载配置文件。此时在读取fs.defaultFs时，会加载配置文件，添加配置到conf中的一个Properties实例属性中去。
* 解析fs.defaultFs配置中的`scheme`和`authority`，即`hdfs`和`localhost:9000`，`scheme`是用来找配置文件的，如`scheme`的值为`hdfs`的时候，相应的配置文件就是`hdfs-site.xml`
* 查找相应FileSystem实现。根据`scheme`，查找配置文件中的`fs.hdfs.impl`项，如果配置文件中指定了特定的FileSystem实现，则返回，没有，则使用`DistributedFileSytem`
    * FileSystem持有一个缓存池。找实例时，FileSystem会先查看配置文件`fs.hdfs.impl.disable.cache`的值是否为true，是则从缓存池中找相应的FileSystem实例，没有就创建一0个，有就返回，否则就直接创建。
* 初始化DistributedFileSystem实例，这其中包括使用反射调用构造函数，以及调用类的initialize()方法。
    * initialize()方法做的事情非常多，包括实例化DFSClient，与NameNode沟通的代理类，设置工作空间（`hdfs://localhost:9000/user/username`）等等
	* 调用父类（FileSystem）的initialize()方法。获得一个Statistics引用。这个东西是用来统计某个FileSystem的读写数据的。这个东西是每个类只对应一个实例，所以会有很多线程写一个实例。内部使用了ThreadLocal，让每个线程写数据的时候互不干扰。
	* 实例化DFSClient，DFSClient对这个类做了很清楚的解释：`DFSClient can connect to a Hadoop Filesystem and perform basic file tasks.  It uses the ClientProtocol to communicate with a NameNode daemon, and connects directly to DataNodes to read/write block data. Hadoop DFS users should obtain an instance of DistributedFileSystem, which uses DFSClient to handle filesystem tasks.`。与NameNode和DataNode进行交互，DistrubutedFileSystem持有对DFSClient的引用，用户操作DistributedFileSystem而不是DFSClient。
	* DFSClient会根据配置文件生成`SpanReceiverHost`与`SpanReceiver`,它用来记录一些东西，具体记录什么我还没弄懂。`SpanReceiverHost`持有对`SpanReceiver`的引用，拥有一个`SpanReceiver`的TreeMap，这是有个有顺序的Map，从`SpanReceiverHost`源码的`loadSpanReceivers()`方法来看，默认是按照配置文件中的顺序存储的。配置文件中关于`SpanReceiver`的配置是在`hadoop.htrace.spanreceiver.classes`项，没配置就不会生成。初次之外，`SpanReceiverHost`还会利用`ShutdownHookManager`注册在JVM关闭之前关闭持有的所有`SpanReceiver`的线程。
	* DFSClient会持有一个与NameNode交流的Proxy，通过socket进行通信


*可以看出DistributedFileSystem的initialize()方法是很重要的，有时间再研究吧*
