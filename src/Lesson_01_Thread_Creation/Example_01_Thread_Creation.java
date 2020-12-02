package Lesson_01_Thread_Creation;

public class Example_01_Thread_Creation {
    public static void main(String[] args) throws InterruptedException {
        // create thread:
        // Thread constructor takes a Runnable object:
        Thread thread1 = new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                    }
                }
        );

        // simpler Java 8 uses functional interface - consumer for run method:
        Thread thread2 = new Thread(()->{
            System.out.println("we're in thread: " + Thread.currentThread().getName());
            System.out.println("current thread priority is: " + Thread.currentThread().getPriority());
        });

        // set thread name:
        thread2.setName("new worker thread");

        // set priority: MAX_PRIORITY/MIN_PRIORITY/NORMAL_PRIORITY/value b/w 1-10
        thread2.setPriority(Thread.MAX_PRIORITY);

        // get current thread name before starting thread2
        System.out.println("we're in thread: " + Thread.currentThread().getName() + " before starting a new thread");

        // start/execute thread:
        thread2.start();

        System.out.println("we're in thread: " + Thread.currentThread().getName() + " after starting a new thread");

        Thread.sleep(10000);
    }
}
