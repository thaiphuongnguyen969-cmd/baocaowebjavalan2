USE fashion_shop;

INSERT INTO users(fullname,email,password,phone,address,role,status) VALUES
('Quản trị viên','admin@fashion.vn','$2a$10$3Vw00Yppsrp8cXw7FD5TOudNbWrvtFXBWAYzH6Kj760lwiqyVY2Ru','0900000001','Hà Nội','admin',1),
('Nguyễn Minh Anh','anh.nguyen@gmail.com','$2a$10$3Vw00Yppsrp8cXw7FD5TOudNbWrvtFXBWAYzH6Kj760lwiqyVY2Ru','0900000002','Hà Nội','customer',1),
('Trần Hoàng Nam','nam.tran@gmail.com','$2a$10$3Vw00Yppsrp8cXw7FD5TOudNbWrvtFXBWAYzH6Kj760lwiqyVY2Ru','0900000003','Đà Nẵng','customer',1),
('Lê Thu Hà','ha.le@gmail.com','$2a$10$3Vw00Yppsrp8cXw7FD5TOudNbWrvtFXBWAYzH6Kj760lwiqyVY2Ru','0900000004','TP Hồ Chí Minh','customer',1),
('Phạm Gia Huy','huy.pham@gmail.com','$2a$10$3Vw00Yppsrp8cXw7FD5TOudNbWrvtFXBWAYzH6Kj760lwiqyVY2Ru','0900000005','Cần Thơ','customer',1),
('Vũ Khánh Linh','linh.vu@gmail.com','$2a$10$3Vw00Yppsrp8cXw7FD5TOudNbWrvtFXBWAYzH6Kj760lwiqyVY2Ru','0900000006','Hải Phòng','customer',1),
('Đỗ Đức Anh','anh.do@gmail.com','$2a$10$3Vw00Yppsrp8cXw7FD5TOudNbWrvtFXBWAYzH6Kj760lwiqyVY2Ru','0900000007','Huế','customer',1),
('Bùi Mai Phương','phuong.bui@gmail.com','$2a$10$3Vw00Yppsrp8cXw7FD5TOudNbWrvtFXBWAYzH6Kj760lwiqyVY2Ru','0900000008','Biên Hòa','customer',1),
('Ngô Tấn Phát','phat.ngo@gmail.com','$2a$10$3Vw00Yppsrp8cXw7FD5TOudNbWrvtFXBWAYzH6Kj760lwiqyVY2Ru','0900000009','Nha Trang','customer',1),
('Hoàng Lan Chi','chi.hoang@gmail.com','$2a$10$3Vw00Yppsrp8cXw7FD5TOudNbWrvtFXBWAYzH6Kj760lwiqyVY2Ru','0900000010','Quảng Ninh','customer',1);

INSERT INTO categories(name,slug,parent_id,description) VALUES
('Áo','ao',NULL,'Các dòng áo thời trang'),('Quần','quan',NULL,'Quần và bottom'),('Váy','vay',NULL,'Váy nữ'),
('Phụ kiện','phu-kien',NULL,'Phụ kiện thời trang'),('Áo sơ mi','ao-so-mi',1,'Áo sơ mi công sở và casual'),
('Áo thun','ao-thun',1,'Áo thun năng động'),('Áo khoác','ao-khoac',1,'Áo khoác theo mùa'),
('Quần jean','quan-jean',2,'Quần jean'),('Quần tây','quan-tay',2,'Quần tây'),('Túi xách','tui-xach',4,'Túi thời trang');

