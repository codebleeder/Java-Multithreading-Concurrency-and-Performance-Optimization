package Lesson_01_Thread_Creation;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Example_04_Thread_Inhertance {
    public static final int MAX_PASSWORD = 999;

    public static void main(String[] args) {
        Random random = new Random();
        Vault vault = new Vault(random.nextInt(MAX_PASSWORD));
        List<Thread> threads = new ArrayList<>();
        threads.add(new AscendingHackerThread(vault));
        threads.add(new DescendingHackerThread(vault));
        threads.add(new PoliceThread());

        for(Thread thread: threads){
            thread.start();
        }
    }
    private static class Vault {
        private Integer password;
        public Vault(Integer password){
            this.password = password;
        }
        public boolean isCorrectPassword(Integer guess){
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this.password == guess;
        }
    }
    private static abstract class HackerThread extends Thread {
        protected Vault vault;
        public HackerThread(Vault vault){
            this.vault = vault;
            this.setName(this.getClass().getSimpleName());
            this.setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public void start(){
            System.out.println("starting thread: " + this.getName());
            super.start();
        }
    }

    private static class AscendingHackerThread extends HackerThread {

        public AscendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run(){
            for(int i=0; i<MAX_PASSWORD; ++i){
                if(vault.isCorrectPassword(i)){
                    System.out.println(this.getName() + " guessed the password: " + i);
                    System.exit(0);
                }
            }
        }
    }

    private static class DescendingHackerThread extends HackerThread {

        public DescendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run(){
            for(int i=MAX_PASSWORD; i>0; --i){
                if(vault.isCorrectPassword(i)){
                    System.out.println(this.getName() + " guessed the password: " + i);
                    System.exit(0);
                }
            }
        }
    }

    private static class PoliceThread extends Thread {
        @Override
        public void run(){
            for(int i=10; i>0; i--){
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e){

                }
                System.out.println("police countdown: " + i);
            }
            System.out.println("game over for you hackers");
            System.exit(0);
        }
    }
}
