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
