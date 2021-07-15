package com.example.demo;

public class MyDate {
	private String date;
	private String month;
	private String year;

	public MyDate() {

	}

	public MyDate(String date) {

		this.date = date;
	}

	public MyDate( String month, String year) {

		this.month = month;
		this.year = year;
	}

	public MyDate(String date, String month, String year) {
		super();
		this.date = date;
		this.month = month;
		this.year = year;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}



}


