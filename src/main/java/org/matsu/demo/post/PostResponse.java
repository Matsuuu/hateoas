package org.matsu.demo.post;

import java.util.List;
import org.matsu.demo.comment.Comment;
import org.matsu.hateoas.links.HalLink;

public record PostResponse(long id, String title, String content,
                           List<Comment> comments, List<HalLink> links) {


  public static List<HalLink> generateLinks() {
    return List.of(); 
  }

  public static PostResponse from(long id, String title, String content,
                      List<Comment> comments) {
    return new PostResponse(id, title, content, comments, generateLinks());
  }

  public static PostResponse from(Post post) {
    return PostResponse.from(post.id, post.title, post.content, post.comments);
  }
}
