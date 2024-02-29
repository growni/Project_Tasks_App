package com.ipsos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final AuthProvider authProvider;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder, AuthProvider authProvider) {
        this.passwordEncoder = passwordEncoder;
        this.authProvider = authProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/dashboard/addProject").hasAnyRole("ADMIN", "LEADER")
                .requestMatchers("/project/editProject").hasAnyRole("ADMIN", "LEADER")
                .requestMatchers("/project/assignUser").hasAnyRole("ADMIN", "LEADER")
                .requestMatchers("/project/**").hasAnyRole("ADMIN", "LEADER", "DEVELOPER")
                .requestMatchers("/dashboard/**").hasAnyRole("ADMIN", "LEADER", "DEVELOPER")
                .requestMatchers("/profile/**").hasAnyRole("ADMIN", "LEADER", "DEVELOPER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/teams").hasAnyRole("ADMIN", "LEADER", "DEVELOPER")
                .requestMatchers("/teams/add").hasRole("ADMIN")
                .requestMatchers("/team/join").hasAnyRole("ADMIN", "LEADER", "DEVELOPER")
                .requestMatchers("/team/approveRequest").hasAnyRole("ADMIN", "LEADER")
                .requestMatchers("/team/rejectRequest").hasAnyRole("ADMIN", "LEADER")
                .requestMatchers("/team/removeMember").hasAnyRole("ADMIN", "LEADER")
                .requestMatchers("/team/assignLeader").hasRole("ADMIN")
                .requestMatchers("/team/**").hasAnyRole("ADMIN", "LEADER", "DEVELOPER")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/dashboard")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
        return http.build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_LEADER \n ROLE_LEADER > ROLE_DEVELOPER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }


}