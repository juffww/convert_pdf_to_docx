# Dashboard - Hướng dẫn sử dụng

## 🎉 Đã hoàn thành

Dashboard hiện đã có đầy đủ các tính năng:

### ✅ Tính năng đã triển khai:

1. **Đăng nhập** (✓)
   - Form đăng nhập tại trang chủ
   - Validation username/password
   - Session management
   - Remember me cookie

2. **Dashboard** (✓)
   - Hiển thị thông tin user đang đăng nhập
   - Hiển thị danh sách file PDF đã upload
   - Thông tin chi tiết: tên file, kích thước, ngày upload
   - Giao diện responsive

3. **Upload file PDF** (✓)
   - Form upload với validation
   - Chỉ chấp nhận file .pdf
   - Giới hạn kích thước: max 50MB/file
   - Lưu file vào thư mục `uploads/`
   - Lưu metadata vào database
   - Thông báo thành công/lỗi

4. **Đăng xuất** (✓)
   - Nút đăng xuất trên header
   - Clear session

## 🚀 Cách sử dụng

### 1. Đăng nhập
```
URL: http://localhost:8080
Username: admin
Password: admin123
```

### 2. Upload file PDF
1. Click vào ô "Chọn file PDF"
2. Chọn file .pdf từ máy tính
3. Click nút "Tải lên"
4. Đợi upload hoàn tất và tự động refresh

### 3. Xem danh sách file
- Tất cả file của bạn hiển thị trong bảng
- Thông tin gồm: ID, Tên file, Kích thước, Ngày upload
- Các nút thao tác (sẽ implement sau):
  - 🔄 Chuyển đổi: Convert PDF sang DOCX
  - ⬇️ Tải về: Download file gốc
  - 🗑️ Xóa: Xóa file

## 📁 Cấu trúc thư mục upload

```
Tomcat/webapps/ROOT/
└── uploads/
    ├── 1731664123456_1_document.pdf
    ├── 1731664789012_2_report.pdf
    └── ...
```

Format tên file: `{timestamp}_{userId}_{originalName}`

## 🔧 Troubleshooting

### Lỗi "error=nofile"
**Nguyên nhân:** Chưa chọn file
**Giải pháp:** Chọn file trước khi click "Tải lên"

### Lỗi "error=invalidtype"
**Nguyên nhân:** File không phải PDF
**Giải pháp:** Chỉ upload file có đuôi .pdf

### Lỗi "error=upload"
**Nguyên nhân:** Lỗi khi lưu file hoặc ghi database
**Giải pháp:** 
- Kiểm tra quyền ghi thư mục uploads
- Kiểm tra kết nối database
- Xem log Tomcat để biết chi tiết

### Lỗi "error=dbfailed"
**Nguyên nhân:** File đã lưu nhưng không ghi được vào DB
**Giải pháp:**
- Kiểm tra bảng `files` trong database
- File sẽ tự động bị xóa nếu không ghi được DB

### File upload thành công nhưng không hiển thị
**Giải pháp:**
1. Refresh lại trang (Ctrl + F5)
2. Kiểm tra database:
```sql
SELECT * FROM files WHERE user_id = 1;
```

### Upload file lớn bị lỗi
**Nguyên nhân:** File vượt quá giới hạn
**Giải pháp:** 
- Mặc định max: 50MB
- Để tăng giới hạn, sửa trong `fileUploadController.java`:
```java
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 100,  // 100MB
    maxRequestSize = 1024 * 1024 * 200 // 200MB
)
```

## 📊 Database Schema

### Bảng files
```sql
CREATE TABLE files (
    file_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

## 🎯 Các bước tiếp theo (cần implement)

1. **Convert Controller** - Chuyển đổi PDF sang DOCX
2. **Download Controller** - Tải file về
3. **Delete Controller** - Xóa file
4. **Queue Management** - Quản lý hàng đợi chuyển đổi
5. **History View** - Xem lịch sử chuyển đổi
6. **Error Handling** - Xử lý lỗi tốt hơn
7. **File Preview** - Xem trước PDF
8. **Search/Filter** - Tìm kiếm và lọc file

## 🧪 Test Cases

### Test 1: Upload file PDF thành công
1. Đăng nhập
2. Chọn file .pdf nhỏ (< 1MB)
3. Click "Tải lên"
4. **Expected:** Thấy thông báo "File đã được tải lên thành công"
5. **Expected:** File xuất hiện trong danh sách

### Test 2: Upload file không phải PDF
1. Chọn file .docx hoặc .jpg
2. Click "Tải lên"
3. **Expected:** Thấy lỗi "Chỉ chấp nhận file PDF!"

### Test 3: Upload mà không chọn file
1. Click "Tải lên" mà không chọn file
2. **Expected:** Browser hiện validation message

### Test 4: Xem danh sách file rỗng
1. User mới chưa upload file nào
2. **Expected:** Hiển thị empty state "Chưa có file nào"

### Test 5: Session timeout
1. Đăng nhập
2. Đợi 1 giờ (hoặc clear session)
3. Refresh trang
4. **Expected:** Redirect về login với error=unauthorized

## 📝 Notes

- Tất cả file được lưu trong thư mục `uploads/` của Tomcat
- File được đổi tên để tránh trùng lặp
- Mỗi user chỉ thấy file của mình
- Session timeout: 1 giờ
- Encoding: UTF-8 cho tiếng Việt

## 🔒 Bảo mật

✅ **Đã implement:**
- Session validation
- File type validation (.pdf only)
- File size limit
- User isolation (chỉ thấy file của mình)

⚠️ **Cần cải thiện:**
- Hash password (hiện đang plain text)
- CSRF protection
- XSS prevention
- SQL injection prevention (đã dùng PreparedStatement)
- File name sanitization

## 📞 Support

Nếu gặp lỗi, check log Tomcat:
```bash
tail -f "/c/Program Files/Apache Software Foundation/Tomcat 11.0/logs/catalina.out"
```

Tìm dòng:
```
=== UPLOAD ERROR ===
=== DASHBOARD ERROR ===
=== LOGIN ERROR ===
```
