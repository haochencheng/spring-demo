package classloader;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-08 23:12
 **/
public class OneClassLoaderTest {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader classLoader1=new CustomerClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader1);
        Class<?> hello = classLoader1.loadClass("Hello");
        Hello o =(Hello) hello.newInstance();
        System.out.println(o.getHello1().getClass().getClassLoader());
        System.out.println(hello.getClassLoader());
    }

}
