package Lesson_02_Thread_Coordination;

import java.math.BigInteger;

public class Example_02_Thread_Interruption2 {
    public static void main(String[] args) {
        Thread thread = new Thread(new LongComputationTask(new BigInteger("200"), new BigInteger("400")));
        // if you want the program to ignore the task and exit, you can make it a daemon:
        // thread.setDaemon(true);
        thread.start();
        thread.interrupt();

    }
    private static class LongComputationTask implements Runnable {
        private BigInteger base;
        private BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power){
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base+"^"+power+" = " + pow(base, power));
        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger res = BigInteger.ONE;
            for(BigInteger i=BigInteger.ZERO; i.compareTo(power) != 0; i.add(BigInteger.ONE)){
                // check here if interrupt has been raised:
                if(Thread.currentThread().isInterrupted()){
                    System.out.println("prematurely interrupted computation");
                    return BigInteger.ZERO;
                }
                res = res.multiply(base);
            }
            return res;
        }
    }

}

