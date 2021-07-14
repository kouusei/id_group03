package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sche")
public class Sche {
	//フィールド
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name="code")
		private int code;

		@Column(name="category_code")
		private int category_code;

		@Column(name="scheduledate")
		private String scheduledate;

		@Column(name="starttime")
		private String starttime;

		@Column(name="endtime")
		private String endtime;

		@Column(name="schedule")
		private String schedule;

		@Column(name="schedulememo")
		private String schedulememo;

		public Sche() {

		}

		public Sche( int category_code, String scheduledate, String schedule, String schedulememo) {

			this.category_code=category_code;
			this.scheduledate=scheduledate;
			this.schedule = schedule;
			this.schedulememo = schedulememo;
		}

		public Sche( int category_code, String scheduledate, String starttime, String endtime, String schedule, String schedulememo) {

			this.category_code=category_code;
			this.scheduledate=scheduledate;
			this.starttime = starttime;
			this.endtime = endtime;
			this.schedule = schedule;
			this.schedulememo = schedulememo;
		}

		public Sche(int code, int category_code, String scheduledate, String starttime, String endtime, String schedule, String schedulememo) {

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

		public String getScheduledate() {
			return scheduledate;
		}

		public void setScheduledate(String scheduledate) {
			this.scheduledate = scheduledate;
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
