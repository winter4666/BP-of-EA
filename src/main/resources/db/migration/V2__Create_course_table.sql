create table course (
    id int unsigned not null AUTO_INCREMENT,
    name varchar(255) not null,
    start_date DATE not null,
    stop_date DATE not null,
    class_times JSON not null,
    capacity int unsigned not null,
    current_student_number int unsigned not null,
    teacher_id int unsigned not null,
    state varchar(16) not null,
    version int unsigned not null,
    PRIMARY KEY (id)
);