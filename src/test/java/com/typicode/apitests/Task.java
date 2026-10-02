package com.typicode.apitests;

import java.util.List;

import org.testng.annotations.Test;

import com.typicode.AbstractBaseTest;
import com.typicode.pojo.Comment;
import com.typicode.pojo.Post;
import com.typicode.pojo.User;

/**
 * Validates following scenario:
 * 1. Search for user, validates its response code and response body
 * 2. Retrieves all the posts created by user, validates its response code and response body
 * 3. Retrieves all the comments on posts created by user, validates its response code and response body
 * 4. Validate if the emails in the comment section are in the proper format
 */
public class Task extends AbstractBaseTest {

	/**
	 * Every email in the comments on Samantha's posts should be in a valid format
	 */
	@Test
	public void validateEmailFormatInCommentsOfSearchedUser() {
		// Search for user 'Samantha' and validates response
		User user = api()
						.users()
						.searchByUserName("Samantha");
		// Retrieves all the posts created by user searched in above step and validates response
		List<Post> posts = api()
						  		.posts()
						  		.getAllUserPosts(user);
		// Retrieves all the comments on posts collected in above step and validates response
		List<Comment> comments = api()
									.comments()
									.getAllCommentOnPosts(posts);
		// Validate if the emails in the comment section are in the proper format
		api().comments().verifyEmailFormatInComments(comments);

		// throws assertion if there's any failure in above soft assertions
		throwAssertionOnFailure();
	}
}
