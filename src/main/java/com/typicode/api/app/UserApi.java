package com.typicode.api.app;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.typicode.api.APIUser;
import com.typicode.api.StatusCode;
import com.typicode.pojo.User;

/**
 * Users API class defines all the operation that can be performed by user or to user
 * (end-point: /users)
 *
 */
public class UserApi {

	private final String URL = "/users";
	private APIUser apiUser;

	/**
	 * Initialize {@link APIUser} class and sets the relative URL to users
	 * @param apiUser : {@link APIUser}
	 */
	public UserApi(APIUser apiUser) {
		this.apiUser = apiUser;
		apiUser.setRelativeUrl(URL);
	}

	/**
	 * Fetches all the users (GET /users)
	 * @return : {@link APIUser} to verify operation
	 */
	public APIUser fetchAllUsers() {
		this.apiUser.setRelativeUrl(URL);
		this.apiUser.requestGet();

		return this.apiUser;
	}

	/**
	 * Searches for user as specified username (GET /users?username=...).
	 * Checks the status code, that every user has an id, that every username matches (ignoring case)
	 * and that exactly one user is found (fails the test immediately otherwise).
	 * @param username : Username to be searched
	 * @return : the matching {@link User}
	 */
	public User searchByUserName(String username) {
		this.apiUser.setRelativeUrl(URL + "?username=" + username);
		User[] users = (User[])this.apiUser
					   .requestGet()
					   .verify().responseCode(StatusCode.OK, "Searching User details")
					   .verify().responseBodySafely("id", everyItem(not(emptyOrNullString())), "User ID should not be null or empty")
					   .verify().responseBodySafely("username", everyItem(is(equalToIgnoringCase(username))), "Username should be " + username)
					   .getResponse().getResponseBody(new User[]{});

		this.apiUser.getAssertUtil().verify(users.length, 1, "Exactly one user should be found with username " + username);

		return users[0];
	}
}
