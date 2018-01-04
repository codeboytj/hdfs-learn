# Configuration

Hadoop有太多的配置文件，配置项，在官方文档里面都有。

---

## 初始化Configuration

在使用hadoop时，通常是由初始化配置文件实例开始的`Configuration conf=new Configuration()`。这句话背后执行了一些过程：

* 检查是否存在deprecated配置文件`hadoop-site.xml`，存在会弹出警告，但不会加载
* “加载”default配置文件`core-default.xml`、`core-site.xml`。这里的“加载”并不是真的加载，只是把`core-default.xml`和`core-site.xml`两个字符串添加到一个CopyOnWriteArrayList中去

就干了这几件事，还真是懒到要命
