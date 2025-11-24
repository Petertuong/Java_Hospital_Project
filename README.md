HospitalProject (Desktop JavaFX) — Quick README

Mục tiêu
- Hướng dẫn build và chạy ứng dụng desktop (HMS.jar).
- Nơi cấu hình database.
- Ghi chú sau khi đã gỡ adapter `hospital.project.main`.

Yêu cầu môi trường
- Java 17 (JDK). Kiểm tra `java -version`.
- Maven 3.x
- JavaFX runtime (nếu hệ thống không có JavaFX tích hợp): bạn có thể chạy với `--module-path` + `--add-modules` nếu cần.

Build
1. Vào thư mục project:

```powershell
cd "C:\NetBeansProjects\Java_Hospital_Project-branch01\Java_Hospital_Project-branch01"
```

2. Biên dịch và tạo JAR:

```powershell
mvn -DskipTests clean package
```

- Kết quả: `target\HMS.jar`.

Chạy ứng dụng
- Nếu máy bạn đã có JavaFX runtime trên Classpath/module-path để chạy JavaFX từ JAR, thử:

```powershell
java -jar target\HMS.jar
```

- Nếu gặp lỗi JavaFX (NoClassDefFoundError hoặc javafx.* not found), chạy với JavaFX SDK (thay `C:\path\to\javafx-sdk-<ver>\lib`):

```powershell
java --module-path "C:\path\to\javafx-sdk-<ver>\lib" --add-modules javafx.controls,javafx.fxml -jar target\HMS.jar
```

Database
- DB kết nối ở `src/main/java/util/DBConnect.java`.
- Mở file đó và chỉnh `URL`, `USER`, `PASSWORD` cho phù hợp với MySQL/Postgres hoặc DB đang dùng.
- Trước khi chạy app, hãy chắc rằng DB server đang chạy và schema/tables đã tồn tại (theo script DB của dự án nếu có).

Thay đổi chính đã thực hiện
- Đã refactor controllers để dùng domain model và services (`model.*`, `service.*`).
- Đã xóa package adapter `hospital.project.main` (DTO + ServiceDesktop) vì không còn dùng.
- Thêm `ui/BaseController` (alert helpers) và `ui/util/TableUtil` (centralize table cell factories).
- Thêm UI cho `Diagnosis` (view + controller).

Kiểm tra nhanh sau build
- Nếu build thành công nhưng runtime gặp lỗi tại màn hình nào đó:
  - Mở logs/stacktrace và gửi cho tôi.
  - Kiểm tra `util/DBConnect` và đảm bảo dữ liệu cần thiết (patient SSN, medicine id, v.v.) tồn tại.

Gợi ý git (nếu bạn muốn commit trên máy):
```powershell
git checkout -b cleanup/remove-adapter
git add -A
git commit -m "Remove adapter layer; refactor controllers to domain services; add Diagnosis UI; add README"
```

Tiếp theo tôi có thể:
- Hướng dẫn bạn chạy GUI và test CRUD từng màn.
- Tiếp tục sửa typos và small cleanup (tôi đã để `Documentation & cleanup` là in-progress).

Nếu cần tôi có thể tạo thêm script chạy cho Windows (`run.bat`) hoặc hướng dẫn chi tiết cài JavaFX cho hệ của bạn.
