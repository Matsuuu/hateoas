package org.matsu.demo.post;

import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("posts")
public class PostController {

  @GET
  public List<PostResponse> getAll() {
    return Post.getAll();
  }

  @GET
  @Path("{postId}")
  public PostResponse byId(@PathParam("postId") long postId) {
    return Post.byId(postId).orElseThrow(NotFoundException::new);
  }

  @POST
  public void add(Post post) {
    System.out.println("Adding post");
  }

  @PATCH
  public void update(Post post) {
    System.out.println("Adding post");
  }

  @DELETE
  @Path("{postId}")
  public void delete(@PathParam("postId") long postId) {
    System.out.println("Deleting post" + postId);
  }
}
