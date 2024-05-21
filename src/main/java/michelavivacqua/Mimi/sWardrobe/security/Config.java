//package michelavivacqua.Mimi.sWardrobe.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.security.config.Customizer;
//
//import java.util.Arrays;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class Config {
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.formLogin(http -> http.disable()); // disabilito il form di login in frontend predefinito (avremo React per quello)
//        httpSecurity.csrf(http -> http.disable()); // Non voglio la protezione da CSRF (per l'applicazione media non è necessaria e complicherebbe tutta la faccenda, anche lato FE)
//        httpSecurity.sessionManagement(http -> http.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Non voglio le sessioni (perché utilizzeremo la token based authentication con JWT)
//        httpSecurity.cors(Customizer.withDefaults());
//        // Possiamo aggiungere dei filtri custom
//
//        // Possiamo decidere se debba essere necessaria o meno un'autenticazione per accedere agli endpoint
//        httpSecurity.authorizeHttpRequests(http -> http.requestMatchers("/**").permitAll());
//
//        // Aggiungere/Rimuovere determinate regole di protezione per gli endpoint
//        // Possiamo decidere se debba essere necessaria o meno un'autenticazione per accedere agli endpoint
//        httpSecurity.authorizeHttpRequests(http -> http.requestMatchers("/**").permitAll());
//
//        return httpSecurity.build();
//    }
//
//    @Bean
//    PasswordEncoder getBCrypt(){
//        return new BCryptPasswordEncoder(11);}
//
//    @Bean
//    CorsConfigurationSource corsConfigurationSource(){
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
//        config.setAllowedMethods(Arrays.asList("*"));
//        config.setAllowedHeaders(Arrays.asList("*"));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        // Registro la configurazione CORS appena fatta a livello globale su tutti gli endpoint del mio server
//
//        return source;
//
//    }
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic(); // O qualsiasi altro metodo di autenticazione che stai usando
//    }
//}

package michelavivacqua.Mimi.sWardrobe.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class Config {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configura CORS
                .csrf(csrf -> csrf.disable()) // Disabilita CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sessione senza stato per JWT
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll() // Permette l'accesso a Swagger senza autenticazione
                        .requestMatchers("/**").permitAll() // Permette tutte le altre richieste
                        .anyRequest().authenticated()) // Tutte le altre richieste richiedono autenticazione
                .httpBasic(Customizer.withDefaults()); // Usa l'autenticazione di base HTTP

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
