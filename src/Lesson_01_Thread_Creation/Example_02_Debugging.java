package Lesson_01_Thread_Creation;

public class Example_02_Debugging {
    public static void main(String[] args) {
        Thread thread = new Thread(()->{
            throw new RuntimeException("intentional exception");
        });
        thread.setName("misbehaving thread");

        // set exception handler:
        Thread.UncaughtExceptionHandler exceptionHandler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("a critical error happened in thread: " + t.getName()
                        + "; error is: " + e.getMessage());
            }
        };
        thread.setUncaughtExceptionHandler(exceptionHandler);

        thread.start();
    }
}
