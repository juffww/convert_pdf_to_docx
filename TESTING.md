# Hướng dẫn kiểm tra đăng nhập

## 1. Kiểm tra MySQL đang chạy

```bash
# Kiểm tra MySQL service
mysql -u root -p

# Sau khi vào MySQL, kiểm tra database
SHOW DATABASES;
USE convert_file;
SHOW TABLES;
SELECT * FROM users;
```

## 2. Thông tin đăng nhập mặc định

Từ file `convertFile.sql`, có 2 tài khoản:
- **Username:** `admin` / **Password:** `admin123`
- **Username:** `user` / **Password:** `user01`

## 3. Các bước test

1. Mở trình duyệt và truy cập: http://localhost:8080
2. Nhập username và password
3. Click "Đăng nhập"

### Kết quả mong đợi:
- ✅ **Thành công:** Chuyển đến trang `dashboard.html`
- ❌ **Sai mật khẩu:** Quay lại `index.html?error=invalid` với thông báo lỗi
- ❌ **Lỗi server:** Quay lại `index.html?error=server` với thông báo lỗi kết nối

## 4. Kiểm tra lỗi nếu có

### Nếu thấy error=server:

**Bước 1:** Kiểm tra Tomcat logs
```bash
cd "/c/Program Files/Apache Software Foundation/Tomcat 11.0/logs"
tail -n 100 catalina.out  # hoặc file log mới nhất
```

Tìm dòng log:
```
=== LOGIN ERROR ===
Username: ...
Error: ...
```

**Bước 2:** Các lỗi thường gặp và cách sửa:

#### a) Lỗi: "Access denied for user 'root'@'localhost'"
**Nguyên nhân:** Sai mật khẩu MySQL

**Giải pháp:** Cập nhật mật khẩu trong `DbConnection.java` hoặc set biến môi trường:
```bash
# Linux/Mac
export DB_PASS="your_mysql_password"

# Windows CMD
set DB_PASS=your_mysql_password

# Windows PowerShell
$env:DB_PASS="your_mysql_password"
```

#### b) Lỗi: "Unknown database 'convert_file'"
**Nguyên nhân:** Chưa import database

**Giải pháp:**
```bash
mysql -u root -p < convertFile.sql
```

#### c) Lỗi: "Communications link failure"
**Nguyên nhân:** MySQL chưa chạy hoặc sai port

**Giải pháp:**
- Khởi động MySQL service
- Kiểm tra port (mặc định: 3306)
- Cập nhật URL trong `DbConnection.java` nếu cần:
  ```java
  private static final String DEFAULT_URL = "jdbc:mysql://localhost:3307/convert_file?useSSL=false&serverTimezone=UTC";
  ```

## 5. Cấu hình Database tùy chỉnh

Nếu MySQL của bạn có cấu hình khác, tạo biến môi trường:

```bash
# Ví dụ cho MySQL trên port 3307 với user 'myuser'
export DB_URL="jdbc:mysql://localhost:3307/convert_file?useSSL=false&serverTimezone=UTC"
export DB_USER="myuser"
export DB_PASS="mypassword"

# Sau đó restart Tomcat hoặc deploy lại
./deploy.sh
```

## 6. Test thủ công kết nối DB

Tạo file test đơn giản:

```java
// TestConnection.java
import utils.DbConnection;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DbConnection.getConnection();
            System.out.println("✅ Kết nối thành công!");
            System.out.println("Database: " + conn.getCatalog());
            conn.close();
        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối:");
            e.printStackTrace();
        }
    }
}
```

Chạy:
```bash
javac -cp "target/classes:target/ConvertFile/WEB-INF/lib/*" TestConnection.java
java -cp ".:target/classes:target/ConvertFile/WEB-INF/lib/*" TestConnection
```

## 7. Xem Tomcat logs real-time

```bash
# Windows Git Bash
tail -f "/c/Program Files/Apache Software Foundation/Tomcat 11.0/logs/catalina.out"

# Hoặc xem file log mới nhất
cd "/c/Program Files/Apache Software Foundation/Tomcat 11.0/logs"
tail -f $(ls -t | head -n1)
```

## 8. Clear cache và session

Nếu gặp lỗi lạ, thử:
1. Xóa cookies trong browser
2. Xóa thư mục work của Tomcat:
   ```bash
   rm -rf "/c/Program Files/Apache Software Foundation/Tomcat 11.0/work/Catalina"
   ```
3. Restart Tomcat
4. Deploy lại: `./deploy.sh`
