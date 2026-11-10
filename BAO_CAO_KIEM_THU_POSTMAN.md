# BÁO CÁO KIỂM THỬ API FASHION SHOP BẰNG POSTMAN

## 1. Thông tin tổng quan

| Mục | Thông tin |
|---|---|
| Ứng dụng | Fashion Shop |
| Kiến trúc | Backend Spring Boot REST API + MySQL + Frontend Thymeleaf |
| Backend | `http://localhost:8080` |
| Frontend | `http://localhost:8090` |
| Database | `fashion_shop` trên XAMPP/MySQL |
| Cổng MySQL | `3306` |
| Cơ chế xác thực | JWT HS256, thời hạn 24 giờ |
| Công cụ kiểm thử | Postman |
| Tài khoản Admin | `ducodwj` |
| Mật khẩu Admin | `password` |

## 2. Chuẩn bị môi trường

1. Khởi động Apache và MySQL trong XAMPP.
2. Import `database.sql` bằng phpMyAdmin hoặc MySQL Workbench.
3. Chạy backend:

```powershells
cd C:\Users\pc\Documents\java_fix\backend
.\mvnw.cmd spring-boot:run
```

4. Chạy frontend:

```powershell
cd C:\Users\pc\Documents\java_fix\frontend
.\mvnw.cmd spring-boot:run
```

5. Mở trang frontend:

```text
http://localhost:8090
```

6. Import collection vào Postman:

```text
FashionShop_Postman_Collection.json
```

## 3. Cấu hình Postman Environment hoặc Collection Variables

```text
baseUrl        = http://localhost:8080
adminEmail     = ducodwj
adminPassword  = password
token          = để trống lúc đầu
productId      = 1
variantId      = 1
categoryId     = 1
userId         = 2
couponId       = 1
orderId        = 1
orderCode      = lấy từ response checkout
postId         = 1
postCategoryId = 1
reviewId       = 1
sessionId      = postman-test-session
```

Mọi request admin gửi header:

```text
Authorization: Bearer {{token}}
Content-Type: application/json
```

## 4. Lấy JWT bằng Postman

### 4.1 Request

```text
POST {{baseUrl}}/api/auth/login
Content-Type: application/json
```

### 4.2 Body

```json
{
  "email": "ducodwj",
  "password": "password"
}
```

### 4.3 Response mong đợi

- HTTP status: `200 OK`
- JWT gồm `3 phần`, phân cách bởi dấu chấm.
- Response chứa `token`, `user.id`, `user.fullname`, `user.email`, `user.role`.

### 4.4 Lưu JWT vào biến Postman

Trong tab `Tests` của request đăng nhập:

```javascript
const response = pm.response.json();

pm.test("Dang nhap thanh cong", function () {
    pm.response.to.have.status(200);
    pm.expect(response.token).to.exist;
});

pm.test("Nhan JWT hop le", function () {
    pm.expect(response.token.split(".").length).to.eql(3);
});

pm.collectionVariables.set("token", response.token);
pm.collectionVariables.set("userId", String(response.user.id));
pm.collectionVariables.set("userRole", response.user.role);
```

Sau đó dùng Authorization Type `Bearer Token`, giá trị:

```text
{{token}}
```

Hoặc dùng header thủ công:

```text
Authorization: Bearer {{token}}
```

### 4.5 Kiểm tra JWT trên jwt.io

1. Sao chép giá trị `token` trong response.
2. Mở `https://jwt.io`.
3. Kiểm tra payload chứa:

```json
{
  "userId": 1,
  "role": "admin",
  "sub": "ducodwj"
}
```

4. Token phải có `iat` và `exp`; khoảng thời gian này là 24 giờ.

## 5. Đăng ký và đăng nhập tài khoản khách hàng

### 5.1 Đăng ký

```text
POST {{baseUrl}}/api/auth/register
```

```json
{
  "fullname": "Khách hàng kiểm thử",
  "email": "customer.test@test.vn",
  "password": "123456",
  "phone": "0909000999",
  "address": "Hà Nội"
}
```

Kết quả mong đợi:

- HTTP `201 Created`
- Lưu bản ghi mới vào bảng `users`.
- Mật khẩu được lưu bằng BCrypt.
- Vai trò mặc định là `customer`.
- Trạng thái mặc định là hoạt động.

