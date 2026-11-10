package com.fashion.backend.controller;

import com.fashion.backend.service.ContentService;
import com.fashion.backend.dto.request.PostCommentRequest;
import com.fashion.backend.dto.request.PostRequest;
import com.fashion.backend.dto.request.ReviewRequest;
import com.fashion.backend.dto.request.ReviewImageRequest;
import com.fashion.backend.dto.request.ReviewReplyRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:8090", "http://127.0.0.1:8090"})
public class ContentController {
    private final ContentService service;

    public ContentController(ContentService service) { this.service = service; }

    @GetMapping("/post-categories")
    public List<Map<String,Object>> postCategories() { return service.postCategories(); }

    @GetMapping("/posts")
    public List<Map<String,Object>> posts(@RequestParam(defaultValue="1") int page,
                                          @RequestParam(defaultValue="10") int size) {
        return service.posts(page,size);
    }

    @GetMapping("/posts/{slug}")
    public Map<String,Object> post(@PathVariable String slug) { return service.post(slug); }

    @PostMapping("/admin/posts")
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest request) {
        int id=service.createPost(request.toMap());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Tạo bài viết thành công","id",id));
    }

    @GetMapping("/admin/posts")
    public List<Map<String,Object>> adminPosts() { return service.adminPosts(); }

    @PutMapping("/admin/posts/{id}")
    public ResponseEntity<?> updatePost(@PathVariable int id,@Valid @RequestBody PostRequest request) {
        service.updatePost(id,request.toMap());
        return ResponseEntity.ok(Map.of("message","Cập nhật bài viết thành công"));
    }

    @DeleteMapping("/admin/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id) {
        service.deletePost(id);
        return ResponseEntity.ok(Map.of("message","Đã xóa bài viết"));
    }

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<?> comment(@PathVariable int id,@Valid @RequestBody PostCommentRequest request) {
        service.createComment(id,request.toMap());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Đã gửi bình luận"));
    }

    @GetMapping("/reviews/{productId}")
    public List<Map<String,Object>> reviews(@PathVariable int productId) { return service.reviews(productId); }
    @GetMapping("/reviews/{reviewId}/images")
    public List<Map<String,Object>> reviewImages(@PathVariable int reviewId) { return service.reviewImages(reviewId); }
    @PostMapping("/admin/reviews/{reviewId}/images")
    public ResponseEntity<?> createReviewImage(@PathVariable int reviewId,@Valid @RequestBody ReviewImageRequest request) {
        int id=service.createReviewImage(reviewId,request.imageUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Đã thêm ảnh đánh giá","id",id));
    }
    @PutMapping("/admin/review-images/{id}")
    public ResponseEntity<?> updateReviewImage(@PathVariable int id,@Valid @RequestBody ReviewImageRequest request) {
        service.updateReviewImage(id,request.imageUrl());
        return ResponseEntity.ok(Map.of("message","Đã cập nhật ảnh đánh giá"));
    }
    @DeleteMapping("/admin/review-images/{id}")
    public ResponseEntity<?> deleteReviewImage(@PathVariable int id) {
        service.deleteReviewImage(id);
        return ResponseEntity.ok(Map.of("message","Đã xóa ảnh đánh giá"));
    }

    @PostMapping("/reviews")
    public ResponseEntity<?> review(@Valid @RequestBody ReviewRequest request) {
        service.createReview(request.toMap());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Cảm ơn bạn đã đánh giá"));
    }

    @PostMapping("/admin/reviews")
    public ResponseEntity<?> createReview(@Valid @RequestBody ReviewRequest request) {
        service.createReview(request.toMap());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Đã thêm đánh giá"));
    }

    @GetMapping("/admin/reviews")
    public List<Map<String,Object>> adminReviews() { return service.adminReviews(); }

    @GetMapping("/admin/review-replies")
    public List<Map<String,Object>> adminReviewReplies() { return service.adminReviewReplies(); }

    @GetMapping("/admin/review-replies/{id}")
    public Map<String,Object> reviewReply(@PathVariable int id) { return service.reviewReply(id); }

    @PutMapping("/admin/review-replies/{id}")
    public ResponseEntity<?> updateReviewReply(@PathVariable int id,@Valid @RequestBody ReviewReplyRequest request) {
        service.updateReviewReply(id,request.comment());
        return ResponseEntity.ok(Map.of("message","Cập nhật phản hồi đánh giá thành công"));
    }

    @DeleteMapping("/admin/review-replies/{id}")
    public ResponseEntity<?> deleteReviewReply(@PathVariable int id) {
        service.deleteReviewReply(id);
        return ResponseEntity.ok(Map.of("message","Đã xóa phản hồi đánh giá"));
    }

    @PostMapping("/admin/reviews/{reviewId}/reply")
    public ResponseEntity<?> replyReview(@PathVariable int reviewId,@RequestBody Map<String,Object> body) {
        service.replyReview(reviewId,body);
        return ResponseEntity.ok(Map.of("message","Đã phản hồi đánh giá"));
    }

    @PutMapping("/admin/reviews/{id}")
    public ResponseEntity<?> updateReview(@PathVariable int id,@Valid @RequestBody ReviewRequest request) {
        service.updateReview(id,request.toMap());
        return ResponseEntity.ok(Map.of("message","Cập nhật đánh giá thành công"));
    }

    @DeleteMapping("/admin/reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable int id) {
        service.deleteReview(id);
        return ResponseEntity.ok(Map.of("message","Đã xóa đánh giá"));
    }

    @GetMapping("/admin/post-categories")
    public List<Map<String,Object>> adminPostCategories() { return service.adminPostCategories(); }

    @PostMapping("/admin/post-categories")
    public ResponseEntity<?> createPostCategory(@RequestBody Map<String,Object> body) {
        int id=service.createPostCategory(body);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(Map.of("message","Tạo danh mục bài viết thành công","id",id));
    }

    @PutMapping("/admin/post-categories/{id}")
    public ResponseEntity<?> updatePostCategory(@PathVariable int id,@RequestBody Map<String,Object> body) {
        service.updatePostCategory(id,body);
        return ResponseEntity.ok(Map.of("message","Cập nhật danh mục bài viết thành công"));
    }

    @DeleteMapping("/admin/post-categories/{id}")
    public ResponseEntity<?> deletePostCategory(@PathVariable int id) {
        service.deletePostCategory(id);
        return ResponseEntity.ok(Map.of("message","Đã xóa danh mục bài viết"));
    }

    @GetMapping("/admin/post-comments")
    public List<Map<String,Object>> adminPostComments() { return service.adminPostComments(); }

    @PostMapping("/admin/post-comments")
    public ResponseEntity<?> createPostComment(@RequestBody Map<String,Object> body) {
        service.createPostComment(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Đã thêm bình luận"));
    }

    @PutMapping("/admin/post-comments/{id}")
    public ResponseEntity<?> updatePostComment(@PathVariable int id,@RequestBody Map<String,Object> body) {
        service.updatePostComment(id,body);
        return ResponseEntity.ok(Map.of("message","Cập nhật bình luận thành công"));
    }

    @DeleteMapping("/admin/post-comments/{id}")
    public ResponseEntity<?> deletePostComment(@PathVariable int id) {
        service.deletePostComment(id);
        return ResponseEntity.ok(Map.of("message","Đã xóa bình luận"));
    }
}
