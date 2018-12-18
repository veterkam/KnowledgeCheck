package edu.javatraining.knowledgecheck.controller;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "SessionLocaleFilter", urlPatterns = {"/*"})
@Singleton
public class SessionLocaleFilter implements Filter {
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Read locale code (ru/en/etc.) from URL end save into session
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

        if (req.getParameter("sessionLocale") != null) {
            session.setAttribute("lang", req.getParameter("sessionLocale"));
        } else if(session.getAttribute("lang") == null) {
            session.setAttribute("lang", "ru");
        }
        chain.doFilter(request, response);
    }

    public void destroy() {}
    public void init(FilterConfig arg0) throws ServletException {}
}