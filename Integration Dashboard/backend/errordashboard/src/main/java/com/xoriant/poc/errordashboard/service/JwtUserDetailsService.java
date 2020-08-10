package com.xoriant.poc.errordashboard.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		if("admin".equals(userName)){
			return new User("admin","$2y$10$YxmhbciTZTJ0IHcIVnR9MOkcqamCfcqBRd12h.GKR7qSXL783mOBG", new ArrayList<>());
		}
		else{
			throw new UsernameNotFoundException("Username not found!");
		}
	}

}
