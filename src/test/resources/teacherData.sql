INSERT INTO teachers(first_name, last_name, gender, birth_date, email, phone, address) VALUES('Ivan', 'Ivanov', 'MALE', '1990-01-01', 'mail@mail.ru', '88008080', 'Ivanov street, 25-5');
INSERT INTO teachers(first_name, last_name, gender, birth_date, email, phone, address) VALUES('Petr', 'Ivanov', 'MALE', '1992-05-03', 'mail11111@mail.ru', '880899908080', 'Petrov street, 25-5');
INSERT INTO courses(name,description) VALUES('Math', 'Science about numbers');
INSERT INTO courses(name,description) VALUES('Astronomy','Science about stars and deep space');
INSERT INTO teachers_courses(teacher_id, course_id) VALUES(1,1);
INSERT INTO teachers_courses(teacher_id, course_id) VALUES(2,2);