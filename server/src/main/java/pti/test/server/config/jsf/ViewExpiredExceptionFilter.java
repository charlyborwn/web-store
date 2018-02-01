package pti.test.server.config.jsf;

import javax.faces.application.ViewExpiredException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ViewExpiredExceptionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

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
                String queryString = "";
                if (request instanceof HttpServletRequest) {
                    url = ((HttpServletRequest)request).getRequestURL().toString();
                    queryString = ((HttpServletRequest)request).getQueryString();
                }
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendRedirect("/expired.xhtml?url="+url);
            } else {
                throw e;
            }
        }
    }

    @Override
    public void destroy() {

    }

}