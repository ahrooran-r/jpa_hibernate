use spring_jpa;

create table course
(
    id   int          not null auto_increment,
    name varchar(255) not null,
    primary key (id)
);

insert into course(name) value ('Comp. Sci.');

select * from course;