Kiểm tra MySQL:

```sql
SELECT id, fullname, email, role, status, created_at
FROM users
WHERE email = 'customer.test@test.vn';
```

### 5.2 Đăng nhập tài khoản khách hàng

```json
{
  "email": "customer.test@test.vn",
  "password": "123456"
}
```

Kết quả mong đợi:

- HTTP `200 OK`
- Role là `customer`
- Nhận JWT hợp lệ.
- Sai mật khẩu hoặc tài khoản bị khóa trả về `401 Unauthorized`.

## 6. Danh mục API

### Nhóm A. Dashboard và người dùng

| STT | Chức năng | Method | Endpoint | Dữ liệu |
|---|---|---|---|---|
| 1 | Dashboard | GET | `/api/admin/dashboard` | Không |
| 2 | Danh sách người dùng | GET | `/api/admin/users` | JWT Admin |
| 3 | Thêm người dùng | POST | `/api/admin/users` | JSON user |
| 4 | Sửa người dùng | PUT | `/api/admin/users/{id}` | JSON user |
| 5 | Xóa người dùng | DELETE | `/api/admin/users/{id}` | Không |

Body thêm người dùng:

```json
{
  "fullname": "Người dùng Postman",
  "email": "postman.user@test.vn",
  "password": "123456",
  "phone": "0909000001",
  "address": "Đà Nẵng",
  "role": "customer",
  "status": 1
}
```

Body sửa người dùng:

```json
{
  "fullname": "Người dùng đã sửa",
  "email": "postman.user@test.vn",
  "password": "",
  "phone": "0909000002",
  "address": "Hải Phòng",
  "role": "customer",
  "status": 1
}
```

Để đổi mật khẩu, thay `password` bằng chuỗi ít nhất 6 ký tự.

### Nhóm B. Danh mục và sản phẩm

| STT | Chức năng | Method | Endpoint |
|---|---|---|---|
| 1 | Danh sách danh mục | GET | `/api/categories` |
| 2 | Chi tiết danh mục | GET | `/api/admin/categories/{id}` |
| 3 | Thêm danh mục | POST | `/api/admin/categories` |
| 4 | Sửa danh mục | PUT | `/api/admin/categories/{id}` |
| 5 | Xóa danh mục | DELETE | `/api/admin/categories/{id}` |
| 6 | Danh sách sản phẩm public | GET | `/api/products?page=1&size=12` |
| 7 | Chi tiết sản phẩm | GET | `/api/products/{id}` |
| 8 | Danh sách sản phẩm admin | GET | `/api/admin/products` |
| 9 | Thêm sản phẩm | POST | `/api/admin/products` |
| 10 | Sửa sản phẩm | PUT | `/api/admin/products/{id}` |
| 11 | Xóa sản phẩm | DELETE | `/api/admin/products/{id}` |

Body danh mục:

```json
{
  "name": "Danh mục Postman",
  "slug": "danh-muc-postman",
  "parentId": null,
  "description": "Dữ liệu tạo bằng Postman"
}
```

Body sản phẩm:

```json
{
  "categoryId": 1,
  "name": "Sản phẩm Postman",
  "slug": "san-pham-postman",
  "description": "Sản phẩm kiểm thử CRUD",
  "thumbnail": "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=900",
  "images": "[\"https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=900\"]",
  "isActive": 1
}
```

### Nhóm C. Biến thể sản phẩm

| STT | Chức năng | Method | Endpoint |
|---|---|---|---|
| 1 | Danh sách biến thể | GET | `/api/admin/variants` |
| 2 | Thêm biến thể | POST | `/api/admin/variants` |
| 3 | Sửa biến thể | PUT | `/api/admin/variants/{id}` |
| 4 | Xóa biến thể | DELETE | `/api/admin/variants/{id}` |

```json
{
  "productId": 1,
  "sku": "POSTMAN-M-01",
  "size": "M",
  "color": "Đen",
  "price": 350000,
  "salePrice": 299000,
  "stock": 20
}
```

### Nhóm D. Giỏ hàng, coupon và đơn hàng

