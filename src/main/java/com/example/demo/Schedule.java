package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class Schedule {
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

		@Column(name="starttime")
		private String starttime;

		@Column(name="endtime")
		private String endtime;

		@Column(name="schedule")
		private String schedule;

		@Column(name="schedulememo")
		private String schedulememo;

		public Schedule() {

		}

		public Schedule(String starttime, String endtime, String schedule, String schedulememo) {

			this.starttime = starttime;
			this.endtime = endtime;
			this.schedule = schedule;
			this.schedulememo = schedulememo;
		}

		public Schedule(int code, String name, String email, String password, String starttime, String endtime,
				String schedule, String schedulememo) {

			this.code = code;
			this.name = name;
			this.email = email;
			this.password = password;
			this.starttime = starttime;
			this.endtime = endtime;
			this.schedule = schedule;
			this.schedulememo = schedulememo;
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

		public String getStarttime() {
			return starttime;
		}

		public void setStarttime(String starttime) {
			this.starttime = starttime;
		}

		public String getEndtime() {
			return endtime;
		}

		public void setEndtime(String endtime) {
			this.endtime = endtime;
		}

		public String getSchedule() {
			return schedule;
		}

		public void setSchedule(String schedule) {
			this.schedule = schedule;
		}

		public String getSchedulememo() {
			return schedulememo;
		}

		public void setSchedulememo(String schedulememo) {
			this.schedulememo = schedulememo;
		}


}
