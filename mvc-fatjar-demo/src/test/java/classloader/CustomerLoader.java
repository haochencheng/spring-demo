package classloader;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 自定义类加载器加载同一个类
 * @description:
 * @author: haochencheng
 * @create: 2019-07-09 13:06
 **/
public class CustomerLoader {

    public static void main(String[] args) throws Exception {
        MyClassLoader myClassLoader = new MyClassLoader(null);
        Class<?> aClass = myClassLoader.findClass("classloader.Hello");
        System.out.println(aClass.getClassLoader());
        Method method = aClass.getMethod("say");
        method.invoke(aClass.newInstance(), null);
        MyClassLoader1 myClassLoader1 = new MyClassLoader1(null);
        Class<?> aClass1 = myClassLoader1.findClass("classloader.Hello");
        System.out.println(aClass1.getClassLoader());
        Method method1 = aClass1.getMethod("say");
        method1.invoke(aClass1.newInstance(), null);
        System.out.println(aClass1==aClass);
    }

    static class MyClassLoader extends ClassLoader {
        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            String myPath = "file:///Users/haochencheng/Workspace/java/demo/spring-demo/mvc-fatjar-demo/target/test-classes/" + name.replace(".", "/") + ".class";
            System.out.println(myPath);
            byte[] cLassBytes = null;
            Path path;
            try {
                path = Paths.get(new URI(myPath));
                cLassBytes = Files.readAllBytes(path);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
            Class clazz = defineClass(name, cLassBytes, 0, cLassBytes.length);
            return clazz;
        }

        protected MyClassLoader(ClassLoader parent) {
            super(parent);
        }

    }

    static class MyClassLoader1 extends CustomerLoader.MyClassLoader {

        protected MyClassLoader1(ClassLoader parent) {
            super(parent);
        }

    }


}
