# FashionShop - Website bán quần áo

Dự án gồm backend REST API và frontend Spring Boot + Thymeleaf. Frontend chạy ở cổng `8090`, backend chạy ở cổng `8080`.

## Yêu cầu

- JDK 25
- MySQL 8+ hoặc MariaDB (XAMPP)
- Maven Wrapper có sẵn trong từng module

## Cài đặt cơ sở dữ liệu

1. Khởi động MySQL và tạo database `fashion_shop`.
2. Import `database.sql`, sau đó import `seed.sql`.
3. Backend mặc định kết nối `jdbc:mysql://localhost:3306/fashion_shop` với username `root` và password rỗng. Sửa `backend/src/main/resources/application.properties` nếu MySQL của bạn dùng cấu hình khác.
4. Hibernate dùng `spring.jpa.hibernate.ddl-auto=none`; ứng dụng không tự tạo hoặc sửa schema. Hãy import SQL trước khi chạy.

## Chạy ứng dụng

Mở hai terminal riêng:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

```powershell
cd frontend
.\mvnw.cmd spring-boot:run
```

- Backend: `http://localhost:8080`
- Website: `http://localhost:8090`

Kiểm tra backend:

```powershell
cd backend
.\mvnw.cmd clean test
```

Postman collection: `FashionShop_Postman_Collection.json` (đặt `baseUrl`, đăng nhập lấy JWT, sau đó dùng token cho API cần xác thực).

## Tài khoản mẫu

Sau khi import `seed.sql`:

- Admin: `admin@fashion.vn` / `password`
- Khách hàng: `anh.nguyen@gmail.com` / `password`

Mật khẩu lưu trong database dạng BCrypt. Nếu tài khoản không đăng nhập được, hãy xác nhận đã import seed và kiểm tra bản ghi `users` trong database hiện tại.

## Backend

Mã nguồn nằm trong `backend/src/main/java/com/fashion/backend`:

```text
config/        Security, JWT và CORS
controller/    REST endpoints, validation request và HTTP response
dto/request/   DTO đầu vào và Jakarta Validation
dto/response/  DTO response dùng chung và kết quả xác thực
entity/        JPA entity ánh xạ schema SQL
exception/     Exception nghiệp vụ và REST exception handler
repository/jpa Spring Data JPA repositories
repository/    Adapter truy cập dữ liệu cho nghiệp vụ
security/      Bộ lọc và tiện ích JWT
service/       Interface nghiệp vụ
service/impl/  Xử lý nghiệp vụ và transaction
```

Các entity ánh xạ 15 bảng trong schema: `users`, `categories`, `products`, `product_variants`, `carts`, `cart_items`, `orders`, `order_items`, `coupons`, `reviews`, `review_images`, `review_replies`, `post_categories`, `posts`, `post_comments`. CORS cho phép frontend local ở `localhost:8090` và `127.0.0.1:8090`. API `/api/admin/**` yêu cầu JWT có role admin; API auth và các endpoint đọc công khai không yêu cầu đăng nhập.

## API

### Xác thực và hồ sơ

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/users/me`, `PUT /api/users/me`
- `PUT /api/users/me/password`

### Danh mục, sản phẩm và biến thể

- `GET /api/categories`, `GET /api/categories/{id}/children`
- `GET /api/admin/categories/{id}`
- `POST|PUT|DELETE /api/admin/categories[/{id}]`
- `GET /api/products?search=&categoryId=&minPrice=&maxPrice=&page=&size=`
- `GET /api/products/{id}`, `GET /api/products/slug/{slug}`
- `GET /api/admin/products`, `POST /api/admin/products`
- `PUT|DELETE /api/admin/products/{id}`
- `GET /api/admin/variants`, `POST /api/admin/variants`
- `PUT|DELETE /api/admin/variants/{id}`

### Giỏ hàng, mã giảm giá và đơn hàng

- `GET /api/coupons`, CRUD admin tại `/api/admin/coupons`
- `POST /api/cart/items`, `GET /api/cart/{sessionId}`
- `PUT /api/cart/items/{id}`, `DELETE /api/cart/items/{id}?sessionId={sessionId}`
- `POST /api/orders/checkout`, `GET /api/orders/track/{code}`
- `GET /api/orders/my`
- `GET /api/admin/orders`, `GET|DELETE /api/admin/orders/{id}`
- `PATCH /api/admin/orders/{id}/status`

### Tài khoản admin

- `GET|POST /api/admin/users`
- `PUT|DELETE /api/admin/users/{id}`
- `GET /api/admin/dashboard`

### Bài viết và đánh giá

- `GET /api/post-categories`, CRUD admin tại `/api/admin/post-categories`
- `GET /api/posts`, `GET /api/posts/{slug}`
- `GET /api/admin/posts`, `POST /api/admin/posts`
- `PUT|DELETE /api/admin/posts/{id}`
- `POST /api/posts/{id}/comments`
- `GET /api/admin/post-comments`, CRUD admin tại `/api/admin/post-comments[/{id}]`
- `GET /api/reviews/{productId}`, `POST /api/reviews`
- `GET /api/reviews/{reviewId}/images`
- CRUD admin reviews tại `/api/admin/reviews[/{id}]`, phản hồi tại `/api/admin/reviews/{id}/reply`
- CRUD admin phản hồi đánh giá tại `/api/admin/review-replies[/{id}]`
- `POST /api/admin/reviews/{reviewId}/images`
- `PUT|DELETE /api/admin/review-images/{id}`