| STT | Chức năng | Method | Endpoint |
|---|---|---|---|
| 1 | Danh sách coupon public | GET | `/api/coupons` |
| 2 | Danh sách coupon admin | GET | `/api/admin/coupons` |
| 3 | Thêm coupon | POST | `/api/admin/coupons` |
| 4 | Sửa coupon | PUT | `/api/admin/coupons/{id}` |
| 5 | Xóa coupon | DELETE | `/api/admin/coupons/{id}` |
| 6 | Thêm vào giỏ | POST | `/api/cart/items` |
| 7 | Xem giỏ | GET | `/api/cart/{sessionId}` |
| 8 | Xóa khỏi giỏ | DELETE | `/api/cart/items/{id}` |
| 9 | Checkout | POST | `/api/orders/checkout` |
| 10 | Danh sách đơn hàng | GET | `/api/admin/orders` |
| 11 | Chi tiết đơn hàng | GET | `/api/admin/orders/{id}` |
| 12 | Sửa trạng thái | PATCH | `/api/admin/orders/{id}/status` |
| 13 | Xóa đơn hàng | DELETE | `/api/admin/orders/{id}` |
| 14 | Tra cứu đơn hàng | GET | `/api/orders/track/{orderCode}` |

Body coupon:

```json
{
  "code": "POSTMAN10",
  "discountType": "percent",
  "discountValue": 10,
  "minOrderValue": 100000,
  "usageLimit": 100,
  "expiresAt": "2027-12-31 23:59:59"
}
```

Body thêm giỏ:

```json
{
  "sessionId": "postman-test-session",
  "variantId": 1,
  "quantity": 2
}
```

Body checkout:

```json
{
  "userId": null,
  "receiverName": "Khách hàng Postman",
  "receiverPhone": "0909000999",
  "receiverAddress": "Hà Nội",
  "paymentMethod": "COD",
  "couponCode": "WELCOME10",
  "note": "Đơn kiểm thử Postman",
  "items": [
    {
      "variantId": 1,
      "quantity": 1
    }
  ]
}
```

Body cập nhật đơn:

```json
{
  "orderStatus": "confirmed",
  "paymentStatus": "paid"
}
```

Giá trị `orderStatus` hợp lệ:

```text
pending | confirmed | shipping | completed | cancelled
```

Giá trị `paymentStatus` hợp lệ:

```text
pending | paid | refunded
```

### Nhóm E. Nội dung, bình luận và đánh giá

| STT | Chức năng | Method | Endpoint |
|---|---|---|---|
| 1 | Danh mục bài viết public | GET | `/api/post-categories` |
| 2 | Danh sách bài viết | GET | `/api/posts?page=1&size=10` |
| 3 | Chi tiết bài viết | GET | `/api/posts/{slug}` |
| 4 | Thêm/sửa/xóa bài viết | POST/PUT/DELETE | `/api/admin/posts` |
| 5 | Danh sách bài viết admin | GET | `/api/admin/posts` |
| 6 | Thêm bình luận bài viết | POST | `/api/posts/{postId}/comments` |
| 7 | Danh mục bài viết admin | GET/POST/PUT/DELETE | `/api/admin/post-categories...` |
| 8 | Bình luận bài viết admin | GET/POST/PUT/DELETE | `/api/admin/post-comments...` |
| 9 | Đánh giá public | GET | `/api/reviews/{productId}` |
| 10 | Đánh giá admin | GET/POST/PUT/DELETE | `/api/admin/reviews...` |
| 11 | Phản hồi đánh giá | POST | `/api/admin/reviews/{reviewId}/reply` |

Body thêm bài viết:

```json
{
  "categoryId": 1,
  "userId": 1,
  "title": "Bài viết Postman",
  "slug": "bai-viet-postman",
  "thumbnail": "https://images.unsplash.com/photo-1445205170230-053b83016050?w=900",
  "summary": "Tóm tắt bài viết",
  "content": "Nội dung chi tiết bài viết kiểm thử.",
  "isPublished": 1
}
```

Body bình luận:

```json
{
  "userId": 2,
  "parentId": null,
  "content": "Bình luận kiểm thử Postman"
}
```

Body đánh giá:

```json
{
  "userId": 2,
  "productId": 1,
  "rating": 5,
  "comment": "Sản phẩm đẹp, giao hàng nhanh"
}
```

Body phản hồi đánh giá:

```json
{
  "userId": 1,
  "comment": "Cảm ơn bạn đã đánh giá shop"
}
```

