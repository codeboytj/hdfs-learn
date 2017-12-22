package cumt.tj.learn.hadoop.hdfs.writedata;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.*;


public class FileCopyWithProgress {

    public static void main(String[] args) {

        String srcUri=args[0];
        String dstUri=args[1];

        InputStream in=null;
        Configuration conf=new Configuration();

        OutputStream out=null;

        try {
            in=new BufferedInputStream(new FileInputStream(srcUri));

            FileSystem fs=FileSystem.get(conf);
            //使用lambda表达式代替繁琐的匿名内部类。
            // Progressable接口的progress()方法为回调方法，
            // hadoop在每次写完64KB的数据之后会调用该回调，从而显示进度。
            out=fs.create(new Path(dstUri), () -> System.out.print("."));
//            out=fs.create(new Path(dstUri), new Progressable() {
//                @Override
//                public void progress() {
//                    System.out.print(".");
//                }
//            });

            IOUtils.copyBytes(in,out,4096,false);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(out);
            IOUtils.closeStream(in);
        }

    }

}
