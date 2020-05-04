package com.fairy_pitt.recordary.endpoint.comment.dto;

import com.fairy_pitt.recordary.common.entity.CommentEntity;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long commentCd;
    private Long commentUserFK;
    private Long commentPostFK;
    private String content;
    private Long commentOriginFK;

    public CommentResponseDto(CommentEntity comment)
    {
        this.commentCd = comment.getCommentCd();
        this.commentOriginFK = comment.getCommentOriginFK().getCommentCd();
        this.content = comment.getContent();
        this.commentPostFK = comment.getCommentPostFK().getPostCd();
        this.commentUserFK = comment.getCommentUserFK().getUserCd();
       // this.commentOriginFK = commentOriginFK;
    }

    
}