## 7. Quy trình CRUD bắt buộc trong Postman

Mỗi chức năng thêm/sửa/xóa thực hiện theo thứ tự sau:

1. Gọi API lấy danh sách.
2. Chạy API `POST` để tạo bản ghi mới.
3. Ghi nhận `id` trong bảng MySQL hoặc response.
4. Gán ID vào Collection Variable tương ứng.
5. Chạy API `PUT` hoặc `PATCH` để thay đổi dữ liệu.
6. Chạy lại API lấy chi tiết hoặc danh sách để xác minh.
7. Chạy API `DELETE`.
8. Chạy lại API lấy danh sách để xác nhận bản ghi đã bị xóa.

## 8. Script kiểm tra chung trong Postman Tests

```javascript
pm.test("HTTP tra ve thanh cong", function () {
    pm.expect(pm.response.code).to.be.oneOf([200, 201, 204]);
});

pm.test("Phan hoi co thoi gian hop ly", function () {
    pm.expect(pm.response.responseTime).to.be.below(3000);
});

pm.test("Khong tra ve loi server", function () {
    pm.expect(pm.response.code).not.eql(500);
});
```

Script kiểm tra CRUD:

```javascript
const response = pm.response.json();

pm.test("Response JSON hop le", function () {
    pm.expect(response).to.be.an("object");
});

pm.test("Co thong bao ket qua", function () {
    pm.expect(response.message).to.exist;
});
```

## 9. Kiểm tra dữ liệu MySQL

```sql
USE fashion_shop;

SELECT * FROM users ORDER BY id DESC;
SELECT * FROM categories ORDER BY id DESC;
SELECT * FROM products ORDER BY id DESC;
SELECT * FROM product_variants ORDER BY id DESC;
SELECT * FROM carts ORDER BY id DESC;
SELECT * FROM cart_items ORDER BY id DESC;
SELECT * FROM coupons ORDER BY id DESC;
SELECT * FROM orders ORDER BY id DESC;
SELECT * FROM order_items ORDER BY id DESC;
SELECT * FROM posts ORDER BY id DESC;
SELECT * FROM post_categories ORDER BY id DESC;
SELECT * FROM post_comments ORDER BY id DESC;
SELECT * FROM reviews ORDER BY id DESC;
SELECT * FROM review_images ORDER BY id DESC;
SELECT * FROM review_replies ORDER BY id DESC;
```

Kiểm tra khóa ngoại và dữ liệu liên quan:

```sql
SELECT o.id, o.order_code, o.receiver_name, o.final_amount,
       o.payment_status, o.order_status,
       oi.product_name, oi.size, oi.color, oi.quantity, oi.price
FROM orders o
JOIN order_items oi ON oi.order_id = o.id
ORDER BY o.id DESC;
```

## 10. Bảng kết quả kiểm thử mẫu

