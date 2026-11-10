package com.fashion.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.LinkedHashMap;
import java.util.Map;

/** Dữ liệu bình luận bài viết. */
public record PostCommentRequest(Integer postId, @NotNull @Positive Integer userId,
                                 Integer parentId, @NotBlank String content) {
    public Map<String,Object> toMap() {
        Map<String,Object> body=new LinkedHashMap<>();
        body.put("postId",postId); body.put("userId",userId); body.put("parentId",parentId);
        body.put("content",content);
        return body;
    }
}
