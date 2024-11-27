package sa.m.ntd.calculator.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.ObservationTextPublisher;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import sa.m.ntd.calculator.controller.JwtRequestFilter;
import sa.m.ntd.calculator.controller.JwtUtil;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class Config {

    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails john = User.builder()
                .username("john@gmail.com")
                .password(passwordEncoder().encode("john0000"))
                .roles("USER")
                .build();

        UserDetails sam = User.builder()
                .username("mary@gmail.com")
                .password(passwordEncoder().encode("mary0000"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(john, sam);
    }

    @Bean
    @Profile("tracing")
    ObservationRegistryCustomizer<ObservationRegistry> addTextHandler() {
        return (registry) -> registry.observationConfig().observationHandler(new ObservationTextPublisher());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUtil getJwtUtil() { return new JwtUtil(); }

    @Bean
    public JwtRequestFilter getJwtRequestFilter(JwtUtil jwtUtil) { return  new JwtRequestFilter(jwtUtil); }

}
