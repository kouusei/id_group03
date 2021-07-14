package com.example.demo;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="schedule")
public class Schedule {
	//フィールド
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name="code")
		private int code;

		@Column(name="category_code")
		private int category_code;

		@Column(name="scheduledate")
		private Date scheduledate;

		@Column(name="starttime")
		private Time starttime;

		@Column(name="endtime")
		private Time endtime;

		@Column(name="schedule")
		private String schedule;

		@Column(name="schedulememo")
		private String schedulememo;

		public Schedule() {

		}

		public Schedule( int category_code, Date scheduledate, String schedule, String schedulememo) {

			this.category_code=category_code;
			this.scheduledate=scheduledate;
			this.schedule = schedule;
			this.schedulememo = schedulememo;
		}

		public Schedule( int category_code, Date scheduledate, Time starttime, Time endtime, String schedule, String schedulememo) {

			this.category_code=category_code;
			this.scheduledate=scheduledate;
			this.starttime = starttime;
			this.endtime = endtime;
			this.schedule = schedule;
			this.schedulememo = schedulememo;
		}

		public Schedule(int code, int category_code, Date scheduledate, Time starttime, Time endtime, String schedule, String schedulememo) {

			this.code = code;
			this.category_code=category_code;
			this.scheduledate=scheduledate;
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

		public int getCategory_code() {
			return category_code;
		}

		public void setCategory_code(int category_code) {
			this.category_code = category_code;
		}

		public Date getScheduledate() {
			return scheduledate;
		}

		public void setScheduledate(Date scheduledate) {
			this.scheduledate = scheduledate;
		}

		public Time getStarttime() {
			return starttime;
		}

		public void setStarttime(Time starttime) {
			this.starttime = starttime;
		}

		public Time getEndtime() {
			return endtime;
		}

		public void setEndtime(Time endtime) {
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
