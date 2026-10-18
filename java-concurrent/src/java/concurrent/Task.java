package java.concurrent;

public record Task(int id) implements Runnable {
    @Override
    public void run() {
        System.out.println("Task with id: " + id + " being executed by: " + Thread.currentThread().getName());
    }
}