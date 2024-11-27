package sa.m.ntd.calculator.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import sa.m.ntd.calculator.model.CalculatorUser;

import java.util.List;

@Configuration
public class CalculatorUserData {

    @Bean
    public List<CalculatorUser> userData(PasswordEncoder encoder) {
        return  List.of(
                new CalculatorUser("john@gmail.com", encoder.encode("john0000"), "Y"),
                new CalculatorUser("mary@gmail.com", encoder.encode("mary0000"), "Y")
        );
    }

}
