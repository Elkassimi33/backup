import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;

public class test implements CommandLineRunner {

    @Autowired
    private Environment env;
    @Override
    public void run(String... args) throws Exception {

        System.out.println(env.getProperty("spring.jpa.properties.hibernate.default_schema"));
    }
}
