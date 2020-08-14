package com.xoriant.poc.errordashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xoriant.poc.errordashboard.dao.UsersDao;

@Repository
public interface UserRepository extends JpaRepository<UsersDao, Long>{
	boolean existsByUsername(String userName);
	UsersDao findByUsername(String userName);
}
