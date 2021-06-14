package com.rozsa.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class PreFlightFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        System.out.println(req.getMethod() + " " + req.getRequestURI());

        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, PATCH, GET, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        res.setHeader("Access-Control-Allow-Origin", "*");

        if (req.getMethod().equalsIgnoreCase("OPTIONS")) {
            log.info("Pre-flight request");
            res.setStatus(HttpServletResponse.SC_ACCEPTED);
        } else {
            chain.doFilter(req, res);
        }
    }
}
