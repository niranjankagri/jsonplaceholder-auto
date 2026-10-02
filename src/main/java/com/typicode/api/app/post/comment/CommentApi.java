package com.typicode.api.app.post.comment;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.typicode.Util;
import com.typicode.api.APIUser;
import com.typicode.api.StatusCode;
import com.typicode.pojo.Comment;
import com.typicode.pojo.Post;

/**
 * Comments API class defines all the operation that can be performed on comment
 * (end-point: /comments)
 *
 */
public class CommentApi {

	private final String URL = "/comments";
	private APIUser apiUser;

	/**
	 * Initialize {@link APIUser} class and sets the relative URL to comments
	 * @param apiUser : {@link APIUser}
	 */
	public CommentApi(APIUser apiUser) {
		this.apiUser = apiUser;
		apiUser.setRelativeUrl(URL);
	}

	/**
	 * All the comments on specified posts, one request per post (GET /comments?postId=...).
	 * Checks the status code, that every comment has an id and that every comment belongs to the post.
	 * @param posts : {@link Post}
	 * @return : All the {@link Comment} related to {@link Post} as provided
	 */
	public List<Comment> getAllCommentOnPosts(List<Post> posts) {
		List<Comment> allComments = new ArrayList<>();
		for(Post post : posts) {
			this.apiUser.setRelativeUrl(URL + "?postId=" + post.getId());
			Comment[] commentOnPost = (Comment[])this.apiUser
														.requestGet()
														.verify().responseCode(StatusCode.OK, "Getting all comments on post")
														.verify().responseBodySafely("id", everyItem(not(emptyOrNullString())), "Every comment should have Id")
														.verify().responseBodySafely("postId", everyItem(is(post.getId())), "Every comment should have postId as " + post.getId())
														.getResponse().getResponseBody(new Comment[]{});
			allComments.addAll(Arrays.asList(commentOnPost));
		}

		return allComments;
	}

	/**
	 * Validates email format in specified comments.
	 * Failures are soft assertions, so every invalid email is reported at the end of the test.
	 * @param comments : {@link Comment} list to check
	 */
	public void verifyEmailFormatInComments(List<Comment> comments) {
		for(Comment comment : comments) {
			// local-part @ domain . top-level domain of 2-4 letters
			boolean isMatched = matchesPattern("^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$").matches(comment.getEmail());
			apiUser.getAssertUtil().verifySafely(isMatched, true, "Email " + comment.getEmail() + " should be in correct format");

			if(isMatched) {
				Util.getLogger().info("PASSED: Email " + comment.getEmail() + " is in correct format");
			}
		}
	}
}
