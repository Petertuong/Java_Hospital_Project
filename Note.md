Các bước để sử dụng JDBC để tương tác cơ sở dữ liệu

Bước 1: tạo kết nối với CSDL
Bước 2: Tạo đối tượng statement
    - Xác định câu lệnh sql:
     + DDL statement: create, alter, etc.
     + DML statement: Select, insert, etc.
     + DCL statement: grant, revoke
     + TCL statement: commit, rollback, savepoint, etc.
    - Tạo đối tượng statement:
     + Statement
     + PreparedStatement
     + CallableStatement
Bước 3: Thực thi statement
    - boolean execute(String SQL) throws SQLException
    - int executeUpdate(String SQL) throws SQLException
    - ResultSet executeQuery(String SQL) throws SQLException
Bước 4: Xử lý kết quả trả về 
Bước 5: ngắt kết nối


Hiểu thế nào về HTTP Rest API

code: 500 means error by unexpected condition -> due to catch exception.
//read
HTTP GET: chỉ dùng để truy xuất (retrieve) dữ liệu [KHÔNG THAY ĐỔI STATE]
    - Response code: 200(OK) ; 404 (NOT FOUND) ; 400 (BAD REQUEST) -> request itself is not corectly formed
    - HTTP GET .../context-path/{id} for eg
//create
HTTP POST: chỉ dùng để tạo mới dữ liệu
    - 201 [CREATED]; 204 [NO CONTENT] the request is succeeded, but has nothing to give back
    - HTTP POST .../context-path/{id}/collection
//update
HTTP PUT: để thay đổi (update) dữ liệu có sẵn (nếu không, sẽ tạo mới)
    - 201 [CREATED] nếu tạo mới dữ liệu; 200[OK] hoặc 204[NO content] nếu update dữ liệu thành công
    - HTTP PUT .../context-path/{id}/collection/{id}

//delete
HTTP DELETE: xóa (delete) dữ liệu:
    - 200 [OK] nếu thành công, 202 (Accepted) nếu hành động được đưa vào hàng chờ, 204 (No Content) nếu hành độgn được thực nhưng phản hồi là rỗng, 404 (NOT FOUND) nếu không tìm thấy dữ liệu
    - HTTP DELETE http://www.appdomain.com/users/123/accounts/456

