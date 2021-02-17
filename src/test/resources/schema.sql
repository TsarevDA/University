DROP TABLE IF EXISTS groups_students;
DROP TABLE IF EXISTS teachers_courses;
DROP TABLE IF EXISTS lessons_groups;
DROP TABLE IF EXISTS lessons;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS lessons_time;
DROP TABLE IF EXISTS auditoriums;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS lessons;


CREATE TABLE groups(
id SERIAL PRIMARY KEY,
name VARCHAR(255));

CREATE TABLE courses(
id SERIAL PRIMARY KEY,
name VARCHAR(255),
description TEXT);

CREATE TABLE lessons_time(
id SERIAL PRIMARY KEY,
order_number INTEGER,
start_time TIME,
end_time TIME,
UNIQUE(order_number));

CREATE TABLE auditoriums(
id SERIAL PRIMARY KEY,
name VARCHAR(255),
capacity INTEGER);

CREATE TABLE students(
id SERIAL PRIMARY KEY,
first_name VARCHAR(255),
last_name VARCHAR(255),
gender VARCHAR(15),
birth_date DATE,
email VARCHAR(255),
phone VARCHAR(255),
address VARCHAR(255));

CREATE TABLE teachers(
id SERIAL PRIMARY KEY,
first_name VARCHAR(255),
last_name VARCHAR(255),
gender VARCHAR(15),
birth_date DATE,
email VARCHAR(255),
phone VARCHAR(255),
address VARCHAR(255));

CREATE TABLE groups_students(
group_id integer REFERENCES groups(id) ON DELETE RESTRICT ON UPDATE CASCADE, 
student_id integer REFERENCES students(id) ON DELETE RESTRICT ON UPDATE CASCADE,
UNIQUE(group_id,student_id));

CREATE TABLE teachers_courses(
teacher_id integer REFERENCES teachers(id) ON DELETE RESTRICT ON UPDATE CASCADE, 
course_id integer REFERENCES courses(id) ON DELETE RESTRICT ON UPDATE CASCADE,
UNIQUE(teacher_id,course_id));

CREATE TABLE lessons(
id SERIAL PRIMARY KEY,
course_id integer REFERENCES courses(id) ON DELETE RESTRICT ON UPDATE CASCADE,
teacher_id INTEGER REFERENCES teachers(id) ON DELETE RESTRICT ON UPDATE CASCADE,
day DATE,
lesson_time_id INTEGER REFERENCES lessons_time(id) ON DELETE RESTRICT ON UPDATE CASCADE,
auditorium_id INTEGER REFERENCES auditoriums(id) ON DELETE RESTRICT ON UPDATE CASCADE);

CREATE TABLE lessons_groups(
lesson_id integer REFERENCES lessons(id) ON DELETE RESTRICT ON UPDATE CASCADE, 
group_id integer REFERENCES groups(id) ON DELETE RESTRICT ON UPDATE CASCADE,
UNIQUE(lesson_id,group_id));

