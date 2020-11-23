DROP DATABASE IF EXISTS university;
CREATE DATABASE university;
DROP ROLE IF EXISTS universityAdmin;
CREATE USER universityAdmin with password 'password';
GRANT ALL PRIVILEGES ON DATABASE university TO universityAdmin;