INSERT INTO products(category_id,name,slug,description,thumbnail,is_active) VALUES
(1,'Áo sơ mi premium xanh','ao-so-mi-premium-xanh','Chất liệu bamboo mềm mát, form slimfit.','https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=900',1),
(6,'Áo thun Essential Trắng','ao-thun-essential-trang','Cotton 220GSM, form basic dễ phối.','https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=900',1),
(7,'Áo khoác bomber đen','ao-khoac-bomber-den','Bomber cao cấp chống gió nhẹ.','https://images.unsplash.com/photo-1551028719-00167b16eac5?w=900',1),
(8,'Quần jean Slimfit xanh','quan-jean-slimfit-xanh','Jean co giãn, bền màu.','https://images.unsplash.com/photo-1542272604-787c3835535d?w=900',1),
(9,'Quần tây công sở đen','quan-tay-cong-so-den','Form slimfit, lịch lãm.','https://images.unsplash.com/photo-1506629082955-511b1aa562c8?w=900',1),
(3,'Váy midi hoa nhí','vay-midi-hoa-nhi','Vải linen pha mềm mại, nữ tính.','https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=900',1),
(1,'Áo polo cafe','ao-polo-cafe','Polo pique thoáng khí, form regular.','https://images.unsplash.com/photo-1625910513413-5fc45ab5f9e0?w=900',1),
(4,'Mũ lưỡi trai basic','mu-luoi-trai-basic','Mũ cotton 6 panel, điều chỉnh được.','https://images.unsplash.com/photo-1521369909029-2afed882baee?w=900',1),
(10,'Túi tote canvas trắng','tui-tote-canvas-trang','Canvas dày, bền, nhiều ngăn.','https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=900',1),
(7,'Áo hoodie oversize ghi','ao-hoodie-oversize-ghi','Nỉ bông cao cấp, phong cách streetwear.','https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=900',1);

INSERT INTO product_variants(product_id,sku,size,color,price,sale_price,stock) VALUES
(1,'ASM-X-01','M','Xanh navy',450000,399000,20),(1,'ASM-X-02','L','Xanh navy',450000,399000,15),
(2,'ATW-01','M','Trắng',220000,NULL,40),(2,'ATW-02','L','Trắng',220000,NULL,35),
(3,'BOM-01','L','Đen',690000,590000,12),(4,'JNS-01','30','Xanh đậm',550000,499000,18),
(5,'QT-01','32','Đen',520000,NULL,14),(6,'VMH-01','M','Hoa nhí',480000,429000,16),
(7,'POL-01','L','Cafe',350000,NULL,25),(8,'CAP-01','Free','Đen',180000,NULL,30),
(9,'TOT-01','Free','Trắng',260000,NULL,22),(10,'HOD-01','XL','Xám',620000,550000,20);

INSERT INTO coupons(code,discount_type,discount_value,min_order_value,usage_limit,used_count,expires_at) VALUES
('WELCOME10','percent',10,100000,100,3,'2027-12-31 23:59:59'),
('GIAM50K','fixed',50000,500000,50,7,'2027-12-31 23:59:59'),
('FREESHIP','fixed',30000,300000,200,12,'2027-12-31 23:59:59'),
('SUMMER15','percent',15,400000,30,2,'2027-08-31 23:59:59'),
('VIP100K','fixed',100000,1000000,20,0,'2027-12-31 23:59:59'),
('NEWUSER','fixed',20000,200000,500,20,'2027-12-31 23:59:59'),
('SALE20','percent',20,700000,40,5,'2027-09-30 23:59:59'),
('BLACKFRIDAY','fixed',150000,1500000,100,1,'2027-11-30 23:59:59'),
('XMAS','percent',12,600000,80,6,'2027-12-25 23:59:59'),
('MEMBER30','fixed',30000,350000,300,15,'2027-12-31 23:59:59');

INSERT INTO reviews(user_id,product_id,rating,comment) VALUES
(2,1,5,'Chất liệu đẹp, mặc rất thoải mái.'),(3,1,4,'Form ổn, giao hàng nhanh.'),
(4,2,5,'Áo trơn dễ phối đồ, đáng mua.'),(5,3,4,'Bomber dày vừa, mặc ấm nhẹ.'),
(6,4,5,'Jean co giãn tốt, không ra màu.'),(7,5,4,'Chất vải cứng cáp, mặc đi làm đẹp.'),
(8,6,5,'Váy rất nữ tính, đúng màu hình.'),(9,7,4,'Polo màu đẹp, size đúng.'),
(10,9,5,'Túi rộng và chắc chắn.'),(2,10,5,'Hoodie oversize đúng gu streetwear.');

INSERT INTO review_images(review_id,image_url) VALUES
(1,'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=300'),
(2,'https://images.unsplash.com/photo-1598032895397-b9472444bf93?w=300'),
(3,'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=300'),
(4,'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=300'),
(5,'https://images.unsplash.com/photo-1542272604-787c3835535d?w=300'),
(6,'https://images.unsplash.com/photo-1506629082955-511b1aa562c8?w=300'),
(7,'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=300'),
(8,'https://images.unsplash.com/photo-1625910513413-5fc45ab5f9e0?w=300'),
(9,'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=300'),
(10,'https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=300');

