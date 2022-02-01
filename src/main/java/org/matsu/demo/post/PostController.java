package org.matsu.demo.post;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("posts")
public class PostController {

    @GET
    public List<Post> getAll() {
        return Post.getAll();
    }
}
