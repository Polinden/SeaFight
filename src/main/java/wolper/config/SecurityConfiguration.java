package wolper.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import wolper.logic.SecurityErrorHandler;
import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth

                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,password,enabled from USER_AUTHENTICATION where username=?")
                .authoritiesByUsernameQuery("select u1.username, u2.role from USER_AUTHENTICATION u1, USER_AUTHORIZATION u2 where u1.user_id = u2.user_id and u1.username =?");

            // Если тестируем без МайСКюЭля
            //  .inMemoryAuthentication()
            //  .withUser("papa").password("qwas").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //Для доступности контроллера СВОМПа
                .csrf()
                .ignoringAntMatchers("/data/**")
                .and()

                //Все остальное
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/goin")
                .failureForwardUrl("/error")
                .failureHandler(new SecurityErrorHandler())
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/register", "/resources/**", "/resources/webjars/**", "/webjars/**", "/",
                        "/home", "/double_reg_final/**", "/double_reg/**", "/test", "/successreg", "/missedpasword", "/regerror").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/goout"))
                .logoutSuccessUrl("/home")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "SESSION")
                .and()
                .sessionManagement()
                .invalidSessionUrl("/home")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .sessionRegistry(sessionRegistry());

    }

    // Настройка класса, читающего данные сессии - для списка залогиненных пользователей
    @Bean(name = "sessionRegistry")
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

}
