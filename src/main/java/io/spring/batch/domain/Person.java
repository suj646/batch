package io.spring.batch.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
public class Person {
	
	
	private long id1;
	private String firstName;
	private String lastName;
	private Date birthdate;
	
	
	
	
	
	
	
	@Override
	public String toString() {
		return "Person [id1=" + id1 + ", firstName=" + firstName + ", lastName=" + lastName + ", birthdate=" + birthdate
				+ "]";
	}
	public Person() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Person(long id1, String firstName, String lastName, Date birthdate) {
		super();
		this.id1 = id1;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
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
	
	
	
	
	
	
}