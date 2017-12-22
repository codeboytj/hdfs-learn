package cumt.tj.learn.hadoop.hdfs.readdata;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class FileSystemCat {

    public static void main(String[] args) {

        String uri=args[0];

        Configuration conf= new Configuration();

        InputStream in=null;

        try {
            FileSystem fs=FileSystem.get(conf);
            in=fs.open(new Path(uri));
            IOUtils.copyBytes(in,System.out,4096,false);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(in);
        }
    }

}
