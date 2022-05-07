package net.gamebacon.demo.security.configuration;

import lombok.AllArgsConstructor;
import net.gamebacon.demo.login_user.LoginUserService;
import net.gamebacon.demo.login_user.Role;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private LoginUserService loginUserService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/register/**", "/login", "/forgot-password/**", "/forgot-password-reset/**").anonymous() //Only anonymous can login/register
                .antMatchers("/", "/terms").permitAll() //everyone can see home page & essentials
                .antMatchers("/games/**").authenticated() //only authenticated can play games
                .antMatchers("/images/**", "/js/**", "/css/**").permitAll() //all need access to statis files
                .antMatchers("/users/**").hasAuthority(Role.ADMIN.name()) // only admin can manage users
                .anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedPage("/access-denied")
                .and().formLogin(form -> {
                    form.loginPage("/login");
                    form.defaultSuccessUrl("/");
                    form.failureUrl("/login?error=true");
                })
        ;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(loginUserService);
        return provider;
    }
}
