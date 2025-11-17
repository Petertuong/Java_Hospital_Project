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