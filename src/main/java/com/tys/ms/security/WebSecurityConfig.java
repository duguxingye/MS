package com.tys.ms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("customUserDetailsService")
    UserDetailsService userDetailsService;

    @Autowired
    PersistentTokenRepository tokenRepository;

    private SessionRegistry sessionRegistry;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter,CsrfFilter.class);

        http.authorizeRequests()
                .antMatchers("/", "/info").access("hasRole('REGULAR') or hasRole('GROUP') or hasRole('AREA') or hasRole('ADMIN')")
                .antMatchers("/", "/change-pwd-*").access("hasRole('REGULAR') or hasRole('GROUP') or hasRole('AREA') or hasRole('ADMIN')")
                .antMatchers("/", "/list-product-*").access("hasRole('REGULAR') or hasRole('GROUP') or hasRole('AREA') or hasRole('ADMIN')")
                .antMatchers("/", "/add-product-*").access("hasRole('REGULAR') or hasRole('GROUP') or hasRole('AREA') or hasRole('ADMIN')")
                .antMatchers("/", "/export-product-*").access("hasRole('REGULAR') or hasRole('GROUP') or hasRole('AREA') or hasRole('ADMIN')")
                .antMatchers("/", "/list").access("hasRole('GROUP') or hasRole('AREA') or hasRole('ADMIN') or hasRole('ADMIN')")
                .antMatchers("/addUser/**", "/delete-user-*", "/edit-user-*").access("hasRole('GROUP') or hasRole('AREA') or hasRole('ADMIN')")
                .and().formLogin().loginPage("/login")
                .loginProcessingUrl("/login").usernameParameter("jobId").passwordParameter("password")
                .successHandler(authSuccessHandler())
                .failureHandler(authFailureHandler())
//                .defaultSuccessUrl("/info", false)
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and().rememberMe().rememberMeParameter("remember-me").tokenRepository(tokenRepository)
                .tokenValiditySeconds(3600)
                .and().csrf()
                .and().exceptionHandling().accessDeniedPage("/Access_Denied")
                .and().sessionManagement()
                .sessionFixation().newSession()
                .maximumSessions(1).maxSessionsPreventsLogin(true)
                .sessionRegistry(sessionRegistry());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    @Bean
    protected AuthenticationSuccessHandler authSuccessHandler() {
        return new AppAuthenticationSuccessHandler();
    }

    @Bean
    protected AuthenticationFailureHandler authFailureHandler() {
        return new AppAuthenticationFailureHandler();
    }

//    @Bean
//    protected LogoutSuccessHandler appLogoutSuccessHandler() {
//        return new AppLogoutSuccessHandler();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
        PersistentTokenBasedRememberMeServices tokenBasedService = new PersistentTokenBasedRememberMeServices(
                "remember-me", userDetailsService, tokenRepository);
        return tokenBasedService;
    }

    @Bean
    public AuthenticationTrustResolver getAuthenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }
}
