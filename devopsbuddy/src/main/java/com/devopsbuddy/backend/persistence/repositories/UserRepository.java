package com.devopsbuddy.backend.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.devopsbuddy.backend.persistence.domain.backend.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	/**
	 * Returns a user given a usename or null if not found
	 * @param username
	 * @return
	 */
	public User findByUsername(String username);
}
