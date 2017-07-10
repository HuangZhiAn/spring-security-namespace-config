package com.hza.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created by JoeHuang on 2017/7/10.
 */
@Component
public class MyPasswordEncoder implements PasswordEncoder{
    public String encode(CharSequence charSequence) {
        return charSequence.toString()+"hza";
    }

    public boolean matches(CharSequence charSequence, String s) {
        return encode(charSequence).equals(s);
    }
}
