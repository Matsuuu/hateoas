package org.matsu.demo.comment;

import java.util.List;
import java.util.Optional;

public class Comment {
  public long id;
  public String content;
  public int sender;

  public Comment(long id, String content, int sender) {
    this.id = id;
    this.content = content;
    this.sender = sender;
  }

  public static List<CommentResponse> getAll() {
    return getMockData().stream().map(CommentResponse::from).toList();
  }

  public static List<Comment> getMockData() {
    return List.of(
        new Comment(1, "Great video!", 1), new Comment(7, "Great video!", 1),
        new Comment(5, "Great video! !!!", 2),
        new Comment(6, "Great video dude!", 6), new Comment(2, "FOOBAR", 1));
  }

public static Optional<CommentResponse> byId(long commentId) {
    return getMockData().stream()
        .filter(c -> c.id == commentId)
        .map(CommentResponse::from)
        .findFirst();
}
}
