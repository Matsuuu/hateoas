package org.matsu.demo.post;

import java.util.ArrayList;
import java.util.List;

import org.matsu.demo.comment.Comment;
import org.matsu.hateoas.core.HalLink;
import org.matsu.hateoas.core.HalLinks;
import org.matsu.hateoas.core.HalResponse;

@HalResponse(PostController.class)
public record PostResponse(
        long id, 
        String title, 
        String content,
        List<Comment> comments, 
        @HalLinks List<HalLink> links
) {

  public static PostResponse from(long id, String title, String content,
                                  List<Comment> comments) {
    return new PostResponse(id, title, content, comments,
                            new ArrayList<>());
  }

  public static PostResponse from(Post post) {
    return PostResponse.from(post.id, post.title, post.content, post.comments);
  }
}
