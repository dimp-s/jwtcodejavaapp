package dev.dipesh.jwtcodejavaapp;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import dev.dipesh.jwtcodejavaapp.jwt.JwtTokenFilter;
import dev.dipesh.jwtcodejavaapp.productapi.UserRepository;

@Deprecated
@Configuration
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter{ 
        @Autowired
        private UserRepository userRepo;
        @Autowired
        private JwtTokenFilter jwtTokenFilter;


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            //exception handling for http errors
            http.exceptionHandling().authenticationEntryPoint(
            (request, response, error) -> {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, error.getMessage());
            }
            );

            //allow logged in users to access secured apis
            http.authorizeRequests().antMatchers("/auth/login").permitAll()
            .anyRequest().authenticated();

            http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        }
     
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(
                username -> userRepo.findByEmail(username)
                    .orElseThrow(
                        () -> new UsernameNotFoundException("User " + username + " not found.")));
        }
     
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }  
     
        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
         
        
    }


// @Configuration
// public class ApplicationSecurityConfig{ 
//     @Autowired
//     private UserRepository userRepository;

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http.csrf().disable();
//         http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//         http.authorizeRequests().anyRequest().permitAll();
//         return http.build();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder(){
//         return new BCryptPasswordEncoder();
//     }

//     //authentication manager builder
 
//     @Bean
//     AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
//         return builder.userDetailsService(username -> userRepository.findByEmail(username)
//         .orElseThrow(()-> new UsernameNotFoundException("User" + username + "not found!"))
//         ).passwordEncoder(passwordEncoder()).and().build();
//     }
// }

