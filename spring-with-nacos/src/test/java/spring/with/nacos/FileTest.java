package spring.with.nacos;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-23 22:37
 **/
public class FileTest {

    public static void main(String[] args) {
        File file = new File("/Users/haochencheng/Workspace/java/demo/spring-demo/spring-with-nacos/README.md");
        System.out.println(file.getName());
        System.out.println(transForDate(file.lastModified()));
//        long l = System.currentTimeMillis();
//        System.out.println(file.setLastModified(l));
//        System.out.println(l);
    }

    public static Date transForDate(long ms) {
        long msl = ms;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date temp = null;
        try {
            String str = sdf.format(msl);
            temp = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }


}
