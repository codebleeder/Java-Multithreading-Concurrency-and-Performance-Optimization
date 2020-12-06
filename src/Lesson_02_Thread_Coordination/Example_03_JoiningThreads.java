package Lesson_02_Thread_Coordination;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Example_03_JoiningThreads {
    public static void main(String[] args) throws InterruptedException {
        List<Long> inputNumbers = Arrays.asList(1000000000L, 3435L, 2324L, 4656L, 23L, 5556L);
        List<FactorialThread> threads = new ArrayList<>();
        for(long input: inputNumbers){
            threads.add(new FactorialThread(input));
        }
        for(Thread thread: threads){
            thread.start();
        }
        // wait for the threads to finish by using join:
        for(Thread thread: threads){
            // give a wait limit. if one computation is taking too long, then it will hold up other threads
            thread.join(2000);
        }

        for(int i=0; i<inputNumbers.size(); ++i){
            FactorialThread thread = threads.get(i);
            if(thread.isFinished()){
                System.out.println("factorial of " + inputNumbers.get(i) + " is: " + thread.getResult());
            }
        }
    }
    private static class FactorialThread extends Thread {
        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished;

        public FactorialThread(long inputNumber){
            this.inputNumber = inputNumber;
        }

        @Override
        public void run(){
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        private BigInteger factorial(long n){
            BigInteger temp = BigInteger.ONE;
            for(long i=n; i>0; --i){
                temp = temp.multiply(new BigInteger(Long.toString(i)));
            }
            return temp;
        }

        public boolean isFinished(){
            return isFinished;
        }

        public BigInteger getResult(){
            return result;
        }
    }
}
