package cumt.tj.learn.hadoop.hdfs.readdata;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by sky on 17-12-21.
 * 从hadoop URL读取数据
 */
public class URLCat {

    static{
        //采用hdfs的FsUrlStreamHandlerFactory作为url流的handler
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
    }

    public static void main(String[] args) {

        InputStream in=null;

        try {
            in=new URL(args[0]).openStream();
            //将数据显示到控制台，结束后不关闭数据流，自行在finally语句中删除
            IOUtils.copyBytes(in,System.out,4096,false);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeStream(in);
        }

    }

}
