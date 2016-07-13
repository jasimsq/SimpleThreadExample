import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private Object result;
    private static long timer;

    public static void main(String[] args) {
        System.out.println("Start searching...");
        timer = System.currentTimeMillis();
        Main main = new Main();
        main.search();
    }

    // post the result to the main thread (the application)
    public void post(Object o){
        // to avoid race condition / avoiding overriding this could help you for prioritisation (this guarantees only the 1st one to find can return the object.
        if(result==null){
            System.out.println("Got a post...");
            result = o;
            Thread.currentThread().interrupt();
        }
    }

    // this is the main thing (house keeping the threads)
    private void search() {
        // keep this dynamic as you dont know how many methods will be implemented
        List<Thread> methods = new ArrayList<>();
        methods.add(new MethodA());
        methods.add(new MethodB());
        methods.add(new MethodC());

        // start each thread in the list
        for (Thread method : methods){
            method.start();
        }

        // keep the main thread alive until one of the methods (threads) find the pattern
        while (result==null){
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }

        System.out.println("Search is done object type: " + result.getClass().getName());
        System.out.println("Search done in: " + (System.currentTimeMillis() - timer) + " millisec");

        // It is important to stop the threads, the best practice is to interrupt them
        for (Thread method : methods){
            method.interrupt();
        }
    }

    // Thread 1
    class MethodA extends Thread {
        // each thread needs to implement run method, this is like a main method for the thread
        @Override
        public void run() {
            // we will assume that this is how long the method takes to find your pattern
            try {
                Random r = new Random();
                Thread.sleep(r.nextInt(10000));
                post(this);
            } catch (InterruptedException e) {
                System.out.println("Thread 1 Interrupted");
            }

        }
    }

    // Thread 2
    class MethodB extends Thread {

        @Override
        public void run() {
            // we will assume that this is how long the method takes to find your pattern
            try {
                Random r = new Random();
                Thread.sleep(r.nextInt(10000));
                post(this);
            } catch (InterruptedException e) {
                System.out.println("Thread 2 Interrupted");
            }
        }
    }

    // Thread 3
    class MethodC extends Thread {
        @Override
        public void run() {
            // we will assume that this is how long the method takes to find your pattern
            try {
                Random r = new Random();
                Thread.sleep(r.nextInt(10000));
                post(this);
            } catch (InterruptedException e) {
                System.out.println("Thread 3 Interrupted");
            }
        }
    }
}
