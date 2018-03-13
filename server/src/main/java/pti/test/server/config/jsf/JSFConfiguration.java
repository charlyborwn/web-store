package pti.test.server.config.jsf;

import com.sun.faces.config.ConfigureListener;
import org.primefaces.webapp.filter.FileUploadFilter;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.faces.webapp.FacesServlet;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;
import java.util.HashMap;

@Configuration
public class JSFConfiguration {

    @Bean
    public static ViewScope viewScope() {
        return new ViewScope();
    }

    @Bean
    public static CustomScopeConfigurer scopeConfigurer() {
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("view", viewScope());
        configurer.setScopes(hashMap);
        return configurer;
    }

    @Bean
    public FacesServlet facesServlet() {
        return new FacesServlet();
    }

    @Bean
    public ServletRegistrationBean facesServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                facesServlet(), "*.xhtml");
        registration.setName("FacesServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Bean
    public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener() {
        return new ServletListenerRegistrationBean<ConfigureListener>(
                new ConfigureListener());
    }

    @Bean
    public ServletContextInitializer initializer() {
        return new ServletContextInitializer() {

            @Override
            public void onStartup(ServletContext servletContext)
                    throws ServletException {

                servletContext.setInitParameter("javax.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL", "true");
                servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", "true");
                servletContext.setInitParameter("contextConfigLocation", "/WEB-INF/spring-security.xml");
                servletContext.setInitParameter("javax.faces.FACELETS_LIBRARIES", "/WEB-INF/springsecurity.taglib.xml");
                servletContext.setInitParameter("primefaces.THEME", "blitzer");
                servletContext.setInitParameter("primefaces.UPLOADER", "commons");

                EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);

                servletContext.addFilter("securityFilter", new DelegatingFilterProxy("springSecurityFilterChain"))
                        .addMappingForUrlPatterns(dispatcherTypes, false, "/*");
                servletContext.addFilter("errorHandlerFilter", new ViewExpiredExceptionFilter())
                        .addMappingForUrlPatterns(dispatcherTypes, false, "/*");
                servletContext.addFilter("PrimeFaces FileUpload Filter", new FileUploadFilter())
                        .addMappingForServletNames(dispatcherTypes, false, "FacesServlet");

            }
        };
    }
}
