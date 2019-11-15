import annonation.Bean;
import annonation.Configuration;
import core.AnnotationBeanFactoryImpl;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-11-15 16:07
 **/
public class Bootstrap {

    private static List<Class<?>> configurationList=new ArrayList<>();

    /**
     * 启动类
     * @param args
     */
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        //1. 找到配置项
        //2. 注入bean 到 BeanFactory (ioc 控制反转)
        //3. 容器提供 bean (DI 依赖注入)

        AnnotationBeanFactoryImpl annotationBeanFactory=new AnnotationBeanFactoryImpl();

        //1. 获取当前类路径  递归回去 子路径 获取 需要的配置类
        String path = Bootstrap.class.getClassLoader().getResource("").getPath();
        scan(path,path.length());
        createBean(annotationBeanFactory);
        Object user = annotationBeanFactory.getBean("user");
        if (Objects.nonNull(user)){
            System.out.println(user.toString());
        }
    }

    private static void createBean(AnnotationBeanFactoryImpl annotationBeanFactory) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (configurationList.size()>0){
            for (Class<?> configClass : configurationList) {
                //将配置类中的 bean注入 beanFactory
                Method[] methods = configClass.getMethods();
                Object config = configClass.newInstance();
                for (Method method : methods) {
                    Bean beanAnnotation = method.getDeclaredAnnotation(Bean.class);
                    if (Objects.nonNull(beanAnnotation)){
                        // bean 名称
                        String[] beanName = beanAnnotation.name();
                        //反射创建对象
                        Object bean = method.invoke(config, null);
                        if (beanName.length>0){
                            for (String s : beanName) {
                                if (!annotationBeanFactory.containsBean(s)){
                                    //是否允许覆盖bean or 报错
                                    annotationBeanFactory.registerSingleton(s,bean);
                                }
                            }
                        }else {
                            // 没有取 方法名
                            annotationBeanFactory.registerSingleton(method.getName(),bean);
                        }
                    }
                }
            }
        }
    }

    private static void scan(String path,int index) throws ClassNotFoundException, IOException {
        File root=new File(path);
        File[] files = root.listFiles();
        if (files.length==0){
            return;
        }
        for (File file : files) {
            if (file.isDirectory()){
                scan(file.getPath(),index);
            }else {
                String filePath = file.getPath();
                String substring = filePath.substring(index);
                String replace = substring.split("\\.")[0].replace("/", ".");
                Class<?> configClass = ClassLoader.getSystemClassLoader().loadClass(replace);
                Annotation configuration = configClass.getDeclaredAnnotation(Configuration.class);
                if (Objects.nonNull(configuration)){
                    configurationList.add(configClass);
                }
            }
            System.out.println(file.getName());
        }
    }

}
