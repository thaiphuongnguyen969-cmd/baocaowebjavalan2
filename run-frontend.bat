@echo off
setlocal
cd /d "%~dp0frontend"
echo Dang khoi dong Frontend FashionShop...
call mvnw.cmd spring-boot:run
if errorlevel 1 (
  echo.
  echo FRONTEND KHONG KHOI DONG DUOC. Hay dam bao Backend dang chay tren cong 8080.
  pause
)
