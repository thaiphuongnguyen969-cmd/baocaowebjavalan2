run-backend.bat.@echo off
setlocal
cd /d "%~dp0backend"
echo Dang khoi dong Backend FashionShop...
call mvnw.cmd spring-boot:run
if errorlevel 1 (
  echo.
  echo BACKEND KHONG KHOI DONG DUOC. Kiem tra MySQL/XAMPP va xem loi ben tren.
  pause
)
