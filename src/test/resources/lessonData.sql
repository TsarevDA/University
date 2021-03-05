INSERT INTO students(first_name, last_name, gender, birth_date, email, phone, address) VALUES('Ivan', 'Ivanov', 'MALE', '1990-01-01', 'mail@mail.ru', '88008080', 'Ivanov street, 25-5');
INSERT INTO auditoriums(name, capacity) VALUES('First',100);
INSERT INTO groups(name) VALUES('T7-09');
INSERT INTO courses(name,description) VALUES('Astronomy','Science about stars and deep space');
INSERT INTO teachers(first_name, last_name, gender, birth_date, email, phone, address) VALUES('Petr', 'Ivanov', 'MALE', '1992-05-03', 'mail11111@mail.ru', '880899908080', 'Petrov street, 25-5');
INSERT INTO lessons_time(order_number,start_time,end_time) VALUES(2,'09:00','10:00');
INSERT INTO teachers_courses(teacher_id, course_id) VALUES(1,1);
INSERT INTO lessons(course_id, teacher_id, day, lesson_time_id, auditorium_id) VALUES(1,1,'2020-12-08',1,1);
INSERT INTO lessons_groups(lesson_id, group_id) VALUES(1,1);
