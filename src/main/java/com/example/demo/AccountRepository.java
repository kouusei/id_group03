package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

	List<Account> findByEmailLikeAndPasswordLike(String email, String password);

	Account findByName(String name);

	String findByPassword(String password);
}

