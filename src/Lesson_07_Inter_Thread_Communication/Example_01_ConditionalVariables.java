package Lesson_07_Inter_Thread_Communication;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.StringJoiner;

public class Example_01_ConditionalVariables {
    // this example shows the producer-consumer pattern. This implements a thread-safe queue to facilitate
    // inter-thread communication

    private static final int N = 10;
    private static final String INPUT_FILE = "./out/matrices";
    private static final String OUTPUT_FILE = "./out/matrices_results.txt";

    public static void main(String[] args) throws IOException{
        ThreadSafeQueue threadSafeQueue = new ThreadSafeQueue();

        File inputFile = new File(INPUT_FILE);
        File outputFile = new File(OUTPUT_FILE);

        Producer producer = new Producer(new FileReader(inputFile), threadSafeQueue);
        Consumer consumer = new Consumer(new FileWriter(outputFile), threadSafeQueue);

        consumer.start();
        producer.start();

    }
    // the Producer reads matrices from file and inserts into queue when empty
    private static class Producer extends Thread {
        private ThreadSafeQueue queue;
        private Scanner scanner;

        public Producer(FileReader fileReader, ThreadSafeQueue queue){
            this.queue = queue;
            this.scanner = new Scanner(fileReader);
        }

        @Override
        public void run(){
            while(true){
                float[][] matrix1 = readMatrix();
                float[][] matrix2 = readMatrix();
                if(matrix1 == null || matrix2 == null){
                    queue.terminate();
                    System.out.println("producer is terminating; no more matrices to read");
                    return;
                }
                MatricesPair matricesPair = new MatricesPair(matrix1, matrix2);
                queue.add(matricesPair);
            }
        }

        // read from file
        private float[][] readMatrix(){
            float[][] matrix = new float[N][N];
            for(int r=0; r<N; ++r){
                if(!scanner.hasNext()) return null;
                String[] line = scanner.nextLine().split(",");
                for(int c = 0; c < N; ++c){
                    matrix[r][c] = Float.valueOf(line[c]);
                }
            }
            scanner.nextLine();
            return matrix;
        }
    }

    // the Consumer reads matrices from queue when full, multiplies and writes to file:
    private static class Consumer extends Thread {
        private ThreadSafeQueue queue;
        private FileWriter fileWriter;

        public Consumer(FileWriter fileWriter, ThreadSafeQueue queue) {
            this.queue = queue;
            this.fileWriter = fileWriter;
        }

        @Override
        public void run(){
            while(true){
                MatricesPair matricesPair = queue.remove();
                if(matricesPair == null){
                    System.out.println("no more matrices to read from consumer is terminating");
                    break;
                }
                float[][] result = multiply(matricesPair.matrix1, matricesPair.matrix2);
                try {
                    saveMatrixToFile(fileWriter, result);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            try {
                fileWriter.flush();
                fileWriter.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        private void saveMatrixToFile(FileWriter fileWriter, float[][] matrix) throws IOException {
            for(int r=0; r<N; ++r){
                StringBuilder sb = new StringBuilder();
                StringJoiner sj = new StringJoiner(", ");
                for(int c=0; c<N; ++c){
                    sj.add(String.format("%.2f", matrix[r][c]));
                }

                fileWriter.write(sj.toString());
                fileWriter.write("\n");
            }
            fileWriter.write("\n");
        }
        private float[][] multiply(float[][] a, float[][] b){
            float[][] res = new float[N][N];
            for(int r=0; r<N; ++r){
                for(int c=0; c<N; ++c){
                    for(int k=0; k<N; ++k){
                        float product = a[r][k] * b[k][c];
                        res[r][c] += product;
                    }
                }
            }
            return res;
        }
    }
    private static class ThreadSafeQueue {
        private Queue<MatricesPair> queue = new LinkedList<>();
        private boolean isEmpty = true;
        private boolean isTerminate = false;
        private final int CAPACITY = 5;

        // add: this will be used by Producers
        public synchronized void add(MatricesPair matricesPair){
            // wait until queue has space lower than CAPACITY:
            while(queue.size() == CAPACITY){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.add(matricesPair);
            isEmpty = false;
            notify(); // notify consumers who are waiting for the queue to fill
        }

        // remove: this will be used by Consumers:
        public synchronized MatricesPair remove(){
            // wait until queue is not empty:
            while(isEmpty && !isTerminate){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(queue.size() == 1) isEmpty = true;
            if(queue.size() == 0 && isTerminate) return null;

            System.out.println("queue size = " + queue.size());

            MatricesPair res = queue.remove();
            if(queue.size() == CAPACITY-1){
                notifyAll(); // notify producers
            }
            return res;
        }

        // terminate
        public synchronized void terminate(){
            isTerminate = true;
            notifyAll();
        }
    }
    private static class MatricesPair {
        public float[][] matrix1;
        public float[][] matrix2;

        public MatricesPair(float[][] matrix1, float[][] matrix2) {
            this.matrix1 = matrix1;
            this.matrix2 = matrix2;
        }
    }
}
