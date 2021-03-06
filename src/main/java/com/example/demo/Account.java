package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class Account {
	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="code")
	private int code;

	@Column(name="name")
	private String name;

	@Column(name="email")
	private String email;

	@Column(name="password")
	private String password;

	@Column(name="secret")
	private String secret;

	@Column(name="answer")
	private String answer;

	public Account() {

	}

	public Account(int code, String name, String email ,String password, String secret, String answer) {
		this.code = code;
		this.name = name;
		this.email = email;
		this.password = password;
		this.secret = secret;
		this.answer = answer;
	}

	public Account(String secret, String answer) {
		this.secret = secret;
		this.answer = answer;
	}

	public Account(String secret) {
		this.secret = secret;
	}

	public Account(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public Account(String name, String email, String password, String secret, String answer) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.secret = secret;
		this.answer = answer;
	}

	public Account(int code, String name, String email, String secret, String answer) {
		this.code = code;
		this.name = name;
		this.email = email;
		this.secret = secret;
		this.answer = answer;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}

