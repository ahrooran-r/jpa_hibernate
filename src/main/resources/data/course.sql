use spring_jpa;

drop table course;
create table course
(
    id           int          not null auto_increment,
    name         varchar(255) not null,
    primary key (id),
    is_deleted   boolean      not null default false,
    created_on   timestamp             default now(),
    last_updated timestamp on update now()
);

insert into course(name)
values ('Comp. Sci.'),
       ('Arts'),
       ('History'),
       ('Comp. Sci.'),
       ('Commerce');


select *
from course;