# 写入数据

* 运行程序，将本地文件系统中的文件，复制到HDFS中：

```
hadoop cumt.tj.learn.hadoop.hdfs.writedata.FileCopyWithProgress ~/.bashrc hdfs://localhost:9000/user/sky/.bashrc
```

* 使用hadoop命令，查看hdfs中的文件是否与实际文件相符：

```
hadoop fs -cat .bashrc
```
