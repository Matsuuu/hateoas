package org.matsu.demo.post;

import java.util.List;
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

  public static List<Post> getAll() {
    return List.of(new Post(1, "Post one", "Lorem Ipsum",
                            List.of(new Comment(1, "Great video!", 1))),
                   new Post(2, "Post two", "Foo bar",
                            List.of(new Comment(1, "Great video!", 1),
                                    new Comment(5, "Great video! !!!", 2),
                                    new Comment(6, "Great video dude!", 6))),
                   new Post(3, "Post one", "Bazzzz",
                            List.of(new Comment(1, "FOOBAR", 1))));
  }
}