INSERT INTO review_replies(review_id,user_id,comment) VALUES
(1,1,'Cảm ơn bạn đã tin tưởng FashionShop!'),(3,1,'Chúng tôi rất vui khi bạn hài lòng.'),
(5,1,'Shop sẽ tiếp tục cải thiện chất lượng sản phẩm.'),(7,1,'Cảm ơn đánh giá chi tiết của bạn!'),
(9,1,'Rất vui vì túi tote phù hợp nhu cầu của bạn.');

INSERT INTO post_categories(name,slug,description) VALUES
('Xu hướng','xu-huong','Tin tức xu hướng thời trang'),('Phong cách','phong-cach','Gợi ý phối đồ'),
('Mẹo mặc đẹp','meo-mac-dep','Kiến thức thời trang'),('Khuyến mãi','khuyen-mai','Ưu đãi mới nhất'),
('Streetwear','streetwear','Phong cách đường phố'),('Công sở','cong-so','Thời trang công sở'),
('Bền vững','ben-vung','Thời trang xanh'),('Phụ kiện','phu-kien','Mẹo chọn phụ kiện'),
('Chăm sóc','cham-soc','Chăm sóc quần áo'),('Tin shop','tin-shop','Thông tin cửa hàng');

INSERT INTO posts(category_id,user_id,title,slug,thumbnail,summary,content,is_published) VALUES
(1,1,'5 xu hướng thời trang dẫn đầu mùa mới','5-xu-huong-thoi-trang-moi','https://images.unsplash.com/photo-1445205170230-053b83016050?w=900','Những màu sắc và kiểu dáng đáng thử.','Thời trang mùa mới nổi bật với phong cách tối giản, màu trung tính và chất liệu bền vững.',1),
(2,1,'Cách phối đồ công sở nam thanh lịch','phoi-do-cong-so-nam','https://images.unsplash.com/photo-1483985988355-763728e1935b?w=900','Set đồ công sở không còn nhàm chán.','Kết hợp sơ mi trắng, quần tây và giày da là công thức an toàn nhưng vẫn tinh tế.',1),
(3,1,'Bí quyết giữ áo sơ mi luôn phẳng phiu','giu-ao-so-mi-phang-phiu','https://images.unsplash.com/photo-1598032895397-b9472444bf93?w=900','Mẹo giặt và ủi đơn giản.','Giặt đúng nhiệt độ, lộn trái áo và ủi khi áo còn hơi ẩm sẽ giúp áo giữ form tốt.',1),
(4,1,'Tuần lễ ưu đãi FashionShop','tuan-le-uu-dai','https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=900','Giảm giá nhiều danh mục.','Áp dụng mã WELCOME10 khi mua hàng và nhận thêm ưu đãi từ chương trình tuần lễ thời trang.',1),
(5,1,'Streetwear cơ bản cho người mới','streetwear-co-ban','https://images.unsplash.com/photo-1523398002811-999ca8dec234?w=900','Nắm rõ những item nền tảng.','Hoodie, áo thun oversize và quần cargo là điểm khởi đầu cho phong cách streetwear.',1),
(6,1,'Trang phục công sở nữ tinh tế','trang-phuc-cong-so-nu','https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=900','Gợi ý outfit đi làm hiện đại.','Áo blazer cùng quần tây hoặc váy midi mang lại vẻ chuyên nghiệp và thoải mái.',1),
(7,1,'Fashion xanh và xu hướng tái chế','fashion-xanh-tai-che','https://images.unsplash.com/photo-1539109136881-3be0616acf4b?w=900','Chọn quần áo thân thiện hơn.','Ưu tiên chất liệu tái chế, giặt ít và bảo quản đúng cách để kéo dài vòng đời sản phẩm.',1),
(8,1,'Cách chọn túi xách theo dáng người','chon-tui-xach-theo-dang','https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=900','Phụ kiện giúp hoàn thiện phong cách.','Túi tote phù hợp đi học, làm việc; clutch nhỏ gọn cho buổi tiệc tối.',1),
(9,1,'Chăm sóc quần jean bền màu','cham-soc-quan-jean','https://images.unsplash.com/photo-1542272604-787c3835535d?w=900','Giữ màu jean lâu hơn.','Hạn chế giặt nóng, dùng nước lạnh và phơi trong bóng râm để giảm bạc màu.',1),
(10,1,'FashionShop khai trương showroom mới','khai-truong-showroom','https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=900','Cập nhật địa điểm mua sắm mới.','Showroom mới mở cửa tại Hà Nội với diện mạo và khu vực trải nghiệm sản phẩm hiện đại.',1);

