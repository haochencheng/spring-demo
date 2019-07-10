package classloader;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-08 20:48
 **/
public class BootstrapTest {

    public static void main(String[] args) {
        //bootstrapClassLoader
        System.out.println(System.getProperty("sun.boot.class.path"));
        // -D java.ext.dirs 指定 ext路径 extClassLoader
        System.out.println(System.getProperty("java.ext.dirs"));
        // appClassLoader
        System.out.println(System.getProperty("java.class.path"));

    }

}
