package com.hza.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JoeHuang on 2017/7/6.
 */
@Component
public class CustomUserDetailService implements UserDetailsService{

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = null ;
        if(s.equals("Joe")){
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            user= new User(s,"111111",authorities);
        }else if(s.equals("Jone")){
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            user= new User(s,"111111",authorities);
        }else{
            throw  new UsernameNotFoundException("用户不存在");
        }
        return user;
    }
}
