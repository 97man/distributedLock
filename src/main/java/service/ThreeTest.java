package service;

public class ThreeTest {

    public static void main(String[] args) {
        //java.lang下面的方法不需要倒入
        int x = 0, y = 5, z = 2;
        int i = y | z;
        Long l=1234l;
        System.out.println(l.toString());
        System.out.println("i 为"+ i);
        System.out.println("-inf".getBytes());
        String s = "x,y,z";
        s+="(summed)=";
        System.out.println(s+(x+y+z));
        System.out.println(s + x + y + z);
    }
}