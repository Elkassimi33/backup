package java.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadExecuterDmo {

    public static void main(String[]args) {
        try (ExecutorService service = Executors.newSingleThreadExecutor()) {

            for (int i = 0; i < 5; i++) {
                service.execute(new Task(i));
            }
        }
    }

}

