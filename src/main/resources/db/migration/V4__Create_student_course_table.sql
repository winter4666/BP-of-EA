create table student_course (
    student_id int unsigned not null,
    course_id int unsigned not null,
    PRIMARY KEY (student_id, course_id)
);