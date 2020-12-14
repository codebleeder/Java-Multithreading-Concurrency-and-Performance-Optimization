package Lesson_04_Data_sharing_between_threads;

public class Example_01_Stack_and_Heap_memory_regions {
    public static void main(String[] args) {
        int x = 1;
        int y = 2;
        int result = sum(x, y);
    }
    public static int sum(int x, int y){
        int s = x + y;
        return s;
    }
}
