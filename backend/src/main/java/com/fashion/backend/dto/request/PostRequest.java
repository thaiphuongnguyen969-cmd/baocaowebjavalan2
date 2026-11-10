package com.fashion.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.LinkedHashMap;
import java.util.Map;

/** Dữ liệu bài viết và liên kết danh mục/tác giả. */
public record PostRequest(@NotNull @Positive Integer categoryId, @NotNull @Positive Integer userId,
                          @NotBlank String title, @NotBlank String slug, String thumbnail,
                          String summary, @NotBlank String content, Integer isPublished) {
    public Map<String,Object> toMap() {
        Map<String,Object> body=new LinkedHashMap<>();
        body.put("categoryId",categoryId); body.put("userId",userId); body.put("title",title);
        body.put("slug",slug); body.put("thumbnail",thumbnail); body.put("summary",summary);
        body.put("content",content); body.put("isPublished",isPublished);
        return body;
    }
}
