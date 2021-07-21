package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

	List<Account> findByEmailLikeAndPasswordLike(String email, String password);

	List<Account> findByName(String name);

	Account findByNameLike(String name);

	List<Account> findBySecretLikeAndAnswerLike(String secret,String answer);

	List<Account> findByEmailLike(String email);

	String findByPassword(String password);

}

