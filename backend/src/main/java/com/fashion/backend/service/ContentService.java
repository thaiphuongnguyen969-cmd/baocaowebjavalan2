package com.fashion.backend.service;

import java.util.List;
import java.util.Map;

public interface ContentService {
    List<Map<String,Object>> postCategories();
    List<Map<String,Object>> posts(int page, int size);
    Map<String,Object> post(String slug);
    int createPost(Map<String,Object> body);
    List<Map<String,Object>> adminPosts();
    void updatePost(int id, Map<String,Object> body);
    void deletePost(int id);
    void createComment(int postId, Map<String,Object> body);
    List<Map<String,Object>> reviews(int productId);
    List<Map<String,Object>> reviewImages(int reviewId);
    int createReviewImage(int reviewId,String imageUrl);
    void updateReviewImage(int id,String imageUrl);
    void deleteReviewImage(int id);
    int createReview(Map<String,Object> body);
    List<Map<String,Object>> adminReviews();
    void replyReview(int id, Map<String,Object> body);
    List<Map<String,Object>> adminReviewReplies();
    Map<String,Object> reviewReply(int id);
    void updateReviewReply(int id, String comment);
    void deleteReviewReply(int id);
    void updateReview(int id, Map<String,Object> body);
    void deleteReview(int id);
    List<Map<String,Object>> adminPostCategories();
    int createPostCategory(Map<String,Object> body);
    void updatePostCategory(int id, Map<String,Object> body);
    void deletePostCategory(int id);
    List<Map<String,Object>> adminPostComments();
    int createPostComment(Map<String,Object> body);
    void updatePostComment(int id, Map<String,Object> body);
    void deletePostComment(int id);
}
