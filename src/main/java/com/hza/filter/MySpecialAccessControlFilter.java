package com.hza.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by JoeHuang on 2017/7/7.
 */
public class MySpecialAccessControlFilter extends GenericFilterBean{

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws AccessControlException, IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        while(iterator.hasNext()){
            GrantedAuthority next = iterator.next();
            String authority = next.getAuthority();
            if(authority.equals("ROLE_SPECIAL")){
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }
        String e = "非special用户不能访问";
        String e2 = new String(e.getBytes(),"utf-8");
        throw new AccessControlException(e2);
    }
}
