package com.xoriant.poc.errordashboard.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.xoriant.poc.errordashboard.dao.UsersDao;
import com.xoriant.poc.errordashboard.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UsersDao user=userRepository.findByUsername(userName);
		if(null!= user && user.isActive()){
			return new User(user.getUsername(),passwordEncoder.encode(user.getPassword()), new ArrayList<>());
		}
		else{
			throw new UsernameNotFoundException("Username not found!");
		}
	}

}
