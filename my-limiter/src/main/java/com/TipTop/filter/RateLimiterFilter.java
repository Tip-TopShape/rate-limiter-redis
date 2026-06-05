package com.TipTop.filter;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.TipTop.model.RateLimiterResult;
import com.TipTop.model.RateLimiterStatus;
import com.TipTop.ratelimiter.RateLimiterService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimiterFilter extends OncePerRequestFilter {

    private final RateLimiterService rateLimiterService;

    public RateLimiterFilter(
            RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse reponse,
            FilterChain chain) throws ServletException, IOException {

        String clientId = request.getHeader("X-Client-Id");
        
        RateLimiterResult status = rateLimiterService.check(clientId, false);

        if(status == null) {
            // try queue
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payload = Map.of(
                // TODO: serialize
            );
            rateLimiterService.enqueue(clientId, clientId)
        }

        System.out.println("Incoming request: " + request.getMethod() + " " + request.getRequestURI());
    }
}
