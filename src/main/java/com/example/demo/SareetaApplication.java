package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@EnableJpaRepositories("com.example.demo.model.persistence.repositories")
@EntityScan("com.example.demo.model.persistence")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SareetaApplication {


    private static final String ADMIN = "admin";
    private static final String PASSWORD = "mypassword";

    public static void main(String[] args) {

        SpringApplication.run(SareetaApplication.class, args);

    }

    @Bean
    CommandLineRunner createDefaultUser(UserRepository userRepository) {
        return args -> {
            User user = new User();
            user.setUsername(ADMIN);
            user.setPassword(bCryptPasswordEncoder().encode(PASSWORD));
            user.setCart(new Cart());
            userRepository.save(user);
        };
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public TcpInput splunkService() throws IOException {
//        HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
//
//        // Create a map of arguments and add login parameters that you get from splunk
//        ServiceArgs loginArgs = new ServiceArgs();
//        loginArgs.setUsername("username");
//        loginArgs.setPassword("password");
//        loginArgs.setHost("127.0.0.1");
//        loginArgs.setPort(8089);
//
//        // Create a Service instance and log in with the argument map
//        Service service = Service.connect(loginArgs);
//
//        IndexCollectionArgs indexcollArgs = new IndexCollectionArgs();
//        indexcollArgs.setSortKey("totalEventCount");
//        indexcollArgs.setSortDirection(IndexCollectionArgs.SortDirection.DESC);
//        IndexCollection myIndexes = service.getIndexes(indexcollArgs);
//
//        for (Index entity : myIndexes.values()) {
//            System.out.println("  " + entity.getName() + " (events: "
//                    + entity.getTotalEventCount() + ")");
//        }
//
//        for (Application app : service.getApplications().values()) {
//            System.out.println(app.getName());
//        }
//        // Get the collection of data inputs
//        InputCollection myInputs = service.getInputs();
//
//        // Iterate and list the collection of inputs
//        System.out.println("There are " + myInputs.size() + " data inputs:\n");
//        for (Input entity : myInputs.values()) {
//            System.out.println("  " + entity.getName() + " (" + entity.getKind() + ")");
//        }
//
//        // Retrieve the input
//        return  (TcpInput)service.getInputs().get("3000");
//    }
}
