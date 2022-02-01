package org.matsu.demo.comment;

import java.util.List;

public class Comment {
  public long id;
  public String content;
  public int sender;

  public Comment(long id, String content, int sender) {
    this.id = id;
    this.content = content;
    this.sender = sender;
  }

  public static List<Comment> getAll() {
    return List.of(new Comment(1, "Great video!", 1),
                   new Comment(5, "Great video! !!!", 2),
                   new Comment(6, "Great video dude!", 6));
  }
}
