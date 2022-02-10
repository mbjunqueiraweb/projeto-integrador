package com.w4.projetoIntegrador.config;

import com.w4.projetoIntegrador.repository.UserRepository;
import com.w4.projetoIntegrador.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository repository;


    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/productannouncements").permitAll()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                .antMatchers(HttpMethod.POST, "/inboundorder").hasAnyAuthority("AGENT")
                .antMatchers(HttpMethod.GET, "/products").hasAnyAuthority("AGENT")
                .antMatchers(HttpMethod.POST, "/products").hasAnyAuthority("AGENT")
                .antMatchers(HttpMethod.POST, "/orders").hasAnyAuthority("BUYER")
                .antMatchers(HttpMethod.POST, "/scheduled-orders").hasAnyAuthority("BUYER")

                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AuthenticationByTokenFilter(tokenService, repository), UsernamePasswordAuthenticationFilter.class);
    }
}


