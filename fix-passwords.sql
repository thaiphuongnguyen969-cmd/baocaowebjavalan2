USE fashion_shop;
UPDATE users
SET password='$2a$10$3Vw00Yppsrp8cXw7FD5TOudNbWrvtFXBWAYzH6Kj760lwiqyVY2Ru'
WHERE email='admin@fashion.vn' OR role='customer';
