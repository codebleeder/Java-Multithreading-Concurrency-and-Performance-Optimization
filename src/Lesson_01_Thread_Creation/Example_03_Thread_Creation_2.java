package Lesson_01_Thread_Creation;

public class Example_03_Thread_Creation_2 {
    public static void main(String[] args) {
        Thread thread = new ExampleThread();
        thread.setName("example thread");
        thread.run();
    }
    // define thread in Thread class:
    public static class ExampleThread extends Thread {
        @Override
        public void run(){
            System.out.println("we're in thread: " + this.getName());
        }
    }
}
