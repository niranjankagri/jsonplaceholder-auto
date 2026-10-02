package com.typicode.api.app.post;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;

import com.typicode.api.APIUser;
import com.typicode.api.StatusCode;
import com.typicode.pojo.Post;
import com.typicode.pojo.User;

/**
 * Posts API class defines all the operation that can be performed on posts
 * (end-point: /posts)
 *
 */
public class PostApi {

	private final String URL = "/posts";
	private APIUser apiUser;

	/**
	 * Initialize {@link APIUser} class and sets the relative URL to posts
	 * @param apiUser : {@link APIUser}
	 */
	public PostApi(APIUser apiUser) {
		this.apiUser = apiUser;
		apiUser.setRelativeUrl(URL);
	}

	/**
	 * Search for all the posts created by specified User (GET /posts?userId=...).
	 * Checks the status code, that every post has an id and that every post belongs to the user.
	 * @param user : {@link User}
	 * @return : List of {@link Post} from {@link User}
	 */
	public List<Post> getAllUserPosts(User user) {
		this.apiUser.setRelativeUrl(URL + "?userId=" + user.getId());
		Post[] posts = (Post[])this.apiUser
						.requestGet()
						.verify().responseCode(StatusCode.OK, "Fetches all the posts created by user")
						.verify().responseBodySafely("id", everyItem(not(emptyOrNullString())), "Every post should have Id")
						.verify().responseBodySafely("userId", everyItem(is(user.getId())), "Every post should have userId as " + user.getId())
						.getResponse().getResponseBody(new Post[]{});

		return Arrays.asList(posts);
	}
}
