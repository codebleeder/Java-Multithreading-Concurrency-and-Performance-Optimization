package Lesson_02_Thread_Coordination;

import java.math.BigInteger;

public class Example_04_ComplexCalculation_Assignment {

    public static void main(String[] args) {
        ComplexCalculation complexCalculation = new ComplexCalculation();
        BigInteger res = complexCalculation.calculateResult(
                new BigInteger("23"), new BigInteger("3"),
                new BigInteger("24"), new BigInteger("4")
        );
        System.out.println(res);
    }

    public static class ComplexCalculation {
        public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) {
            BigInteger result;
            /*
                Calculate result = ( base1 ^ power1 ) + (base2 ^ power2).
                Where each calculation in (..) is calculated on a different thread
            */
            PowerCalculatingThread t1 = new PowerCalculatingThread(base1, power1);
            PowerCalculatingThread t2 = new PowerCalculatingThread(base2, power2);
            t1.start();
            t2.start();
            try {
                t1.join(2000);
                t2.join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = t1.getResult().add(t2.getResult());
            return result;
        }

        private static class PowerCalculatingThread extends Thread {
            public BigInteger result = BigInteger.ONE;
            private BigInteger base;
            private BigInteger power;

            public PowerCalculatingThread(BigInteger base, BigInteger power) {
                this.base = base;
                this.power = power;
            }
            public BigInteger getResult() {
                return result;
            }
            @Override
            public void run() {
                // Implement the calculation of result = base ^ power
                result = pow(base, power);
            }

            private BigInteger pow(BigInteger base, BigInteger power) {
                BigInteger res = BigInteger.ONE;
                for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i=i.add(BigInteger.ONE)) {
                    // check here if interrupt has been raised:
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("prematurely interrupted computation");
                        return BigInteger.ZERO;
                    }
                    res = res.multiply(base);
                }
                return res;
            }


        }

    }}

