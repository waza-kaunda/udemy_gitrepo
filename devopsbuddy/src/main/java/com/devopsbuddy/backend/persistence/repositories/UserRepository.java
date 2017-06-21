package com.devopsbuddy.backend.persistence.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.devopsbuddy.backend.persistence.domain.backend.User;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends CrudRepository<User, Long> {

	/**
	 * Returns a user given a username or null if not found
	 * @param username
	 * @return
	 */
	User findByUsername(String username);
	
	/**
	 * Returns a user given a user email or else null
	 * @param email
	 * @return
	 */
	User findByEmail(String email); 
	
	@Modifying
	@Query("update User u set u.password = :password where u.id = :userId")
	void updateUserPassword(@Param("userId") long userId, @Param("password") String password);
	
}
