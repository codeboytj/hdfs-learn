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
