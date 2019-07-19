package spring.with.nacos;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-15 23:03
 **/
public class MockController {

    public static void main(String[] args) {
        final String pig = "length: 10";
        final String dog = "length: " + pig.length();
        System.out.println(pig == dog);
        System.out.println(pig.hashCode());
        System.out.println(dog.hashCode());
        System.out.println();


        int[] from=new int[]{1};
        int[] to=new int[1];
        System.arraycopy(from,0,to,0,1);
        System.out.println(from==to);
        System.out.println(from.hashCode());
        System.out.println(to.hashCode());
    }
}
