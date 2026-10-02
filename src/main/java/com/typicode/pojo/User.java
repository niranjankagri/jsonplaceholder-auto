package com.typicode.pojo;

/**
 * Defines User class, mapped from a JSON item of the /users end-point.
 * JSON fields without a matching Java field are ignored when mapping.
 *
 */
public class User {

	private int id;
	private String name;
	// Login name, used to search for the user
	private String username;
	private String email;
	private String phone;
	private String website;
	private Address address;
	private Company company;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}

	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}

	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
}

/**
 * Postal address of a {@link User}
 */
class Address {
	// Note: the JSON field is "street", so this misspelled field is never filled
	private String strret;
	private String suite;
	private String city;
	private String zipcode;

	public String getStrret() {
		return strret;
	}
	public void setStrret(String strret) {
		this.strret = strret;
	}

	public String getSuite() {
		return suite;
	}
	public void setSuite(String suite) {
		this.suite = suite;
	}

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
}

/**
 * Company a {@link User} works for
 */
class Company {
	private String name;
	private String catchPhrase;
	private String bs;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getCatchPhrase() {
		return catchPhrase;
	}
	public void setCatchPhrase(String catchPhrase) {
		this.catchPhrase = catchPhrase;
	}

	public String getBs() {
		return bs;
	}
	public void setBs(String bs) {
		this.bs = bs;
	}
}
