package pti.test.server.config.jsf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pti.test.server.ExceptionsHandler;

import javax.faces.application.ViewExpiredException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ViewExpiredExceptionFilter implements Filter {

    @Autowired
    private ExceptionsHandler exceptionsHandler;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext());
        this.exceptionsHandler = ctx.getBean(ExceptionsHandler.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;
        try {
            chain.doFilter(req, response);
        } catch (Exception e) {
            if (e.getCause() instanceof ViewExpiredException) {
                String url = "";
                if (request instanceof HttpServletRequest) {
                    url = ((HttpServletRequest) request).getRequestURL().toString();
                }
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendRedirect("/expired.xhtml?url=" + url);
            } else {
                try {
                    exceptionsHandler.handleGenericException(e, req);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void destroy() {

    }

}