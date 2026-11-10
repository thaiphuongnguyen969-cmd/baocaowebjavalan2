package com.fashion.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.LinkedHashMap;
import java.util.Map;

/** Dữ liệu tạo hoặc cập nhật danh mục sản phẩm. */
public record CategoryRequest(@NotBlank String name, @NotBlank String slug,
                              Integer parentId, String description) {
    public Map<String,Object> toMap() {
        Map<String,Object> body=new LinkedHashMap<>();
        body.put("name",name); body.put("slug",slug); body.put("parentId",parentId);
        body.put("description",description);
        return body;
    }
}
