# 📚 BookStore - Website Bán Sách Trực Tuyến

> **Đề thi Quá trình - Môn Lập Trình Web**  
> **Sinh viên thực hiện:** Lê Huỳnh Anh Khôi  
> **Mã số sinh viên (MSSV):** 24133034  
> **Đề số:** 02  

---

## 📖 Giới thiệu dự án

Website **BookStore** là một ứng dụng thương mại điện tử chuyên cung cấp và bán các đầu sách bản quyền. Dự án được xây dựng dựa trên kiến trúc chuẩn **MVC (Model - View - Controller)** kết hợp các tầng **DAO (Data Access Object)** và **Service** trong Java Web.

---

## 🛠️ Công nghệ sử dụng

- **Ngôn ngữ & Nền tảng:** Java 8 (JDK 1.8), Java Servlet 3.0 / JSP, JSTL 1.2
- **Kiến trúc:** MVC, DAO Pattern, Service Layer
- **Trình trang trí giao diện:** OpenSymphony SiteMesh 2.4.2
- **Cơ sở dữ liệu:** Microsoft SQL Server (kết nối qua JDBC Driver `mssql-jdbc`)
- **Giao diện (Frontend):** HTML5, CSS3, Bootstrap 5.3, Bootstrap Icons
- **Quản lý dự án & Build:** Apache Maven
- **Server chạy ứng dụng:** Apache Tomcat

---

## ✨ Các chức năng chính

### 1. 🛒 Chức năng Giỏ hàng (Cart)
- **Thêm vào giỏ hàng:** Có thể thêm sách nhanh từ trang danh sách hoặc chọn số lượng chi tiết tại trang thông tin sách.
- **Giới hạn số lượng chặt chẽ theo tồn kho:**
  - Số lượng tối thiểu: `1` cuốn.
  - Số lượng tối đa: Không thể vượt quá số lượng còn lại trong kho (`quantity` trong CSDL). Hệ thống tự động giới hạn và đưa ra thông báo cảnh báo rõ ràng nếu người dùng cố ý nhập quá số lượng.
  - Sách hết hàng (`quantity <= 0`) sẽ hiển thị huy hiệu *"Tạm hết hàng"* và vô hiệu hóa nút đặt.
- **Cập nhật & Điều chỉnh số lượng:** Hỗ trợ nút tăng (`+`), giảm (`-`), hoặc nhập số trực tiếp.
- **Xóa sản phẩm & Làm trống giỏ:** Xóa từng cuốn hoặc xóa toàn bộ giỏ hàng (kèm hộp thoại xác nhận).
- **Huy hiệu giỏ hàng (Navbar Badge):** Biểu tượng giỏ hàng trên thanh điều hướng cập nhật số lượng sản phẩm theo thời gian thực.

### 2. 💵 Chức năng Thanh toán COD (Cash On Delivery)
- Cho phép khách hàng đặt hàng với phương thức **Thanh toán tiền mặt khi nhận hàng (COD)**.
- Form nhập thông tin nhận hàng trực quan:
  - Họ và tên người nhận (tự động điền theo tài khoản đăng nhập).
  - Số điện thoại người nhận.
  - Địa chỉ giao hàng chi tiết.
  - Ghi chú đơn hàng (nếu có).
- Bảng tóm tắt đơn hàng, phí vận chuyển (Miễn phí), và tổng số tiền thanh toán.
- **Xử lý đơn hàng an toàn:**
  - Tự động trừ số lượng sách tương ứng trong kho hàng (`dbo.books`).
  - Xóa giỏ hàng trong phiên làm việc sau khi đặt hàng thành công.
  - Điều hướng ngay lập tức đến trang Lịch sử đơn hàng kèm mã đơn.

### 3. 📦 Chức năng Lịch sử đặt hàng & Bộ lọc 8 trạng thái
- Khách hàng có thể theo dõi toàn bộ đơn hàng của mình tại đường dẫn `/order-history`.
- Hỗ trợ thanh Tab lọc theo đúng **8 trạng thái** đơn hàng:
  1. 📝 **Đơn hàng mới**
  2. 🔍 **Đã xác nhận**
  3. 📦 **Chuẩn bị hàng**
  4. 🚚 **Vận chuyển**
  5. 🛵 **Giao hàng**
  6. ✅ **Đã giao**
  7. ❌ **Đơn hàng hủy**
  8. 🔄 **Đơn hàng hoàn**
- Mỗi tab hiển thị huy hiệu số lượng đơn tương ứng.
- Mỗi đơn hàng hiển thị chi tiết:
  - Mã đơn hàng (`#OD-...`), ngày giờ đặt hàng.
  - Huy hiệu màu sắc và biểu tượng đặc trưng cho từng trạng thái.
  - **Thanh tiến trình trực quan (Order Stepper)** thể hiện tiến độ xử lý đơn hàng.
  - Thông tin người nhận, số điện thoại, địa chỉ nhận hàng, phương thức thanh toán.
  - Danh sách từng cuốn sách trong đơn (ảnh bìa, tên sách, đơn giá, số lượng, thành tiền).
  - Tổng số tiền thanh toán COD.
- **Hủy đơn hàng:** Cho phép khách hàng tự hủy đơn nếu đơn đang ở trạng thái `Đơn hàng mới`. Khi hủy, hệ thống **tự động hoàn trả lại số lượng sách vào kho**.

