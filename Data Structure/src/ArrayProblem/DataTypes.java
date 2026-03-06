package ArrayProblem;

class MethodEx{
    int add(int a, int b){
        return a+b;
    }

    int mul(int a, int b){
        return a*b;
    }
}

public class DataTypes {
    public static void main(String args[]) {
        MethodEx obj = new MethodEx();
        System.out.println(obj.add(4,5));
        System.out.println(obj.add(9,5));
        System.out.println(obj.mul(9,5));


        // byte
        byte a = 120;
        // short
        short b = 2000;
        // int
        int c = 20000;
        // long
        long d = 204009033243L;
        // float
        float e = 3.4123f;
        // double
        double f = 428.7523987293;
        // char
        char g = 'S';
        // boolean
        boolean h = true;

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
        System.out.println(e);
        System.out.println(f);
        System.out.println(g);
        System.out.println(h);
    }
}
