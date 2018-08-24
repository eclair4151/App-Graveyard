package com.tomer.drexelgpacalc;

public class Course {
	private String name;
	private String grade;
	private Double credits;
	
	public Course(String name, String grade, double credits)
	{
		this.name = name;
		this.grade = grade;
		this.credits = credits;
	}
	
	public String getname()
	{
	 return name;
	}
	
	public String getgrade()
	{
		return grade;
	}
	
	public double getcredits()
	{
		return credits;
	}
	

}
