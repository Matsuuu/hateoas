package org.matsu.demo.comment;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("comments")
public class CommentController {


    @GET
    public List<Comment> getAll() {
        return Comment.getAll();
    }
}
