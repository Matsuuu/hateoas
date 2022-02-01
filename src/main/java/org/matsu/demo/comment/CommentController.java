package org.matsu.demo.comment;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("comments")
public class CommentController {


    @GET
    public List<CommentResponse> getAll() {
        return Comment.getAll();
    }

    @GET
    @PathParam("{commentId}")
    public CommentResponse byId(@PathParam("commentId") long commentId) {
        return Comment.byId(commentId)
            .orElseThrow(NotFoundException::new);
    }

    @POST
    public void add(Comment comment) {
        System.out.println("Adding comment");
    }

    @PATCH
    public void update(Comment comment) {
        System.out.println("Adding comment");
    }

    @DELETE
    @Path("{commentId}")
    public void delete(@PathParam("commentId") long commentId) {
        System.out.println("Deleting post" + commentId);
    }
}