INSERT INTO post_comments(post_id,user_id,parent_id,content) VALUES
(1,2,NULL,'Bài viết rất hữu ích!'),(1,3,1,'Mình cũng thấy màu trung tính dễ phối.'),
(2,4,NULL,'Mình sẽ thử combo này đi làm.'),(5,5,NULL,'Streetwear rất hợp phong cách mới.'),
(6,6,NULL,'Gợi ý đi làm rất tinh tế.'),(7,7,NULL,'Thời trang xanh là hướng đáng quan tâm.'),
(8,8,NULL,'Cảm ơn mẹo chọn túi.'),(9,9,NULL,'Đúng những gì mình đang cần.'),(10,10,NULL,'Rất mong được ghé showroom.'),(4,2,NULL,'Đã áp dụng mã WELCOME10 thành công.');

INSERT INTO carts(user_id,session_id) VALUES
(2,'session-demo-1'),(3,'session-demo-2'),(4,'session-demo-3'),(5,'session-demo-4'),
(6,'session-demo-5'),(7,'session-demo-6'),(8,'session-demo-7'),(9,'session-demo-8'),
(10,'session-demo-9'),(NULL,'session-demo-10');
INSERT INTO cart_items(cart_id,product_variant_id,quantity) VALUES
(1,1,1),(2,3,2),(3,5,1),(4,6,1),(5,7,1),(6,8,1),(7,9,1),(8,10,1),(9,11,2),(10,12,1);

INSERT INTO orders(user_id,order_code,receiver_name,receiver_phone,receiver_address,total_amount,discount_amount,shipping_fee,final_amount,payment_method,payment_status,order_status,note) VALUES
(2,'FS-1001','Nguyễn Minh Anh','0900000002','Hà Nội',399000,0,30000,429000,'COD','pending','pending','Giao giờ hành chính'),
(3,'FS-1002','Trần Hoàng Nam','0900000003','Đà Nẵng',440000,20000,30000,450000,'VNPAY','paid','confirmed',''),
(4,'FS-1003','Lê Thu Hà','0900000004','TP Hồ Chí Minh',590000,50000,0,540000,'MOMO','paid','shipping','Gọi trước khi giao'),
(5,'FS-1004','Phạm Gia Huy','0900000005','Cần Thơ',499000,0,30000,529000,'COD','pending','pending',''),
(6,'FS-1005','Vũ Khánh Linh','0900000006','Hải Phòng',520000,0,30000,550000,'BANK_TRANSFER','paid','completed',''),
(7,'FS-1006','Đỗ Đức Anh','0900000007','Huế',429000,0,30000,459000,'COD','pending','completed',''),
(8,'FS-1007','Bùi Mai Phương','0900000008','Biên Hòa',350000,20000,30000,360000,'VNPAY','paid','shipping',''),
(9,'FS-1008','Ngô Tấn Phát','0900000009','Nha Trang',180000,0,30000,210000,'MOMO','paid','completed',''),
(10,'FS-1009','Hoàng Lan Chi','0900000010','Quảng Ninh',260000,0,30000,290000,'COD','pending','confirmed',''),
(NULL,'FS-1010','Khách lẻ','0901234567','Hà Nội',550000,0,0,550000,'COD','pending','pending','Đơn khách vãng lai');
INSERT INTO order_items(order_id,product_variant_id,product_name,size,color,price,quantity) VALUES
(1,1,'Áo sơ mi premium xanh','M','Xanh navy',399000,1),(2,3,'Áo thun Essential Trắng','M','Trắng',220000,2),
(3,5,'Áo khoác bomber đen','L','Đen',590000,1),(4,6,'Quần jean Slimfit xanh','30','Xanh đậm',499000,1),
(5,7,'Quần tây công sở đen','32','Đen',520000,1),(6,8,'Váy midi hoa nhí','M','Hoa nhí',429000,1),
(7,9,'Áo polo cafe','L','Cafe',350000,1),(8,10,'Mũ lưỡi trai basic','Free','Đen',180000,1),
(9,11,'Túi tote canvas trắng','Free','Trắng',260000,1),(10,12,'Áo hoodie oversize ghi','XL','Xám',550000,1);
