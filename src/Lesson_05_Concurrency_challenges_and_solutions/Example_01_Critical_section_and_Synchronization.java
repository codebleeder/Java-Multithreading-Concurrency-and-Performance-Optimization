package Lesson_05_Concurrency_challenges_and_solutions;

public class Example_01_Critical_section_and_Synchronization {
    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();
        IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

        incrementingThread.start();
        decrementingThread.start();

        incrementingThread.join();
        decrementingThread.join();

        System.out.println("We currently have " + inventoryCounter.getItems() + " items");
    }

    public static class DecrementingThread extends Thread {

        private InventoryCounter inventoryCounter;

        public DecrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.decrement();
            }
        }
    }

    public static class IncrementingThread extends Thread {

        private InventoryCounter inventoryCounter;

        public IncrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.increment();
            }
        }
    }

    // a better approach is to use lock:
    // with this two threads can use the class simultaneously and the items object is also synchronized
    // This will minimize the critical section of the code.
    private static class InventoryCounter {
        private int items = 0;

        Object lock = new Object();

        public void increment() {
            synchronized (this.lock) {
                items++;
            }
        }

        public void decrement() {
            synchronized (this.lock) {
                items--;
            }
        }

        public int getItems() {
            synchronized (this.lock) {
                return items;
            }
        }
    }

    // lock the class by using "synchronized" keyword
    // this will lock up the entire object and only one thread can use this at a time
    private static class InventoryCounter2 {
        private int items = 0;

        Object lock = new Object();

        public synchronized void increment() {
                items++;
        }

        public synchronized void decrement() {
                items--;
        }

        public synchronized int getItems() {
                return items;
        }
    }
}