### 4. 🛡️ Chức năng Quản trị (Admin)
- Đăng nhập quyền quản trị viên (`is_admin = 1`).
- Quản lý sách (CRUD): Thêm mới, chỉnh sửa, xóa sách, tải lên ảnh bìa.
- **Quản lý đơn hàng (`/admin/orders`):** Admin có thể xem danh sách tất cả các đơn hàng của mọi khách hàng và nhanh chóng chuyển đổi qua lại giữa 8 trạng thái đơn hàng.

---

## 🚀 Hướng dẫn cài đặt và chạy ứng dụng

### Bước 1: Khởi tạo Cơ sở dữ liệu (SQL Server)
1. Mở **SQL Server Management Studio (SSMS)**.
2. Mở file `database_24133034.sql` trong thư mục gốc dự án.
3. Thực thi toàn bộ script (`Execute` hoặc nhấn `F5`) để tạo Database `BookStore_24133034`, các bảng và dữ liệu mẫu.

### Bước 2: Cấu hình kết nối CSDL
Nếu máy tính của bạn sử dụng tài khoản hoặc mật khẩu SQL Server khác, hãy chỉnh sửa trong file:  
`src/main/java/vn/iotstar/connection/DBConnection_24133034.java`
```java
private static final String SERVER_NAME = "localhost";
private static final String PORT = "1433";
private static final String DATABASE_NAME = "BookStore_24133034";
private static final String USER_NAME = "sa";
private static final String PASSWORD = "123"; // Mật khẩu SQL Server của bạn
```

### Bước 3: Chạy ứng dụng

#### Cách 1: Chạy bằng lệnh Maven (Nhanh nhất)
Mở Terminal tại thư mục dự án và chạy:
```bash
mvn tomcat7:run
```
Sau khi server khởi động xong, mở trình duyệt truy cập:  
👉 **http://localhost:8080/** hoặc **http://localhost:8080/home**

#### Cách 2: Chạy trên IntelliJ IDEA / Eclipse
- Cấu hình máy chủ **Tomcat Server (Local)**.
- Thêm Deployment: `BookStore_24133034:war exploded` với Application Context là `/`.
- Bấm nút **Run / Debug**.

---

## 🔑 Tài khoản kiểm thử hệ thống

| Phân quyền | Email | Mật khẩu | Chức năng kiểm thử |
| :--- | :--- | :--- | :--- |
| **Khách hàng** | `user1@gmail.com` | `123456` | Giỏ hàng, Đặt hàng COD, Lịch sử 8 trạng thái đơn |
| **Quản trị viên** | `admin@bookstore.vn` | `123456` | Quản lý sách, Quản lý & đổi trạng thái đơn hàng |

---

## 🧪 Hướng dẫn thay đổi trạng thái trong SQL Server để quan sát

Bạn có thể mở SQL Server và chạy các lệnh SQL mẫu dưới đây để đổi trạng thái đơn hàng số 1 (hoặc mã đơn bất kỳ), sau đó tải lại trang `/order-history` để quan sát đơn hàng tự động chuyển sang tab tương ứng:

```sql
USE BookStore_24133034;
GO

-- 1. Đổi sang 'Đã xác nhận'
UPDATE dbo.orders SET status = N'Đã xác nhận' WHERE order_id = 1;

-- 2. Đổi sang 'Chuẩn bị hàng'
UPDATE dbo.orders SET status = N'Chuẩn bị hàng' WHERE order_id = 1;

-- 3. Đổi sang 'Vận chuyển'
UPDATE dbo.orders SET status = N'Vận chuyển' WHERE order_id = 1;

-- 4. Đổi sang 'Giao hàng'
UPDATE dbo.orders SET status = N'Giao hàng' WHERE order_id = 1;

-- 5. Đổi sang 'Đã giao'
UPDATE dbo.orders SET status = N'Đã giao' WHERE order_id = 1;

-- 6. Đổi sang 'Đơn hàng hủy'
UPDATE dbo.orders SET status = N'Đơn hàng hủy' WHERE order_id = 1;

-- 7. Đổi sang 'Đơn hàng hoàn'
UPDATE dbo.orders SET status = N'Đơn hàng hoàn' WHERE order_id = 1;

-- 8. Trở về 'Đơn hàng mới'
UPDATE dbo.orders SET status = N'Đơn hàng mới' WHERE order_id = 1;
```

---

## 📂 Cấu trúc thư mục dự án

```text
BookStore_24133034/
├── database_24133034.sql       # Script khởi tạo Database & Dữ liệu mẫu
├── pom.xml                     # Cấu hình Maven Dependencies & Plugins
├── README.md                   # Tài liệu hướng dẫn dự án
└── src/
    ├── main/
    │   ├── java/vn/iotstar/
    │   │   ├── connection/     # Kết nối Database JDBC
    │   │   ├── controller/     # Servlet Controllers (Cart, Checkout, OrderHistory,...)
    │   │   ├── dao/            # Data Access Object Interfaces & Impl
    │   │   ├── filter/         # Encoding Filter, Admin Security Filter
    │   │   ├── model/          # Model Entities (Book, Cart, Order, User,...)
    │   │   ├── service/        # Service Layer Interfaces & Impl
    │   │   └── util/           # Constants, Email utilities
    │   └── webapp/
    │       ├── WEB-INF/        # web.xml, sitemesh.xml, decorators.xml
    │       ├── decorators/     # Giao diện khung (user.jsp, admin.jsp)
    │       ├── uploads/        # Ảnh bìa sách
    │       └── views/          # JSP Views (auth, user, admin)
    └── test/                   # JUnit Test Cases tự động
```
