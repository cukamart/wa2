package cz.cvut.fel.aos.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/destination/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/destination/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/destination/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/flight/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/flight/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/flight/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/reservation/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/reservation/**").hasRole("MANAGER")
                .antMatchers(HttpMethod.GET, "/reservation/{\\d+}").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/reservation/{\\d+}/{\\d+}").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/reservation/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER").and()
                .withUser("admin").password("password").roles("USER", "ADMIN").and()
                .withUser("manager").password("password").roles("USER", "MANAGER");
    }
}
