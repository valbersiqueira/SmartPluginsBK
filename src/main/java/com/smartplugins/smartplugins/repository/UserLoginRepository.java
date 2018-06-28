package com.smartplugins.smartplugins.repository;

import com.smartplugins.smartplugins.model.User;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginRepository extends JpaRepository<User, Long> {
	
}