| STT | Test case | Precondition | Input | Kết quả mong đợi | HTTP status | Đạt |
|---|---|---|---|---|---|---|
| TC01 | Đăng nhập Admin | Admin tồn tại | `ducodwj/password` | Nhận JWT | 200 | |
| TC02 | JWT ba phần | Đăng nhập thành công | Token | Ba phần JWT | 200 | |
| TC03 | Đăng ký Customer | Email chưa tồn tại | Họ tên/email/mật khẩu | Lưu `users` | 201 | |
| TC04 | Đăng nhập Customer mới | Đăng ký thành công | Email/mật khẩu mới | Nhận JWT customer | 200 | |
| TC04A | Đăng ký bằng mật khẩu chỉ gồm chữ | Email chưa tồn tại | Password: `matkhauchu` | Đăng ký thành công | 201 | |
| TC04B | Đăng nhập bằng mật khẩu chỉ gồm chữ | TC04A đã thành công | Password: `matkhauchu` | Nhận JWT customer, chuyển trang chủ | 200 | |
| TC05 | Sai mật khẩu | User tồn tại | Mật khẩu sai | Không nhận token | 401 | |
| TC06 | Thêm danh mục | Có JWT Admin | Name/slug | Thêm MySQL | 201 | |
| TC07 | Sửa danh mục | Category tồn tại | Tên mới | UPDATE MySQL | 200 | |
| TC08 | Xóa danh mục | Category tồn tại | ID | DELETE MySQL | 200 | |
| TC09 | Thêm sản phẩm | Category tồn tại | Category/name/slug | Thêm MySQL | 201 | |
| TC10 | Thêm biến thể | Product tồn tại | SKU/size/color/price/stock | Thêm MySQL | 201 | |
| TC11 | Thêm coupon | JWT Admin | Code/type/value | Thêm MySQL | 201 | |
| TC12 | Thêm giỏ hàng | Variant tồn tại | Session/variant/quantity | Thêm cart | 200/201 | |
| TC13 | Checkout | Có cart hoặc items | Người nhận/items | Tạo order | 201 | |
| TC14 | Cập nhật đơn | Order tồn tại | Trạng thái hợp lệ | UPDATE MySQL | 200 | |
| TC15 | Thêm bài viết | Category/user tồn tại | Tiêu đề/content | Thêm MySQL | 201 | |
| TC16 | Thêm bình luận | Post/user tồn tại | Nội dung | Thêm MySQL | 201 | |
| TC17 | Thêm đánh giá | User/product tồn tại | Rating 1-5 | Thêm MySQL | 201 | |
| TC18 | Trả lời đánh giá | Review tồn tại | Nội dung Admin | Thêm reply | 200 | |
| TC19 | Token sai | Không có JWT hoặc JWT sai | Authorization sai | Không truy cập API bảo vệ | 401/403 | |
| TC20 | Backend không kết nối MySQL | MySQL đã dừng | Gọi API CRUD | HTTP 500 và log lỗi | 500 | |

## 11. HTTP Status mong đợi

| Status | Ý nghĩa |
|---|---|
| `200 OK` | Lấy dữ liệu hoặc cập nhật thành công |
| `201 Created` | Đăng ký hoặc thêm bản ghi thành công |
| `204 No Content` | Xóa thành công nếu endpoint trả không có body |
| `400 Bad Request` | Thiếu trường bắt buộc hoặc dữ liệu không hợp lệ |
| `401 Unauthorized` | Sai đăng nhập hoặc JWT không hợp lệ |
| `403 Forbidden` | Không có quyền truy cập chức năng |
| `404 Not Found` | Không tìm thấy ID hoặc slug |
| `500 Internal Server Error` | Lỗi backend hoặc kết nối MySQL |

## 12. Lưu ý kiểm thử

- Không gửi mật khẩu thật trong báo cáo hoặc commit.
- Mỗi email/tài khoản đăng ký phải duy nhất.
- Đổi dữ liệu trong Postman không được chỉ theo response; phải kiểm tra trực tiếp phpMyAdmin.
- Khi ID trong response chưa khớp, cập nhật lại Collection Variable.
- JWT hiện tại được tạo khi đăng nhập và có thời hạn 24 giờ.
- Một số API admin hiện đang được permit-all ở tầng Spring Security demo. Khi đưa vào môi trường thật, cần mở khóa quyền theo role JWT thay vì để `/api/admin/**` permit-all.

## 13. Test xác minh đăng ký lưu MySQL bằng mật khẩu chữ

1. Khởi động XAMPP/MySQL và backend cổng `8080`.
2. Gọi API đăng ký:

```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json
```

```json
{
  "fullname": "SQL Verify",
  "email": "sql.verify@test.vn",
  "password": "matkhauchu",
  "phone": "0909000123",
  "address": "Hanoi"
}
```

3. Gọi API đăng nhập bằng đúng email và mật khẩu:

```json
{
  "email": "sql.verify@test.vn",
  "password": "matkhauchu"
}
```

4. Response mong đợi là HTTP `200 OK`, gồm JWT 3 phần và:

```json
{
  "role": "customer"
}
```

5. Kiểm tra trực tiếp bằng MySQL:

```sql
USE fashion_shop;

SELECT id, fullname, email, password, role, status
FROM users
WHERE email = 'sql.verify@test.vn';
```

Tài khoản sau khi đăng ký phải có:

- `role = customer`
- `status = 1`
- Trường `password` bắt đầu bằng `$2a$`, `$2b$` hoặc `$2y$`

Không kiểm tra dòng mật khẩu BCrypt bằng cách nhập nó vào mật khẩu. Hệ thống chỉ so khớp chuỗi nhập với BCrypt hash trong cột `password`.
