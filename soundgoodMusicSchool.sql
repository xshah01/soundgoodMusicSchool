CREATE TABLE instrument (
 instrument_id INT NOT NULL,
 instrument CHAR(10),
 price CHAR(10),
 brand CHAR(10)
);

ALTER TABLE instrument ADD CONSTRAINT PK_instrument PRIMARY KEY (instrument_id);


CREATE TABLE lesson (
 lesson_id INT NOT NULL,
 typeOfLesson VARCHAR(15) NOT NULL,
 level VARCHAR(15) NOT NULL,
 audition CHAR(10),
 genre CHAR(10)
);

ALTER TABLE lesson ADD CONSTRAINT PK_lesson PRIMARY KEY (lesson_id);


CREATE TABLE person (
 person_id INT NOT NULL,
 ssn VARCHAR(12) NOT NULL,
 firstname CHAR(10) NOT NULL,
 lastname CHAR(10) NOT NULL,
 age INT NOT NULL,
 street VARCHAR(50),
 zipcode VARCHAR(10),
 city VARCHAR(20)
);

ALTER TABLE person ADD CONSTRAINT PK_person PRIMARY KEY (person_id);


CREATE TABLE phone (
 phone_no VARCHAR(15) NOT NULL,
 person_id INT NOT NULL
);

ALTER TABLE phone ADD CONSTRAINT PK_phone PRIMARY KEY (phone_no,person_id);


CREATE TABLE student (
 student_id INT NOT NULL,
 person_id INT NOT NULL,
 sibling VARCHAR(10),
 audition VARCHAR(10)
);

ALTER TABLE student ADD CONSTRAINT PK_student PRIMARY KEY (student_id,person_id);


CREATE TABLE student_instrument (
 rental_id INT NOT NULL,
 student_id INT NOT NULL,
 person_id INT NOT NULL,
 instrument_id INT NOT NULL,
 rental_date DATE NOT NULL,
 return_date DATE
);

ALTER TABLE student_instrument ADD CONSTRAINT PK_student_instrument PRIMARY KEY (rental_id,student_id,person_id,instrument_id);


CREATE TABLE email (
 email VARCHAR(50) NOT NULL,
 person_id INT NOT NULL
);

ALTER TABLE email ADD CONSTRAINT PK_email PRIMARY KEY (email,person_id);


CREATE TABLE instructor (
 instructor_id INT NOT NULL,
 person_id INT NOT NULL,
 skill_level VARCHAR(15) NOT NULL
);

ALTER TABLE instructor ADD CONSTRAINT PK_instructor PRIMARY KEY (instructor_id,person_id);


CREATE TABLE student_lesson_instructor (
 student_id INT NOT NULL,
 person_id INT NOT NULL,
 lesson_id INT NOT NULL,
 instructor_id INT NOT NULL,
 start_date DATE NOT NULL,
 price DECIMAL(5) NOT NULL
);

ALTER TABLE student_lesson_instructor ADD CONSTRAINT PK_student_lesson_instructor PRIMARY KEY (student_id,person_id,lesson_id,instructor_id);


ALTER TABLE phone ADD CONSTRAINT FK_phone_0 FOREIGN KEY (person_id) REFERENCES person (person_id);


ALTER TABLE student ADD CONSTRAINT FK_student_0 FOREIGN KEY (person_id) REFERENCES person (person_id);


ALTER TABLE student_instrument ADD CONSTRAINT FK_student_instrument_0 FOREIGN KEY (student_id,person_id) REFERENCES student (student_id,person_id);
ALTER TABLE student_instrument ADD CONSTRAINT FK_student_instrument_1 FOREIGN KEY (instrument_id) REFERENCES instrument (instrument_id);


ALTER TABLE email ADD CONSTRAINT FK_email_0 FOREIGN KEY (person_id) REFERENCES person (person_id);


ALTER TABLE instructor ADD CONSTRAINT FK_instructor_0 FOREIGN KEY (person_id) REFERENCES person (person_id);


ALTER TABLE student_lesson_instructor ADD CONSTRAINT FK_student_lesson_instructor_0 FOREIGN KEY (student_id,person_id) REFERENCES student (student_id,person_id);
ALTER TABLE student_lesson_instructor ADD CONSTRAINT FK_student_lesson_instructor_1 FOREIGN KEY (lesson_id) REFERENCES lesson (lesson_id);
ALTER TABLE student_lesson_instructor ADD CONSTRAINT FK_student_lesson_instructor_2 FOREIGN KEY (instructor_id,person_id) REFERENCES instructor (instructor_id,person_id);


