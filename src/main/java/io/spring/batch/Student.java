package io.spring.batch;

import java.sql.Timestamp;
import java.util.Date;

public class Student {
    private long id1;
    private String firstName;
    private String lastName;
    private Date birthdate;
   
	public Student(long id1, String firstName, String lastName, Date birthdate) {
		super();
		this.id1 = id1;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
	}
	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getId1() {
		return id1;
	}
	public void setId1(long id1) {
		this.id1 = id1;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	@Override
	public String toString() {
		return "Student [id1=" + id1 + ", firstName=" + firstName + ", lastName=" + lastName + ", birthdate="
				+ birthdate + "]";
	}
	
	
	

   
}