class A implements Runnable {
    public void run(){
        for(int i = 0; i < 5; i++){
            System.out.println("hello");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class B implements Runnable { 
    public void run(){
        for(int i = 0; i < 5; i++){
            System.out.println("World");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class DemoThread {
    public static void main(String[] args) {
        Runnable a = () -> {
            for(int i = 0; i < 5; i++){
                System.out.println("hello");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable b = new B();

        Thread t1 = new Thread(a);
        Thread t2 = new Thread(b);
        t1.start();
        t2.start();
    }
}

//multiple threads can run simultaneously, and the output may interleave "hello" and "World" in any order.
//threads are independent of each other, and the scheduling of threads is determined by the JVM and the underlying operating system. 
//mutation of shared resources should be handled carefully to avoid race conditions and ensure thread safety. In this example, there are no shared resources being modified, so we don't have to worry about synchronization. 

