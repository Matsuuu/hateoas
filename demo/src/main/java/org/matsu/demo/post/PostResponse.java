package org.matsu.demo.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.matsu.demo.comment.Comment;
import org.matsu.hateoas.core.HalId;
import org.matsu.hateoas.core.HalLink;
import org.matsu.hateoas.core.HalLinks;
import org.matsu.hateoas.core.HalResponse;

@HalResponse(PostController.class)
public record PostResponse(
        @HalId long id, 
        String title, 
        String content,
        List<Comment> comments, 
        @HalLinks Map<String, HalLink> links
) {

  public static PostResponse from(long id, String title, String content,
                                  List<Comment> comments) {
    return new PostResponse(id, title, content, comments,
                            new HashMap<>());
  }

  public static PostResponse from(Post post) {
    return PostResponse.from(post.id, post.title, post.content, post.comments);
  }
}
