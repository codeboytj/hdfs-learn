# 写入数据

## 运行程序

* 运行程序，将本地文件系统中的文件，复制到HDFS中：

```
hadoop cumt.tj.learn.hadoop.hdfs.writedata.FileCopyWithProgress ~/.bashrc hdfs://localhost:9000/user/sky/.bashrc
```

* 使用hadoop命令，查看hdfs中的文件是否与实际文件相符：

```
hadoop fs -cat .bashrc
```

## namenode与datanode

在hadoop集群中，文件块通常会有多个副本分别存储在不同的datanode上面。那么namenode是如何决定文件块存储到哪些datanode上，以及怎么将副本存储在相应的datanode上的呢？

namenode在选取datanode时，不仅要考虑写入带宽、读取带宽，还要考虑可靠性，提供真正的冗余。比如如果将所有块写入到一个datanode上面，虽然保证了带宽，但是没有实现真正的冗余，当一个datanode挂掉的时候，这个文件就gameover了。namenode最终采用这样的方式写入副本：

* 在运行客户端的节点上放第一个副本，如果客户端运行在集群之外，就随机选择一个节点
* 在第一个节点所在机架之外，随机选择一个机架的节点，存储第二个副本
* 第二个副本所在机架上，随机选择另一个节点，存储第三个副本
* 如果还需要存储其他副本，就在集群中随机选择节点存储 

那么如何将副本存储在这些datanode上呢：

* 写入数据时，数据会分成一个个数据包，形成“数据队列”
* namenode选取的datanodes构成了一个管线
* 数据流传入第一个datanode，datanode存储完数据包后，将它们发送给第二个datanode，依次类推
* 如果一切成功，所有datanode的返回形成“确认队列”。当收到管道内的所有datanode的确认队列之后，数据包才会从确认队列中删除
* 如果某个datanode在写入数据期间发生故障，那么需要执行以下操作：
    * 关闭管线，将所有数据包添加回数据队列的前端，以确保下下一个datanode不会漏掉任何数据
    * 在已经存储块，且运行正常的datanode的块中添加新标识，传送给namenode，以便故障datanode恢复正常后可以删除已经存储的部分数据块
    * 从管线中删除故障datanode，其他的datanode构成一个新管线，继续进行数据写入。当namenode注意到副本数不足的时候，会在另一个datanode上创建新的副本。
* namenode在等待最小副本数（dfs.namenode.replication.min，默认为1）写入成功后，就能返回写入数据成功。因为它已经知道了文件由哪些块组成，写副本是可以后续或者异步进行的。
