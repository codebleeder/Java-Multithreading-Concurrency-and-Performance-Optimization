package Lesson_01_Thread_Creation;

import java.util.ArrayList;
import java.util.List;

public class Example_05_MultiExecutor_Assignment {
    public static class MultiExecutor {

        // Add any necessary member variables here
        List<Runnable> tasks;
        /*
         * @param tasks to executed concurrently
         */
        public MultiExecutor(List<Runnable> tasks) {
            // Complete your code here
            this.tasks = tasks;
        }

        /**
         * Starts and executes all the tasks concurrently
         */
        public void executeAll() {
            // complete your code here
            for(Runnable task: tasks){
                Thread t = new Thread(task);
                t.start();
            }
        }
    }
}




