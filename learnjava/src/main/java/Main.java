import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello");
        System.out.print("\033[31m");
        System.out.println("you");
        File f = new File("./src/main/java/main.java");
        try (FileWriter fw = new FileWriter(f, true)) {
            fw.write("\nclass test {\n private int test ;\n} ");
        }
        System.out.println(f.exists());
    }
}