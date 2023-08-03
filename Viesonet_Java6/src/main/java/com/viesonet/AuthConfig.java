package com.viesonet;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import com.viesonet.component.CustomAuthenticationEntryPoint;
import com.viesonet.entity.Accounts;
import com.viesonet.handler.CustomAuthenticationSuccessHandler;
import com.viesonet.service.AccountsService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthConfig {

    @Autowired
    private AccountsService accountsService;
    
    @Bean
    public CustomAuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }
    
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationManager();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(authenticationProvider()));
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
                Accounts account = accountsService.getAccountById(userId);
                System.out.println("User ID: " + account.getUserId());
                System.out.println("Username: " + account.getPhoneNumber());
                System.out.println("Password: " + account.getPassword());
                System.out.println("UserId: " + account.getUserId());
                // ...

                // Mã hóa mật khẩu trước khi trả về UserDetails
                String hashedPassword = passwordEncoder().encode(account.getPassword());
System.out.println("hashedPassword: "+hashedPassword);
                return User.builder()
                        .username(account.getPhoneNumber())
                        .password(hashedPassword)
                        .roles(String.valueOf(account.getRole().getRoleId()))
                        .build();
            }
        };
        
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf().disable()
            .authorizeRequests()
            .requestMatchers("/login", "/register","/dangky","/login-fail", "/images/**", "/js/**", "/css/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/auth/login")
            .defaultSuccessUrl("/index", false) // Chuyển hướng đến URL "/" sau khi đăng nhập thành công
            .usernameParameter("username") // [username]
            .passwordParameter("password") // [password]
            .failureUrl("/login-fail")
            .and()
            .logout()
            .logoutSuccessUrl("/login")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .and()
            .exceptionHandling() // Xử lý ngoại lệ khi người dùng chưa đăng nhập
            .authenticationEntryPoint(authenticationEntryPoint())
            .and()
            .build();
    }
    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    
 // Phương thức riêng để lấy account từ Authentication
    public Accounts getLoggedInAccount(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.User) {
                String userId = ((org.springframework.security.core.userdetails.User) principal).getUsername();
                return accountsService.getAccountById(userId);
            }
        }
        return null;
    }

}
