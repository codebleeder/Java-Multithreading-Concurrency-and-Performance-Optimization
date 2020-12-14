package Lesson_05_Concurrency_challenges_and_solutions;


import java.util.Random;

public class Example_02_MetricsAggregation {
    public static void main(String[] args) {
        Metrics metrics = new Metrics();
        BusinessLogic businessLogic1 = new BusinessLogic(metrics);
        BusinessLogic businessLogic2 = new BusinessLogic(metrics);
        MetricsPrinter printer = new MetricsPrinter(metrics);
        printer.start();
        businessLogic1.start();
        businessLogic2.start();
    }
    public static class MetricsPrinter extends Thread{
        Metrics metrics;
        public MetricsPrinter(Metrics metrics){
            this.metrics = metrics;
        }
        @Override
        public void run(){
            while(true){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                double currentAverage = metrics.getAverage();
                System.out.println("current average is: " + currentAverage);
            }
        }
    }
    public static class BusinessLogic extends Thread {
        private Metrics metrics;
        private Random random = new Random();

        public BusinessLogic(Metrics metrics){
            this.metrics = metrics;
        }
        @Override
        public void run(){
            long start = System.currentTimeMillis();
            try {
                Thread.sleep(random.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            metrics.addSample(end-start);
        }
    }
    public static class Metrics {
        private long count;
        // since double is non-atomic, we need to make it atomic by using "volatile":
        private volatile double average;
        // since the following is non-atomic, it needs to be locked:
        public synchronized void addSample(long sample){
            double currentSum = average * count;
            count++;
            average = (currentSum + sample)/count;
        }
        public double getAverage(){
            return average;
        }
    }
}
