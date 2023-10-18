package io.spring.batch.domain;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class Person2 {

	
	private int id;
	private String firstName;
//	private String lastName;
//	private Date birthdate;
	public Person2(int id, String firstName) {
		super();
		this.id = id;
		this.firstName = firstName;
//		this.lastName = lastName;
//		this.birthdate = birthdate;
	}
	public Person2() {
		super();
		
	}
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
//	public String getLastName() {
//		return lastName;
//	}
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//	public Date getBirthdate() {
//		return birthdate;
//	}
//	public void setBirthdate(Date birthdate) {
//		this.birthdate = birthdate;
//	}
}

