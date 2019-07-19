package spring.with.nacos;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-18 19:12
 **/
public class Lazy {


    private static boolean initialized = false;

    static {
        Thread t = new Thread(() ->
        {
            System.out.println("aaa");
            initialized = true;
        }
        );
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(initialized);
    }

}
