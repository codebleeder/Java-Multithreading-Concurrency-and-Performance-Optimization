package Lesson_02_Thread_Coordination;

public class Example_01_Thread_Interruption {
    public static void main(String[] args) {
        Thread thread = new Thread(new BlockingTask());
        thread.start();
        thread.interrupt();
    }
    private static class BlockingTask implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(500000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                System.out.println("exiting blocking thread");
            }

        }
    }
}
