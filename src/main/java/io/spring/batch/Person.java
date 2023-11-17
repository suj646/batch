package io.spring.batch;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//public class Person {
//    private long id1; // Corresponds to the 'id' column
//    private String firstName; // Corresponds to the 'first_Name' column
//    private String lastName; // Corresponds to the 'last_Name' column
//    private String contact; // Corresponds to the 'contact' column
//    private Date birthdate; // Corresponds to the 'birthdate' column
//    private Date lastUpdateTs; // Corresponds to the 'last_update_ts' column
//    private short hostNo; // Corresponds to the 'host_no' column
//    private int prodBrndNo; // Corresponds to the 'prodBrndNo' column
//    
//	public Person(long id1, String firstName, String lastName,String contact,  Date birthdate, Date lastUpdateTs,
//			short hostNo, int prodBrndNo) {
//		super();
//		this.id1 = id1;
//		this.firstName = firstName;
//		this.lastName = lastName;
//		this .contact=contact;
//		this.birthdate = birthdate;
//		this.lastUpdateTs = lastUpdateTs;
//		this.hostNo = hostNo;
//		this.prodBrndNo = prodBrndNo;
//	}
//
//	public String getContact() {
//		return contact;
//	}
//
//	public void setContact(String contact) {
//		this.contact = contact;
//	}
//
//	public Person() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//
//	public long getId1() {
//		return id1;
//	}
//
//	public void setId1(long id1) {
//		this.id1 = id1;
//	}
//
//	public String getFirstName() {
//		return firstName;
//	}
//
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//
//	public String getLastName() {
//		return lastName;
//	}
//
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//
//
//	public Date getBirthdate() {
//		return birthdate;
//	}
//
//	public void setBirthdate(Date birthdate) {
//		this.birthdate = birthdate;
//	}
//
//	public Date getLastUpdateTs() {
//		return lastUpdateTs;
//	}
//
//	public void setLastUpdateTs(Date lastUpdateTs) {
//		this.lastUpdateTs = lastUpdateTs;
//	}
//
//	public short getHostNo() {
//		return hostNo;
//	}
//
//	public void setHostNo(short hostNo) {
//		this.hostNo = hostNo;
//	}
//
//	public int getProdBrndNo() {
//		return prodBrndNo;
//	}
//
//	public void setProdBrndNo(int prodBrndNo) {
//		this.prodBrndNo = prodBrndNo;
//	}
//
//   
//	
//    
//}


@Data
@Builder
public class Person {
	 	private long id1;
	    private String firstName;
	    private String lastName;
	    private Date birthdate;
		public Person() {
			super();
			
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
		@Override
		public String toString() {
			return "Person [id1=" + id1 + ", firstName=" + firstName + ", lastName=" + lastName + ", birthdate="
					+ birthdate + "]";
		}
	
	
	    
}