abstract class A{
    public abstract void display();
}

public class Demo{
    public static void main(String[] args) {
        A a = new A() {
            @Override
            public void display() {
                System.out.println("Hello World");
            }
        };
        a.display();;
}
}