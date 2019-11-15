import annonation.Autowired;
import annonation.Bean;
import annonation.Configuration;
import annonation.Controller;
import controller.EchoController;
import core.AnnotationBeanFactoryImpl;
import core.BeanFactory;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-11-15 16:07
 **/
public class Bootstrap {

    private static List<Class<?>> configurationBeanList = new ArrayList<>();
    private static List<Class<?>> controllerList = new ArrayList<>();

    /**
     * 启动类
     *
     * @param args
     */
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        //1. 找到配置项
        //2. 注入bean 到 BeanFactory (ioc 控制反转)
        //3. 容器提供 bean (DI 依赖注入)

        AnnotationBeanFactoryImpl annotationBeanFactory = new AnnotationBeanFactoryImpl();

        //1. 获取当前类路径  递归回去 子路径 获取 需要的配置类
        String path = Bootstrap.class.getClassLoader().getResource("").getPath();
        scan(path, path.length());
        //2. 根据配置类创建bean
        createBean(annotationBeanFactory);

        registerController(annotationBeanFactory);

        //3. 从容器中 取bean
        Object user = annotationBeanFactory.getBean("user");
        if (Objects.nonNull(user)) {
            System.out.println(user.toString());
        }

        // 监听tcp端口
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new ClientThread(clientSocket,annotationBeanFactory)).start();
        }
    }

    private static void registerController(AnnotationBeanFactoryImpl annotationBeanFactory) throws InstantiationException, IllegalAccessException {
        for (Class<?> aClass : controllerList) {
            Object controller = aClass.newInstance();
            //DI 依赖注入
            Field[] declaredFields = controller.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Autowired declaredAnnotation = declaredField.getDeclaredAnnotation(Autowired.class);
                if (Objects.nonNull(declaredAnnotation)){
                    String[] name = declaredAnnotation.name();
                    for (String s : name) {
                        Object bean = annotationBeanFactory.getBean(s);
                        if (Objects.nonNull(bean)){
                            declaredField.setAccessible(true);
                            declaredField.set(controller,bean);
                            break;
                        }
                    }
                }
            }
            annotationBeanFactory.registerSingleton(controller.getClass().getName(), controller);
        }
    }

    static class ClientThread implements Runnable {

        private Socket client;
        private BeanFactory beanFactory;

        public ClientThread(Socket client, BeanFactory beanFactory) {
            this.client = client;
            this.beanFactory = beanFactory;
        }

        @Override
        public void run() {
            try {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line ;
                while ((line = bufferedReader.readLine()) != null
                        && !line.equals("")) {
                    stringBuilder.append(line).append("\n");
                }
                // 请求报文
                System.out.println("\r\n"+stringBuilder.toString());
                PrintWriter printWriter = new PrintWriter(
                        client.getOutputStream(), true);
                // tomcat 监听请求 -> servlet -> springMvc -> DispatcherServlet 路由
                // 简化只实现DI 全部路由到一个Controller
                Object controller = beanFactory.getBean(EchoController.class.getName());
                Method echo = controller.getClass().getMethod("echo", null);
                Object invoke = echo.invoke(controller, null);
                //封装响应报文 编码方式为中文
                printWriter.println("HTTP/1.1 200 OK \n" +
                        "Content-Type: text/html;Charset=UTF-8 \r\n\r\n"+
                        invoke
                );
                printWriter.flush();
                printWriter.close();
                bufferedReader.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createBean(AnnotationBeanFactoryImpl annotationBeanFactory) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (configurationBeanList.size() > 0) {
            for (Class<?> configClass : configurationBeanList) {
                //将配置类中的 bean注入 beanFactory
                registerBean(annotationBeanFactory, configClass);
            }
        }
    }

    private static void registerBean(AnnotationBeanFactoryImpl annotationBeanFactory, Class<?> configClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object config = configClass.newInstance();
        Method[] methods = configClass.getMethods();
        for (Method method : methods) {
            Bean beanAnnotation = method.getDeclaredAnnotation(Bean.class);
            if (Objects.nonNull(beanAnnotation)) {
                // bean 名称
                String[] beanName = beanAnnotation.name();
                //反射创建对象
                Object bean = method.invoke(config, null);
                if (beanName.length > 0) {
                    for (String s : beanName) {
                        if (!annotationBeanFactory.containsBean(s)) {
                            //是否允许覆盖bean or 报错
                            annotationBeanFactory.registerSingleton(s, bean);
                        }
                    }
                } else {
                    // 没有取 方法名
                    annotationBeanFactory.registerSingleton(method.getName(), bean);
                }
            }
        }
    }

    private static void scan(String path, int index) throws ClassNotFoundException, IOException {
        File root = new File(path);
        File[] files = root.listFiles();
        if (files.length == 0) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                scan(file.getPath(), index);
            } else {
                String filePath = file.getPath();
                String substring = filePath.substring(index);
                String replace = substring.split("\\.")[0].replace("/", ".");
                Class<?> configClass = ClassLoader.getSystemClassLoader().loadClass(replace);
                registerConfigBean(configClass);
                registerControllerBean(configClass);
            }
            System.out.println(file.getName());
        }
    }

    private static void registerControllerBean(Class<?> configClass) {
        Annotation configuration = configClass.getDeclaredAnnotation(Controller.class);
        if (Objects.nonNull(configuration)) {
            controllerList.add(configClass);
        }
    }

    private static void registerConfigBean(Class<?> configClass) {
        Annotation configuration = configClass.getDeclaredAnnotation(Configuration.class);
        if (Objects.nonNull(configuration)) {
            configurationBeanList.add(configClass);
        }
    }

}
