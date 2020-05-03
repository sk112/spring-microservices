package com.auth.springauthservice.config;

import javax.sql.DataSource;

import com.auth.springauthservice.ForwardAuthenticationSuccessHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer.AuthorizationEndpointConfig;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    DataSource datasource;

    @Autowired
    JwtFilterRequest jwtFilterRequest;

    @Autowired
    JdbcUserDetailsManager jdbcUserDetailsManager;

    @Autowired
    private ForwardAuthenticationSuccessHandler successHandler;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
        .dataSource(datasource);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(jwtFilterRequest, UsernamePasswordAuthenticationFilter.class)
            .csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers("/register*").permitAll()
            .antMatchers("/auth*").permitAll()
            .antMatchers("/login**").permitAll()
            .anyRequest().authenticated()
            .and()
            .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .formLogin().successHandler(successHandler)
            .loginPage("/auth")
            .and()
            .logout()
            .permitAll();

        http.headers().frameOptions().sameOrigin();

    }

    @Bean
    UserDetailsManager usersDetailsManager(DataSource dataSource){
        
        // UserDetails user = User.builder()
        //                        .username("admin")
        //                        .password(passwordEncoder().encode("admin"))
        //                        .roles("USER", "ADMIN")
        //                        .build();

        // jdbcUserDetailsManager.createUser(user);
        return jdbcUserDetailsManager;
    }

    @Bean
    JdbcUserDetailsManager getjdbcUserManagetDetails(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}