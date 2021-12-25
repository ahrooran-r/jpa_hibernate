use spring_jpa;

drop table course;
create table course
(
    id           int          not null auto_increment,
    name         varchar(255) not null,
    primary key (id),
    created_on   timestamp default now(),
    last_updated timestamp on update now()
);

insert into course(name) value ('Comp. Sci.');

select *
from course;