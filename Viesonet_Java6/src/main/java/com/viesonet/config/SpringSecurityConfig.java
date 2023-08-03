package com.viesonet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.viesonet.service.AccountsService;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    @Configuration
    public static class LoginFormSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        AccountsService accountService;

        // /*--Mã hóa mật khẩu--*/
        @Bean
        public BCryptPasswordEncoder getPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(accountService);
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        // Phân quyền sử dụng
        /*--Phân quyền sử dụng và hình thức đăng nhập--*/
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // CSRF, CORS
            http.csrf().disable();

            // Phân quyền sử dụng
            http.authorizeRequests()
                    .antMatchers("/auth/login").permitAll()
                     .antMatchers("/api/account").authenticated()
                     .antMatchers("/api/cart/**").hasRole("USER")
                      // 401-UNAUTHORIZED when anonymous user tries to access protected URLs
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

            
            // Điều khiển lỗi truy cập không đúng vai trò
            http.exceptionHandling()
                    .accessDeniedPage("/auth/access/denied"); // [/error]

            // Giao diện đăng nhập
            http.formLogin()
                    .loginPage("/auth/login/form")
                    .loginProcessingUrl("/auth/login") // [/login]
                    .defaultSuccessUrl("/auth/login/success")
                    .failureUrl("/auth/login/error")
                    .usernameParameter("username") // [username]
                    .passwordParameter("password"); // [password]
            http.rememberMe()
                    .rememberMeParameter("remember"); // [remember-me]

            // Đăng xuất
            http.logout()
                    .logoutUrl("/auth/logoff") // [/logout]
                    .logoutSuccessUrl("/auth/logoff/success");
        }
    }
}
