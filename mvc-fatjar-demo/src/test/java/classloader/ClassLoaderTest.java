package classloader;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-08 18:21
 **/
public class ClassLoaderTest {


    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
        System.out.println(classLoader);
        ClassLoader parent = classLoader.getParent();
        System.out.println(parent);
        ClassLoader parent1 = parent.getParent();
        System.out.println(parent1);
        Class<?> helloClass = classLoader.loadClass("classloader.Hello1");
        String simpleName = helloClass.getSimpleName();
        Hello1 hello = (Hello1)helloClass.newInstance();
        System.out.println(simpleName);
        hello.setName("classloader");
        System.out.println(hello.getName());


        ClassLoader classLoader1=new CustomerClassLoader();
        ClassLoader classLoader2=new CustomerClassLoader1();
        Class<?> hello1 = classLoader1.loadClass("classloader.Hello");
        Class<?> hello2 = classLoader2.loadClass("classloader.Hello");
        System.out.println(hello1==hello2);
    }

}
