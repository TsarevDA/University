DROP DATABASE IF EXISTS universityTest;
CREATE DATABASE universityTest;
DROP ROLE IF EXISTS testAdmin;
CREATE USER testAdmin with password 'password';
GRANT ALL PRIVILEGES ON DATABASE universityTest TO testadmin;