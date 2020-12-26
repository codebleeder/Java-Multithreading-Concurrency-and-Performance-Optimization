package Lesson_08_Lock_free_algorithms_Datastructures_and_Techniques;

import java.util.concurrent.atomic.AtomicReference;

public class Example_02_AtomicReference_CompareSet {
    public static void main(String[] args) {
        String oldName = "oldname";
        String newName = "newName";
        AtomicReference<String> atomicReference = new AtomicReference<>(oldName);
        // following line simulates a multi-threaded situation, where another thread modifies the
        // variable before your thread modifies:
        atomicReference.set("unexpected value");
        if(atomicReference.compareAndSet(oldName, newName)){
            System.out.println("value successfully changed: " + atomicReference.get());
        }
        else {
            System.out.println("nothing changed: " + atomicReference.get());
        }
    }
}
