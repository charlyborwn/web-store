package pti.test.server.config.security;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pti.test.server.SuperRedirecter;
import pti.test.server.config.jsf.JSFRedirectStrategy;
import pti.test.service.authorization.SystemUserDetailsService;

/**
 * @author Syrotyuk R.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SystemUserDetailsService systemUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().disable();

        http.csrf().disable();
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login.xhtml");

        http.authorizeRequests()
                .antMatchers("/login.xhtml").permitAll()
                .antMatchers("/shop.xhtml").hasAnyRole("ADMIN", "USER")
                .antMatchers("/cart.xhtml").hasAnyRole("ADMIN", "USER")
                .antMatchers("/cart_mini.xhtml").hasAnyRole("ADMIN", "USER")
                .antMatchers("/dialog.xhtml").hasAnyRole("ADMIN", "USER")
                .antMatchers("/favourites.xhtml").hasAnyRole("ADMIN", "USER")
                .antMatchers("/history.xhtml").hasAnyRole("ADMIN", "USER")
                .antMatchers("/productListForUser.xhtml").hasAnyRole("ADMIN", "USER")
                .antMatchers("/admin/add_product.xhtml").hasRole("ADMIN")
                .antMatchers("/treeTable.xhtml").hasAnyRole("ADMIN", "USER")
                .antMatchers("/admin/product_update.xhtml").hasRole("ADMIN")
                .antMatchers("/admin/product.xhtml").hasRole("ADMIN")
                .antMatchers("/admin/user.xhtml").hasRole("ADMIN")
                .antMatchers("/admin/users.xhtml").hasRole("ADMIN")
                .antMatchers("/admin/add_types.xhtml").hasRole("ADMIN")
                .antMatchers("/test.xhtml").hasAnyRole("ADMIN", "USER")
                .and().formLogin().loginPage("/login.xhtml")
                .loginProcessingUrl("/login").defaultSuccessUrl("/shop.xhtml").and()
                .exceptionHandling().accessDeniedPage("/denied.xhtml");

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(systemUserService).passwordEncoder(passwordEncoder());
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/javax.faces.resource/**");
    }

    @Bean
    public HttpSessionSecurityContextRepository httpSessionSecurityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SessionManagementFilter sessionManagementFilter() {
        SessionManagementFilter filter = new SessionManagementFilter(httpSessionSecurityContextRepository());
        JSFRedirectStrategy strategy = new JSFRedirectStrategy();
        strategy.setInvalidSessionUrl("/login.xhtml");

        filter.setInvalidSessionStrategy(strategy);
        return filter;
    }

}