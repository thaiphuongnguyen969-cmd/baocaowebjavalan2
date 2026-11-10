package com.fashion.backend.repository;

import com.fashion.backend.entity.*;
import com.fashion.backend.exception.ResourceNotFoundException;
import com.fashion.backend.exception.BadRequestException;
import com.fashion.backend.repository.jpa.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class JpaContentRepositoryAdapter implements ContentRepository {
    private final PostCategoryJpaRepository postCategoriesJpa;
    private final PostJpaRepository postsJpa;
    private final PostCommentJpaRepository commentsJpa;
    private final ReviewJpaRepository reviewsJpa;
    private final ReviewImageJpaRepository reviewImagesJpa;
    private final ReviewReplyJpaRepository repliesJpa;
    private final UserJpaRepository usersJpa;
    private final ProductJpaRepository productsJpa;

    public JpaContentRepositoryAdapter(PostCategoryJpaRepository postCategoriesJpa,
                                 PostJpaRepository postsJpa, PostCommentJpaRepository commentsJpa,
                                 ReviewJpaRepository reviewsJpa, ReviewImageJpaRepository reviewImagesJpa,
                                 ReviewReplyJpaRepository repliesJpa,
                                 UserJpaRepository usersJpa, ProductJpaRepository productsJpa) {
        this.postCategoriesJpa = postCategoriesJpa;
        this.postsJpa = postsJpa;
        this.commentsJpa = commentsJpa;
        this.reviewsJpa = reviewsJpa;
        this.reviewImagesJpa = reviewImagesJpa;
        this.repliesJpa = repliesJpa;
        this.usersJpa = usersJpa;
        this.productsJpa = productsJpa;
    }

    @Override public List<Map<String,Object>> postCategories() {
        return postCategoriesJpa.findAllByOrderByIdAsc().stream().map(this::postCategoryRow).toList();
    }
    @Override public List<Map<String,Object>> posts(int size, int offset) {
        return postsJpa.findByPublishedTrue(PageRequest.of(offset/size,size,Sort.by("createdAt").descending()))
            .getContent().stream().map(this::postRow).toList();
    }
    @Override public Map<String,Object> post(String slug) {
        return postRow(postsJpa.findBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết")));
    }
    @Override public void incrementPostViews(String slug) {
        postsJpa.findBySlug(slug).ifPresent(post -> {
            post.views++;
            postsJpa.save(post);
        });
    }
    @Override public List<Map<String,Object>> postComments(int postId) {
        return commentsJpa.findByPostIdOrderByCreatedAtDesc(postId).stream().map(this::commentRow).toList();
    }
    @Override public int createPost(Map<String,Object> b) {
        Post post = new Post();
        post.category = postCategoriesJpa.findById(number(b,"categoryId"))
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục bài viết"));
        post.author = usersJpa.findById(number(b,"userId"))
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tác giả"));
        setPost(post,b);
        return postsJpa.save(post).id;
    }
    @Override public List<Map<String,Object>> adminPosts() {
        return postsJpa.findAllByOrderByIdDesc().stream().map(this::postRow).toList();
    }
    @Override public void updatePost(int id, Map<String,Object> b) {
        Post post = postsJpa.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết"));
        post.category = postCategoriesJpa.findById(number(b,"categoryId"))
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục bài viết"));
        post.author = usersJpa.findById(number(b,"userId"))
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tác giả"));
        setPost(post,b);
        postsJpa.save(post);
    }
    @Override public void deletePost(int id) { postsJpa.deleteById(id); }
    @Override public void createComment(int postId, Map<String,Object> b) {
        PostComment comment=new PostComment();
        comment.post=postsJpa.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết"));
        comment.user=usersJpa.findById(number(b,"userId")).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        comment.parent=optionalId(b,"parentId")==null?null:commentsJpa.findById(optionalId(b,"parentId"))
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bình luận cha"));
        comment.content=String.valueOf(b.get("content"));
        commentsJpa.save(comment);
    }
    @Override public List<Map<String,Object>> reviews(int productId) {
        return reviewsJpa.findByProductIdOrderByCreatedAtDesc(productId).stream().map(review -> reviewRow(review,false)).toList();
    }
    @Override public List<Map<String,Object>> reviewImages(int reviewId) {
        return reviewImagesJpa.findByReviewId(reviewId).stream().map(this::reviewImageRow).toList();
    }
    @Override public int createReviewImage(int reviewId,String imageUrl) {
        ReviewImage image=new ReviewImage();
        image.review=reviewsJpa.findById(reviewId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đánh giá"));
        image.imageUrl=imageUrl;
        return reviewImagesJpa.save(image).id;
    }
    @Override public void updateReviewImage(int id,String imageUrl) {
        ReviewImage image=reviewImagesJpa.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ảnh đánh giá"));
        image.imageUrl=imageUrl;
        reviewImagesJpa.save(image);
    }
    @Override public void deleteReviewImage(int id) { reviewImagesJpa.deleteById(id); }
    @Override public int createReview(Map<String,Object> b) {
        Review review=new Review();
        review.user=usersJpa.findById(number(b,"userId")).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        review.product=productsJpa.findById(number(b,"productId")).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
        review.rating=number(b,"rating"); review.comment=(String)b.get("comment");
        return reviewsJpa.save(review).id;
    }
    @Override public List<Map<String,Object>> adminReviews() {
        return reviewsJpa.findAllByOrderByCreatedAtDesc().stream().map(review -> reviewRow(review,true)).toList();
    }
    @Override public void replyReview(int id, Map<String,Object> b) {
        ReviewReply reply=repliesJpa.findByReviewId(id).orElseGet(ReviewReply::new);
        reply.review=reviewsJpa.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đánh giá"));
        reply.user=usersJpa.findById(number(b,"userId")).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        reply.comment=String.valueOf(b.get("comment"));
        repliesJpa.save(reply);
    }
    @Override public List<Map<String,Object>> adminReviewReplies() {
        return repliesJpa.findAllByOrderByIdDesc().stream().map(this::reviewReplyRow).toList();
    }
    @Override public Map<String,Object> reviewReply(int id) {
        return reviewReplyRow(repliesJpa.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phản hồi đánh giá")));
    }
    @Override public void updateReviewReply(int id,String comment) {
        ReviewReply reply=repliesJpa.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phản hồi đánh giá"));
        reply.comment=comment;
        repliesJpa.save(reply);
    }
    @Override public void deleteReviewReply(int id) {
        if (!repliesJpa.existsById(id)) throw new ResourceNotFoundException("Không tìm thấy phản hồi đánh giá");
        repliesJpa.deleteById(id);
    }
    @Override public void updateReview(int id, Map<String,Object> b) {
        Review review=reviewsJpa.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đánh giá"));
        review.user=usersJpa.findById(number(b,"userId")).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        review.product=productsJpa.findById(number(b,"productId")).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
        review.rating=number(b,"rating"); review.comment=(String)b.get("comment");
        reviewsJpa.save(review);
    }
    @Override public void deleteReview(int id) { reviewsJpa.deleteById(id); }
    @Override public List<Map<String,Object>> adminPostCategories() {
        return postCategoriesJpa.findAllByOrderByIdDesc().stream().map(this::postCategoryRow).toList();
    }
    @Override public int createPostCategory(Map<String,Object> b) {
        PostCategory category=new PostCategory();
        category.name=String.valueOf(b.get("name")); category.slug=String.valueOf(b.get("slug"));
        category.description=(String)b.get("description");
        return postCategoriesJpa.save(category).id;
    }
    @Override public void updatePostCategory(int id, Map<String,Object> b) {
        PostCategory category=postCategoriesJpa.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục bài viết"));
        category.name=String.valueOf(b.get("name")); category.slug=String.valueOf(b.get("slug"));
        category.description=(String)b.get("description"); postCategoriesJpa.save(category);
    }
    @Override public void deletePostCategory(int id) { postCategoriesJpa.deleteById(id); }
    @Override public List<Map<String,Object>> adminPostComments() {
        return commentsJpa.findAllByOrderByCreatedAtDesc().stream().map(this::commentRow).toList();
    }
    @Override public int createPostComment(Map<String,Object> b) {
        PostComment comment=new PostComment();
        comment.post=postsJpa.findById(number(b,"postId")).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết"));
        comment.user=usersJpa.findById(number(b,"userId")).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        comment.parent=optionalId(b,"parentId")==null?null:commentsJpa.findById(optionalId(b,"parentId"))
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bình luận cha"));
        comment.content=String.valueOf(b.get("content"));
        return commentsJpa.save(comment).id;
    }
    @Override public void updatePostComment(int id, Map<String,Object> b) {
        PostComment comment=commentsJpa.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bình luận"));
        comment.content=String.valueOf(b.get("content")); commentsJpa.save(comment);
    }
    @Override public void deletePostComment(int id) { commentsJpa.deleteById(id); }

    private int number(Map<String,Object> body,String key) {
        Object value=body.get(key);
        if(value==null) throw new BadRequestException("Thiếu trường "+key);
        try { return Integer.parseInt(value.toString()); }
        catch(NumberFormatException ex) { throw new BadRequestException("Trường "+key+" phải là số nguyên"); }
    }
    private Integer optionalId(Map<String,Object> body,String key) {
        Object value=body.get(key);
        return value==null||value.toString().isBlank()?null:Integer.valueOf(value.toString());
    }
    private void setPost(Post post,Map<String,Object> body) {
        post.title=String.valueOf(body.get("title")); post.slug=String.valueOf(body.get("slug"));
        post.thumbnail=(String)body.get("thumbnail"); post.summary=(String)body.get("summary");
        post.content=String.valueOf(body.get("content"));
        Object published=body.get("isPublished");
        post.published=published==null||"1".equals(published.toString())||"true".equalsIgnoreCase(published.toString());
    }
    private Map<String,Object> postCategoryRow(PostCategory category) {
        Map<String,Object> row=new java.util.LinkedHashMap<>();
        row.put("id",category.id); row.put("name",category.name); row.put("slug",category.slug);
        row.put("description",category.description); row.put("created_at",category.createdAt);
        return row;
    }

    private Map<String,Object> postRow(Post post) {
        Map<String,Object> row=new java.util.LinkedHashMap<>();
        row.put("id",post.id); row.put("category_id",post.category.id);
        row.put("user_id",post.author.id); row.put("title",post.title); row.put("slug",post.slug);
        row.put("thumbnail",post.thumbnail); row.put("summary",post.summary); row.put("content",post.content);
        row.put("views",post.views); row.put("is_published",post.published);
        row.put("created_at",post.createdAt); row.put("updated_at",post.updatedAt);
        row.put("category_name",post.category.name); row.put("author",post.author.fullname);
        return row;
    }

    private Map<String,Object> commentRow(PostComment comment) {
        Map<String,Object> row=new java.util.LinkedHashMap<>();
        row.put("id",comment.id); row.put("post_id",comment.post.id); row.put("user_id",comment.user.id);
        row.put("parent_id",comment.parent==null?null:comment.parent.id); row.put("content",comment.content);
        row.put("created_at",comment.createdAt); row.put("fullname",comment.user.fullname);
        row.put("post_title",comment.post.title);
        return row;
    }

    private Map<String,Object> reviewRow(Review review,boolean admin) {
        Map<String,Object> row=new java.util.LinkedHashMap<>();
        row.put("id",review.id); row.put("user_id",review.user.id); row.put("product_id",review.product.id);
        row.put("rating",review.rating); row.put("comment",review.comment); row.put("created_at",review.createdAt);
        row.put("fullname",review.user.fullname);
        if(admin) row.put("product_name",review.product.name);
        return row;
    }

    private Map<String,Object> reviewImageRow(ReviewImage image) {
        Map<String,Object> row=new java.util.LinkedHashMap<>();
        row.put("id",image.id); row.put("review_id",image.review.id); row.put("image_url",image.imageUrl);
        row.put("created_at",image.createdAt);
        return row;
    }

    private Map<String,Object> reviewReplyRow(ReviewReply reply) {
        Map<String,Object> row=new java.util.LinkedHashMap<>();
        row.put("id",reply.id); row.put("review_id",reply.review.id); row.put("user_id",reply.user.id);
        row.put("comment",reply.comment); row.put("created_at",reply.createdAt);
        row.put("fullname",reply.user.fullname); row.put("product_name",reply.review.product.name);
        return row;
    }

}
