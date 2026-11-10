# Huong dan test backend FashionShop bang Postman

Tai lieu nay dung de kiem tra API Spring Boot tren XAMPP/MySQL.

## 1. Mo truong chay

1. Mo XAMPP va Start `MySQL`.
2. Mo phpMyAdmin tai `http://localhost/phpmyadmin`.
3. Import `database.sql`, sau do co the import `seed.sql` neu can du lieu mau.
4. Mo terminal thu nhat, chay backend:

```powershell
cd C:\Users\pc\Documents\java_fix\backend
.\mvnw.cmd spring-boot:run
```

Backend mac dinh chay tai:

```text
http://localhost:8080
```

Frontend ky thuat:

```text
http://localhost:8090
```

Kiem tra backend:

```text
GET http://localhost:8080/api/categories
```

Dung Web server khong bao mat: admin hien tai cho phep `/api/admin/**`; du lieu van duoc luu vao MySQL. Khi goi API admin, van nen gui header `Authorization: Bearer {{token}}`.

## 2. Import vao Postman

1. Mo Postman.
2. Chon `Import`.
3. Chon file `FashionShop_Postman_Collection.json`.
4. Mo collection va chon tab `Variables`.
5. Kiem tra:

| Bien | Gia tri |
|---|---|
| `baseUrl` | `http://localhost:8080` |
| `adminEmail` | `ducodwj` |
| `adminPassword` | `password` |
| `sessionId` | `postman-test-session` |
| `variantId` | `1` |
| `productId` | `1` |
| `userId` | `2` |

## 3. Cau hinh chung

Moi request `POST`, `PUT`, `PATCH` gui body JSON:

```text
Content-Type: application/json
Authorization: Bearer {{token}}
```

Trong Postman chon `Body > raw > JSON`.

## 4. Quy trinh test co ban

1. Goi `01 Auth > Dang nhap admin`.
2. Kiem tra response co `token` va `user.role = admin`.
3. Goi danh sach san pham, danh muc va nguoi dung.
4. Them mot ban ghi bang API.
5. Goi danh sach de lay `id`.
6. Doi `id` vao URL bien `categoryId`, `productId`, hoac ID tuong ung.
7. Goi API cap nhat va kiem tra du lieu trong response/MySQL.
8. Goi API xoa.
9. Goi danh sach lai de xac nhan da bi xoa.

## 5. Bang API xac thuc

| Method | URL | Body bat buoc | Ket qua mong doi |
|---|---|---|---|
| POST | `/api/auth/login` | `email`, `password` | `200`, tra token va user |
| POST | `/api/auth/register` | `fullname`, `email`, `password`, `phone`, `address` | `201`, them customer vao `users` |

Body dang nhap:

```json
{
  "email": "ducodwj",
  "password": "password"
}
```

Body dang ky:

```json
{
  "fullname": "Postman Customer",
  "email": "postman.customer@test.vn",
  "password": "123456",
  "phone": "0909000999",
  "address": "Ha Noi"
}
```

## 6. Bang API danh muc, san pham, bien the

| Method | URL | Body chinh |
|---|---|---|
| GET | `/api/categories` | Khong |
| GET | `/api/admin/categories/{id}` | Khong |
| POST | `/api/admin/categories` | `name`, `slug`, `parentId`, `description` |
| PUT | `/api/admin/categories/{id}` | `name`, `slug`, `parentId`, `description` |
| DELETE | `/api/admin/categories/{id}` | Khong |
| GET | `/api/products?page=1&size=12` | Khong |
| GET | `/api/products/{id}` | Khong |
| GET | `/api/admin/products` | Khong |
| POST | `/api/admin/products` | `categoryId`, `name`, `slug`, `description`, `thumbnail`, `images`, `isActive` |
| PUT | `/api/admin/products/{id}` | Day du `categoryId`, `name`, `slug`, `description`, `thumbnail`, `images`, `isActive` |
| DELETE | `/api/admin/products/{id}` | Khong |
| GET | `/api/admin/variants` | Khong |
| POST | `/api/admin/variants` | `productId`, `sku`, `size`, `color`, `price`, `salePrice`, `stock` |
| PUT | `/api/admin/variants/{id}` | Day du cac truong cua bien the |
| DELETE | `/api/admin/variants/{id}` | Khong |

Body category mau:

```json
{
  "name": "Danh muc Postman",
  "slug": "danh-muc-postman",
  "parentId": null,
  "description": "Du lieu tao bang Postman"
}
```

Body product mau:

```json
{
  "categoryId": 1,
  "name": "San pham Postman",
  "slug": "san-pham-postman",
  "description": "San pham test CRUD",
  "thumbnail": "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=900",
  "images": "[\"https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=900\"]",
  "isActive": 1
}
```

## 7. Bang API nguoi dung va dashboard

| Method | URL | Body chinh |
|---|---|---|
| GET | `/api/admin/dashboard` | Khong |
| GET | `/api/admin/users` | Khong |
| POST | `/api/admin/users` | `fullname`, `email`, `password`, `phone`, `address`, `role`, `status` |
| PUT | `/api/admin/users/{id}` | `fullname`, `email`, `phone`, `address`, `role`, `status`, `password` neu doi mat khau |
| DELETE | `/api/admin/users/{id}` | Khong |

Body tao nguoi dung:

