package com.mrwhite.polls.Security;

import com.mrwhite.polls.Exception.PollException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// We should use OncePerRequestFilter since we are doing a database call, there is no point in doing this more than once
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private JwtTokenProvider jwtTokenProvider;
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,CustomUserDetailsService customUserDetailsService){
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException{
        try {
            String token = jwtTokenProvider.getJwtFromRequest(request);
            if(token != null && jwtTokenProvider.validateToken(token) && !jwtTokenProvider.isTokenExpired(token)){
                Long id = jwtTokenProvider.getIdFromToken(token);
                UserDetails userDetails = customUserDetailsService.loadUserById(id);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (PollException exception){
            log.error("Could not set user authentication in security context");
            SecurityContextHolder.clearContext();
            response.sendError(exception.getCode().getErrorCode(),exception.getMessage());
        }
        filterChain.doFilter(request,response);
    }
}
