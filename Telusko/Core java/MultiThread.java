class Counter{
    int count = 0;

    public synchronized void increment(){
        count++;
    }
}

public class MultiThread {
    public static void main(String[] args) throws InterruptedException {

        Counter counter = new Counter();

        Runnable obj1 = () -> {
            for(int i = 0; i < 10000; i++) {
                counter.increment();
            }
        };

        Runnable obj2 = () -> {
            for(int i = 0; i < 10000; i++) {
                counter.increment();
            }
        };

        Thread thread1 = new Thread(obj1);
        Thread thread2 = new Thread(obj2);

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        System.out.println(counter.count);
    }
}

// Thread States New, Runnable, Running, Blocked, Waiting, Timed Waiting, Terminated
// New: Thread is created but not started yet.
// Runnable: Thread is ready to run but not yet running.
// Running: Thread is executing.
// Blocked: Thread is waiting for a monitor lock to enter a synchronized block/method.
// Waiting: Thread is waiting indefinitely for another thread to perform a particular action.
// Timed Waiting: Thread is waiting for a specified amount of time.
// Terminated: Thread has completed execution.
// Deadlock: A situation where two or more threads are blocked forever, waiting for each other to release resources. This can occur when multiple threads are trying to acquire locks on the same resources in a circular manner.
// Deadlock can be avoided by ensuring that all threads acquire locks in a consistent order, using timeout mechanisms, or by using higher-level concurrency utilities that manage locks more effectively.

