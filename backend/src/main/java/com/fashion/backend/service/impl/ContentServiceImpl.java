package com.fashion.backend.service.impl;

import com.fashion.backend.exception.BadRequestException;
import com.fashion.backend.exception.ResourceNotFoundException;
import com.fashion.backend.repository.ContentRepository;
import com.fashion.backend.service.ContentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ContentServiceImpl implements ContentService {
    private final ContentRepository repository;
    public ContentServiceImpl(ContentRepository repository) { this.repository = repository; }

    @Override public List<Map<String,Object>> postCategories() { return repository.postCategories(); }
    @Override public List<Map<String,Object>> posts(int page,int size) {
        if (page < 1 || size < 1 || size > 100) throw new BadRequestException("page phải >= 1, size phải từ 1 đến 100");
        return repository.posts(size,(page-1)*size);
    }
    @Override public Map<String,Object> post(String slug) {
        Map<String,Object> result;
        try { result=repository.post(slug); }
        catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Không tìm thấy bài viết");
        }
        repository.incrementPostViews(slug);
        result.put("comments",repository.postComments(((Number)result.get("id")).intValue()));
        return result;
    }
    @Override public int createPost(Map<String,Object> b) { return repository.createPost(b); }
    @Override public List<Map<String,Object>> adminPosts() { return repository.adminPosts(); }
    @Override public void updatePost(int id,Map<String,Object> b) { repository.updatePost(id,b); }
    @Override public void deletePost(int id) { repository.deletePost(id); }
    @Override public void createComment(int postId,Map<String,Object> b) { repository.createComment(postId,b); }
    @Override public List<Map<String,Object>> reviews(int productId) { return repository.reviews(productId); }
    @Override public List<Map<String,Object>> reviewImages(int reviewId) { return repository.reviewImages(reviewId); }
    @Override public int createReviewImage(int reviewId,String imageUrl) {
        if(imageUrl==null||imageUrl.isBlank()) throw new BadRequestException("Thiếu trường imageUrl");
        return repository.createReviewImage(reviewId,imageUrl.trim());
    }
    @Override public void updateReviewImage(int id,String imageUrl) {
        if(imageUrl==null||imageUrl.isBlank()) throw new BadRequestException("Thiếu trường imageUrl");
        repository.updateReviewImage(id,imageUrl.trim());
    }
    @Override public void deleteReviewImage(int id) { repository.deleteReviewImage(id); }
    @Override public int createReview(Map<String,Object> b) {
        Object rating=b.get("rating");
        if (rating == null) throw new BadRequestException("Thiếu trường rating");
        try {
            int value=Integer.parseInt(rating.toString());
            if (value<1 || value>5) throw new BadRequestException("rating phải từ 1 đến 5");
        } catch (NumberFormatException ex) { throw new BadRequestException("rating phải là số nguyên từ 1 đến 5"); }
        return repository.createReview(b);
    }
    @Override public List<Map<String,Object>> adminReviews() { return repository.adminReviews(); }
    @Override public void replyReview(int id,Map<String,Object> b) { repository.replyReview(id,b); }
    @Override public List<Map<String,Object>> adminReviewReplies() { return repository.adminReviewReplies(); }
    @Override public Map<String,Object> reviewReply(int id) { return repository.reviewReply(id); }
    @Override public void updateReviewReply(int id,String comment) { repository.updateReviewReply(id,comment); }
    @Override public void deleteReviewReply(int id) { repository.deleteReviewReply(id); }
    @Override public void updateReview(int id,Map<String,Object> b) { repository.updateReview(id,b); }
    @Override public void deleteReview(int id) { repository.deleteReview(id); }
    @Override public List<Map<String,Object>> adminPostCategories() { return repository.adminPostCategories(); }
    @Override public int createPostCategory(Map<String,Object> b) { return repository.createPostCategory(b); }
    @Override public void updatePostCategory(int id,Map<String,Object> b) { repository.updatePostCategory(id,b); }
    @Override public void deletePostCategory(int id) { repository.deletePostCategory(id); }
    @Override public List<Map<String,Object>> adminPostComments() { return repository.adminPostComments(); }
    @Override public int createPostComment(Map<String,Object> b) { return repository.createPostComment(b); }
    @Override public void updatePostComment(int id,Map<String,Object> b) { repository.updatePostComment(id,b); }
    @Override public void deletePostComment(int id) { repository.deletePostComment(id); }
}