```json
{
  "fullname": "Nguoi dung Postman",
  "email": "postman.user@test.vn",
  "password": "123456",
  "phone": "0909000001",
  "address": "Da Nang",
  "role": "customer",
  "status": 1
}
```

## 8. Gio hang, ma giam gia va don hang

| Method | URL | Body chinh |
|---|---|---|
| GET | `/api/admin/coupons` | Khong |
| GET | `/api/coupons` | Khong |
| POST | `/api/admin/coupons` | `code`, `discountType`, `discountValue`, `minOrderValue`, `usageLimit`, `expiresAt` |
| PUT | `/api/admin/coupons/{id}` | Day du cac truong coupon |
| DELETE | `/api/admin/coupons/{id}` | Khong |
| POST | `/api/cart/items` | `sessionId`, `variantId`, `quantity` |
| GET | `/api/cart/{sessionId}` | Khong |
| DELETE | `/api/cart/items/{cartItemId}` | Khong |
| POST | `/api/orders/checkout` | Thong tin nguoi nhan, `items`, `paymentMethod`, `couponCode` |
| GET | `/api/admin/orders` | Khong |
| GET | `/api/admin/orders/{id}` | Khong |
| PATCH | `/api/admin/orders/{id}/status` | `orderStatus`, `paymentStatus` |
| DELETE | `/api/admin/orders/{id}` | Khong |
| GET | `/api/orders/track/{orderCode}` | Khong |

Body them gio:

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
  "receiverName": "Postman Customer",
  "receiverPhone": "0909000999",
  "receiverAddress": "Ha Noi",
  "paymentMethod": "COD",
  "couponCode": "WELCOME10",
  "note": "Test don hang Postman",
  "items": [
    {
      "variantId": 1,
      "quantity": 1
    }
  ]
}
```

Trang thai don hop le:

```text
orderStatus: pending | confirmed | shipping | completed | cancelled
paymentStatus: pending | paid | refunded
```

## 9. Bai viet, danh muc bai viet, binh luan, danh gia

| Method | URL | Body chinh |
|---|---|---|
| GET | `/api/post-categories` | Khong |
| GET | `/api/posts?page=1&size=10` | Khong |
| GET | `/api/posts/{slug}` | Khong |
| POST | `/api/admin/posts` | `categoryId`, `userId`, `title`, `slug`, `thumbnail`, `summary`, `content`, `isPublished` |
| GET | `/api/admin/posts` | Khong |
| PUT | `/api/admin/posts/{id}` | Day du cac truong bai viet |
| DELETE | `/api/admin/posts/{id}` | Khong |
| POST | `/api/posts/{postId}/comments` | `userId`, `parentId`, `content` |
| GET | `/api/admin/post-categories` | Khong |
| POST | `/api/admin/post-categories` | `name`, `slug`, `description` |
| PUT | `/api/admin/post-categories/{id}` | Day du cac truong danh muc bai viet |
| DELETE | `/api/admin/post-categories/{id}` | Khong |
| GET | `/api/admin/post-comments` | Khong |
| POST | `/api/admin/post-comments` | `postId`, `userId`, `parentId`, `content` |
| PUT | `/api/admin/post-comments/{id}` | `content` |
| DELETE | `/api/admin/post-comments/{id}` | Khong |
| GET | `/api/reviews/{productId}` | Khong |
| GET | `/api/admin/reviews` | Khong |
| POST | `/api/reviews` | `userId`, `productId`, `rating`, `comment` |
| POST | `/api/admin/reviews` | `userId`, `productId`, `rating`, `comment` |
| POST | `/api/admin/reviews/{reviewId}/reply` | `userId`, `comment` |
| PUT | `/api/admin/reviews/{id}` | `userId`, `productId`, `rating`, `comment` |
| DELETE | `/api/admin/reviews/{id}` | Khong |

## 10. Kiem tra MySQL sau khi CRUD

Mo phpMyAdmin va chon database `fashion_shop`.

```sql
SELECT * FROM categories ORDER BY id DESC LIMIT 10;
SELECT * FROM products ORDER BY id DESC LIMIT 10;
SELECT * FROM product_variants ORDER BY id DESC LIMIT 10;
SELECT * FROM users ORDER BY id DESC LIMIT 10;
SELECT * FROM coupons ORDER BY id DESC LIMIT 10;
SELECT * FROM orders ORDER BY id DESC LIMIT 10;
SELECT * FROM order_items ORDER BY order_id DESC;
SELECT * FROM posts ORDER BY id DESC LIMIT 10;
SELECT * FROM post_categories ORDER BY id DESC LIMIT 10;
SELECT * FROM post_comments ORDER BY id DESC LIMIT 10;
SELECT * FROM reviews ORDER BY id DESC LIMIT 10;
```

## 11. Ma trang thai can ghi nhan

- `200 OK`: lay danh sach hoac cap nhat thanh cong.
- `201 Created`: tao moi thanh cong.
- `400 Bad Request`: thieu truong hoac du lieu khong hop le.
- `401 Unauthorized`: dang nhap sai hoac tai khoan bi khoa.
- `404 Not Found`: khong thay san pham, bai viet hoac ma don hang.
- `500 Internal Server Error`: kiem tra MySQL dang chay, khoa ngoai, gia tri ID va log backend.

Luu y: GUI/frontend dang o co che demo; API admin co the truy cap neu khong gui token. Tat ca du lieu CRUD van phai dung endpoint backend va Spring Boot se ghi vao MySQL.
