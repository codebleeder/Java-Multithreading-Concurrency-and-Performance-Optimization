package Lesson_06_Advanced_Locking;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Example_02_ReentranReadWriteLock {
    public static final int MAX_PRICE = 1000;
    public static void main(String[] args) throws InterruptedException {
        InventoryDatabase inventoryDatabase = new InventoryDatabase();
        Random random = new Random();
        // fill the db:
        for(int i=0; i<100000; ++i){
            inventoryDatabase.addItem(random.nextInt(MAX_PRICE));
        }
        Thread writer = new Thread(()->{
            while (true){
                inventoryDatabase.addItem(random.nextInt(MAX_PRICE));
                inventoryDatabase.removeItem(random.nextInt(MAX_PRICE));

                // to simulate writing doesn't happen more than reading:
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        writer.setDaemon(true);
        writer.start();

        // create 7 reader threads:
        int numReaderThreads = 7;
        List<Thread> readers = new ArrayList<>();
        for(int i=0; i<numReaderThreads; ++i){
            Thread readerThread = new Thread(()->{
                for(int j=0; j<100000; ++j){
                    int upper = random.nextInt(MAX_PRICE);
                    int lower = upper > 0 ? random.nextInt(upper) : 0;
                    inventoryDatabase.getNumberOfItemsInPriceRange(lower, upper);
                }
            });
            readerThread.setDaemon(true);
            readers.add(readerThread);
        }

        // start the writer and reader threads:
        long start = System.currentTimeMillis();

        for(Thread reader: readers){
            reader.start();
        }
        for(Thread reader: readers){
            reader.join();
        }
        long end = System.currentTimeMillis();
        System.out.println("reading took: " + (end-start) + " ms");
    }
    private static class InventoryDatabase {
        private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();
        private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        private Lock readLock = reentrantReadWriteLock.readLock();
        private Lock writeLock = reentrantReadWriteLock.writeLock();

        public InventoryDatabase(){}

        public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound) {
            readLock.lock();
            try{
                Integer fromKey = priceToCountMap.ceilingKey(lowerBound);

                Integer toKey = priceToCountMap.floorKey(upperBound);

                if (fromKey == null || toKey == null) {
                    return 0;
                }

                NavigableMap<Integer, Integer> rangeOfPrices = priceToCountMap.subMap(fromKey, true, toKey, true);

                int sum = 0;
                for (int numberOfItemsForPrice : rangeOfPrices.values()) {
                    sum += numberOfItemsForPrice;
                }

                return sum;
            }
            finally {
                readLock.unlock();
            }
        }

        public void addItem(int price){
            writeLock.lock();
            try{
                Integer numOfItems = priceToCountMap.get(price);
                if(numOfItems == null){
                    priceToCountMap.put(price, 1);
                }
                else {
                    priceToCountMap.put(price, numOfItems+1);
                }
            }
            finally {
                writeLock.unlock();
            }
        }

        public void removeItem(int price){
            writeLock.lock();
            try{
                Integer numOfItems = priceToCountMap.get(price);
                if(numOfItems == null || numOfItems == 1){
                    priceToCountMap.remove(price);
                }
                else {
                    priceToCountMap.put(price, numOfItems-1);
                }
            }
            finally {
                writeLock.unlock();
            }
        }
    }
}
