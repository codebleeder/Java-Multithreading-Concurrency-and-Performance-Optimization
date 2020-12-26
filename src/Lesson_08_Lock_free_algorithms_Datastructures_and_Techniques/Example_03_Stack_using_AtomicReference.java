package Lesson_08_Lock_free_algorithms_Datastructures_and_Techniques;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class Example_03_Stack_using_AtomicReference {
    public static void main(String[] args) {
        //StandardStack<Integer> stack = new StandardStack<>();
        LockFreeStack<Integer> stack = new LockFreeStack<>();
        Random random = new Random();

        // push random values into stack:
        for(int i=0; i<100000; ++i){
            stack.push(random.nextInt());
        }

        int numPushThreads = 2;
        int numPopThreads = 2;
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<numPushThreads; ++i){
            Thread thread = new Thread(()->{
                while (true){
                    stack.push(random.nextInt());
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }

        for(int i=0; i<numPopThreads; ++i){
            Thread thread = new Thread(()->{
                while (true){
                    stack.pop();
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }

        // run the threads for 10 seconds:
        for(Thread thread: threads){
            thread.start();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("number of operations completed: %,d", stack.getCounter()));
    }
    private static class StackNode<T> {
        public T value;
        public StackNode<T> next;

        public StackNode(T value, StackNode<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    private static class LockFreeStack<T> {
        private AtomicReference<StackNode<T>> head = new AtomicReference<>();
        private AtomicInteger counter = new AtomicInteger(0); // counts operations

        // push
        public void push(T value){
            StackNode<T> newHead = new StackNode<>(value, null);
            while(true){
                StackNode<T> currentHead = head.get();
                newHead.next = currentHead;
                if(head.compareAndSet(currentHead, newHead)){
                   break;
                }
                else {
                    LockSupport.parkNanos(1);
                }
            }
            counter.incrementAndGet();
        }

        // pop
        public T pop(){
            StackNode<T> currentHead = head.get();
            StackNode<T> newHeadNode;
            while(currentHead != null){
                newHeadNode = currentHead.next;
                if(head.compareAndSet(currentHead, newHeadNode)){
                    break;
                }
                else {
                    LockSupport.parkNanos(1);
                    currentHead = head.get();
                }
            }
            counter.incrementAndGet();
            return currentHead != null ? currentHead.value : null;
        }

        // getCounter
        public int getCounter(){
            return counter.get();
        }
    }

    private static class StandardStack<T> {
        private StackNode<T> head;
        private int counter = 0;

        // push
        public synchronized void push(T value){
            StackNode<T> newHead = new StackNode<>(value, null);
            newHead.next = head;
            head = newHead;
            ++counter;
        }

        // pop
        public synchronized T pop(){
            if(head == null) {
                ++counter;
                return null;
            }
            T res = head.value;
            head = head.next;
            ++counter;
            return res;
        }

        // getCounter
        public int getCounter(){
            return counter;
        }
    }
}
