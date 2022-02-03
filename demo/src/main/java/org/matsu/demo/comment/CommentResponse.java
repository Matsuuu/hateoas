package org.matsu.demo.comment;

public record CommentResponse(long id, String content, int sender) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.id, comment.content, comment.sender);
    }
}
