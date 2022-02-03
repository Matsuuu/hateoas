package org.matsu.demo.post;

import java.util.List;
import java.util.Optional;

import org.matsu.demo.comment.Comment;

public class Post {
  public long id;
  public String title;
  public String content;
  public List<Comment> comments;

  public Post(long id, String title, String content, List<Comment> comments) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.comments = comments;
  }

  public static List<PostResponse> getAll() {
      return getMockData().stream()
          .map(PostResponse::from)
          .toList();
  }

  public static Optional<PostResponse> byId(long postId) {
    return getMockData().stream()
        .filter(p -> p.id == postId)
        .map(PostResponse::from)
        .findFirst();
  }

  public static List<Post> getMockData() {
    return List.of(new Post(1, "Post one", "Lorem Ipsum",
                            List.of(new Comment(1, "Great video!", 1))),
                   new Post(2, "Post two", "Foo bar",
                            List.of(new Comment(7, "Great video!", 1),
                                    new Comment(5, "Great video! !!!", 2),
                                    new Comment(6, "Great video dude!", 6))),
                   new Post(3, "Post one", "Bazzzz",
                            List.of(new Comment(2, "FOOBAR", 1))));
  }
}
