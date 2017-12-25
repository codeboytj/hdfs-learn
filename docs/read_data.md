# 读取数据

## 从Hadoop Url读取数据


* 在hadoop根目录下，先启动hdfs：

```
./sbin/start-dfs.sh
```

* 在环境变量中配置hadoop，以便在命令行使用hadoop：

```
#hadoop环境变量
export HADOOP_HOME=/usr/local/hadoop
export PATH=$PATH:$HADOOP_HOME/bin
```

* 将java程序类路径，临时添加到hadoop的classpath：

```
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:./
```

* 运行程序

```
hadoop cumt.tj.learn.hadoop.hdfs.readdata.URLCat hdfs://localhost:9000/user/sky/README.txt
```

## 通过FileSystem API读取数据


* 运行程序

```
hadoop cumt.tj.learn.hadoop.hdfs.readdata.FileSystemCat hdfs://localhost:9000/user/sky/README.txt
```

## 区别

* 通过URL读取的时候，由于使用了URL类的静态方法setURLStreamHandlerFactory()。通过查看源码发现：
```
public static void setURLStreamHandlerFactory(URLStreamHandlerFactory fac) {
	synchronized (streamHandlerLock) {
		if (factory != null) {
			throw new Error("factory already defined");
		}
		SecurityManager security = System.getSecurityManager();
		if (security != null) {
			security.checkSetFactory();
		}
		handlers.clear();
		factory = fac;
	}
}
```
当其他程序调用setURLStreamHandlerFactory(……)方法时，会直接产生Error。因此一个jvm虚拟中只能使用一种URLStreamHandlerFactory。然而使用FileSystem API读取数据时就不会出现这种问题。

## namenode与datanode

HDFS中的namenode为管理节点，而工作节点是datanode。

* namenode管理文件系统的命名空间，维护着文件系统树及整棵树内所有文件和目录，这些信息以命名空间镜像文件和编辑日志文件永久保存在本地磁盘上。另外，namenode还记录着每个文件中的各个块的数据节点信息。
* datanode是文件系统的工作节点，根据需要存储并检索数据块，并定期向namenode发送所存储的块的列表。

在读取数据的时候，DistributedFileSystem会调用namenode，以确定文件的每一个块的所在的datanode地址。而通常文件块会有多个副本在不同的datanode上面，那么namenode怎样的顺序返回datanode的地址呢：

* 同一节点上的进程
* 同一机架上的不同节点
* 同一数据中心中不同机架上的节点
* 不同数据中心中的节点
