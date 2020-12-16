package Lesson_05_Concurrency_challenges_and_solutions;

import java.util.Random;

public class Example_04_Deadlock {
    public static void main(String[] args) {
        Intersection intersection = new Intersection();
        Thread threadA = new Thread(new TrainA(intersection));
        Thread threadB = new Thread(new TrainB(intersection));
        threadA.start();
        threadB.start();
    }
    private static class Intersection {
        private Object trackA = new Object();
        private Object trackB = new Object();
        // these two methods will wait on each other and create a deadlock
        // to fix this, the sequence of the locks should be the same
        public void takeRoadAtoB(){
            synchronized (trackA){
                System.out.println("taking track A");
                synchronized (trackB){
                    System.out.println("taking track B");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        public void takeRoadBtoA(){
            // fixed the deadlock issue by keeping one sequence of locks throughout the code:
            synchronized (trackA){
              //synchronized (trackB){
                System.out.println("taking track B");
                synchronized (trackB){
                 //synchronized (trackA){
                    System.out.println("taking track A");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private static class TrainA implements Runnable {
        private Intersection intersection;
        private Random random = new Random();
        public TrainA(Intersection intersection){
            this.intersection = intersection;
        }
        @Override
        public void run() {
            while(true){
                long sleepTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                intersection.takeRoadAtoB();
            }

        }
    }
    private static class TrainB implements Runnable {
        private Intersection intersection;
        private Random random = new Random();

        public TrainB(Intersection intersection){
            this.intersection = intersection;
        }
        @Override
        public void run() {
            while(true){
                long sleepTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                intersection.takeRoadBtoA();
            }

        }
    }
}
