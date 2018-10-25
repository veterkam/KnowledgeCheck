DROP table IF EXISTS `tutor_profiles`;
DROP table IF EXISTS `student_profiles`;
DROP table IF EXISTS `users`;

create table `users` (
  `id` int(11) not null auto_increment,
  `firstname` varchar(20) not null,
  `lastname` varchar(20) not null,
  `email` varchar(50) not null,
  `role` int(11) not null,
  `username` varchar(20) not null,
  `password` varchar(30) not null,
  primary key (`id`),
  unique key `username_unique` (`username`)
) ENGINE=InnoDB default charset=utf8 collate = utf8_general_ci;

create table `tutor_profiles` (
  `id` int(11) not null,
  `position` varchar(128),
  `scientific_degree` varchar(128),
  `academicTitle` varchar(128),
  primary key (`id`),
  unique key `id_unique` (`id`),
  FOREIGN KEY (`id`)
    REFERENCES `users` (`id`)
      ON DELETE CASCADE
) ENGINE=InnoDB default charset=utf8 collate = utf8_general_ci;

create table `student_profiles` (
  `id` int(11) not null,
  `specialty` varchar(128),
  `group` varchar(10),
  `year` int(1),
  primary key (`id`),
  unique key `id_unique` (`id`),
  FOREIGN KEY (`id`)
    REFERENCES `users` (`id`)
      ON DELETE CASCADE
) ENGINE=InnoDB default charset=utf8 collate = utf8_general_ci;

insert into `users`(`firstname`, `lastname`, `email`, `role`, `username`, `password`)
VALUES('Bob', 'Smith', 'bob@gmail.com', 1, 'bob', '123'),
      ('Alex', 'McDonald', 'alex@gmail.com', 2, 'alex', '123'),
      ('Mary', 'Peak', 'mary@gmail.com', 1, 'mary', '123'),
      ('Ann', 'Angry', 'ann@gmail.com', 2, 'ann', '123'),
      ('Jack', 'Crazy', 'jack@gmail.com', 1, 'jack', '123'),
      ('Nelson', 'Mandela', 'nelson@gmail.com', 2, 'nelson', '123'),
      ('Berny', 'Black', 'barny@gmail.com', 1, 'barny', '123'),
      ('Den', 'Forest', 'den@gmail.com', 2, 'den', '123'),
      ('Mike', 'Sky', 'mike@gmail.com', 1, 'mike', '123'),
      ('Iren', 'Gamp', 'iren0@gmail.com', 1, 'iren', '123'),
      ('Fredy', 'White', 'fredy@gmail.com', 1, 'fredy', '123'),
      ('John', 'McKey', 'john@gmail.com', 2, 'john', '123'),
      ('Lucy', 'Love', 'lucy@gmail.com', 2, 'lucy', '123'),
      ('Piter', 'Hangry', 'piter@gmail.com', 2, 'piter', '123');


insert into `tutor_profiles`(`id`, `position`, `scientific_degree`, `academicTitle`)
values(1, 'Assitent of Math', 'Bachelor of Science',  'Bachelor of Mathematics'),
      (3, 'Teacher of Math', 'Bachelor of Science',  'Bachelor of Mathematics'),
      (5, 'Senior lecturer of Physics', 'Master of Science',  'Master of Physics'),
      (7, 'Professor of Chemistry', 'Doctor of Philosophy',  'Doctor of chemistry'),
      (9, 'Head of department', 'Doctor of Medicine',  'Doctor of chemistry'),
      (10, 'Dean of the faculty', 'Doctor of Science',  'Doctor of Mathematics'),
      (11, 'Senior lecturer of Geography', 'Master of Science',  'Master of Geography');

insert into `student_profiles`(`id`, `specialty`, `group`, `year`)
values(2, 'Mathematics', 'PM-033',  1),
      (4, 'Physics', 'PR-2017',  2),
      (6, 'Chemistry & Physics', 'LG-99',  3),
      (8, 'System Programming', 'HP-3000',  4),
      (12, 'Biology', 'NY-2019',  5),
      (14, 'Geography', 'Line-2',  16);


select * from `users`;

select `users`.`id`,
       `firstname`,
       `lastname`,
       `email`,
       `role`,
       `username`,
       `password`,
       `position`,
       `scientific_degree`,
       `academicTitle`
from users
       inner join tutor_profiles on users.id = tutor_profiles.id
UNION ALL
select `users`.`id`,
       `firstname`,
       `lastname`,
       `email`,
       `role`,
       `username`,
       `password`,
       `specialty`,
       `group`,
       `year`
from users
       inner join student_profiles on users.id = student_profiles.id;

select `users`.`id`,
       `firstname`,
       `lastname`,
       `email`,
       `role`,
       `username`,
       `password`,
       `specialty`,
       `group`,
       `year`
from users
       left join student_profiles on users.id = student_profiles.id
where role = 2;


delete from users where email LIKE "%@mail.%";
select * from users;






