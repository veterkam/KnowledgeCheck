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
  `position` varchar(123),
  `scientific_degree` varchar(123),
  `academicTitle` varchar(123),
  primary key (`id`),
  unique key `id_unique` (`id`),
  FOREIGN KEY (`id`)
    REFERENCES `users` (`id`)
      ON DELETE CASCADE
) ENGINE=InnoDB default charset=utf8 collate = utf8_general_ci;

create table `student_profiles` (
  `id` int(11) not null,
  `specialty` varchar(123),
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
values(1, 'Assitent of Biology', 'Bachelor of Science',  'Bachelor of Biology'),
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
      (3, 'System Programming', 'HP-3000',  4),
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

show tables;
drop table if exists `subjects`;

create table `subjects` (
  `id` int(11) not null auto_increment,
  `name` varchar(50) not null,
  primary key (`id`),
  unique key `name_unique` (`name`)
) ENGINE=InnoDB default charset=utf8 collate = utf8_general_ci;


drop table if exists `answers`;
drop table if exists `questions`;
drop table if exists `tests`;

create table `tests` (
  `id` bigint(11) not null auto_increment primary key,
  `subject_id` int(11),
  `tutor_id` int(11),
  `description` varchar(10000)
) ENGINE=InnoDB default charset=utf8 collate = utf8_general_ci;


create table `questions` (
  `id` bigint(11) not null auto_increment primary key,
  `test_id` bigint(11),
  `description` varchar(1000),
  FOREIGN KEY (`test_id`)
    REFERENCES `tests` (`id`)
      ON DELETE CASCADE
) ENGINE=InnoDB default charset=utf8 collate = utf8_general_ci;

create table `answers` (
  `id` bigint(11) not null auto_increment primary key,
  `question_id` bigint(11),
  `description` varchar(1000),
  `correct` boolean,
  FOREIGN KEY (`question_id`)
    REFERENCES `questions` (`id`)
      ON DELETE CASCADE
) ENGINE=InnoDB default charset=utf8 collate = utf8_general_ci;

insert into `subjects`(`name`)
values('computer science'),
      ('mathematics'),
      ('physics'),
      ('chemistry'),
      ('English'),
      ('history'),
      ('biology'),
      ('geography');

select * from subjects;

insert into `tests`(`subject_id`, `tutor_id`, `description`)
values(7, 7, 'Biology Practice Test'),
      (2, 3, 'TEst teST'),
      (8,11, 'Geography test');

insert into `questions`(`test_id`, `description`)
values(1, 'When the chromosomes line up in mitosis this is known as which phase?'),
      (1, 'Which cellular organelle contains enzymes that are considered digestive?'),
      (1, 'Organs repair themselves through a process of?'),
      (1, 'Which of the following is considered a model for enzyme action?'),
      (1, 'Which of the following statements about enzymes is not true?'),
      (1, 'Which of the following statements about prostaglandins is not true?'),
      (1, 'Cholesterol that is known as (LDL) stands for:'),
      (1, 'Hardening of the arteries is known as:'),
      (1, 'Breathing properly requires the presence of what compound that affects surface tension of alveoli in the lungs?'),
      (1, 'Which of the following is not considered a function of the kidneys?'),
      (1, 'The functional unit of the kidney is known as?'),
      (1, 'What anatomical structure connects the stomach and the mouth?'),
      (2, 'Вопрос А'),
      (2, 'Вопрос B'),
      (2, 'Вопрос C'),
      (2, 'Вопрос D'),
      (3, 'Where might one find the Acropolis?'),
      (3, 'Where is Stonehenge located?'),
      (3, 'Near what Chinese city are the terracotta warriors buried?'),
      (3, 'Where might one go to see giant stone heads?'),
      (3, 'Where is Hadrian’s Wall?'),
      (3, 'Where is the Temple of the Tooth found?'),
      (3, 'Where was the Bastille?'),
      (3, 'Where would one find the Topkapi Palace?'),
      (3, 'In what city would one find the Brandenburg Gate?'),
      (3, 'Where might one find a leaning tower?'),
      (3, 'What is the capital of Pakistan?'),
      (3, 'What is the capital of Armenia?'),
      (3, 'What is the chief port of Israel?'),
      (3, 'What is the capital of Oman?'),
      (3, 'The gateway to the Persian Gulf or Gulf of Iran is the:'),
      (3, 'What is the capital of Saudi Arabia?'),
      (3, 'What is the capital of Tajikistan?'),
      (3, 'The highest mountain in Iran is:'),
      (3, 'Which of these seas does Iran border?'),
      (3, 'On what ocean does Oman lie?');


insert into `answers` (`question_id`, `description`, `correct`)
values(1, 'Telophase ', false),
      (1, 'Anaphase ', false),
      (1, 'Metaphase ', true),
      (1, 'Prophase', false),

      (2, 'Golgi Apparatus ', false),
      (2, 'Lysosomes ', true),
      (2, 'Nucleus ', false),
      (2, 'Ribosomes', false),

      (3, 'Meiosis ', false),
      (3, 'Mitosis ', true),
      (3, 'Cellular differentiation ', false),
      (3, 'Transformation ', false),

      (4, 'Lock and Key model ', true),
      (4, 'Enzyme interaction model', false),
      (4, 'Transformation model ', false),
      (4, 'Transcription model ', false),

      (5, 'Enzymes are catalysts.', false),
      (5, 'Almost all enzymes are proteins.', false),
      (5, 'Enzymes operate most efficiently at optimum pH. ', false),
      (5, 'Enzymes are destroyed during chemical reactions. ', true),

      (6, 'Prostaglandins promote inflammation. ', false),
      (6, 'Prostaglandins can only constrict blood vessels. ', true),
      (6, 'Prostaglandins are made in the renal medulla. ', false),
      (6, 'Prostaglandins can lead to pain and fever.', false),

      (7, 'Low-density lipoproteins', true),
      (7, 'Low-density lysosomes', false),
      (7, 'Level-density lipoproteins', false),
      (7, 'Level-density lysosomes', false),

      (8, 'Atheriosclerosis', true),
      (8, 'Venous narrowing', false),
      (8, 'Micro-circulation', false),
      (8, 'Hypertension', false),

      (9, 'Potassium ', true),
      (9, 'Plasma', false),
      (9, 'Surfactant', false),
      (9, 'Sodium Chloride', false),

      (10, 'Secretion', false),
      (10, 'Reabsorption', false),
      (10, 'Transport', true),
      (10, 'Filtration', false),

      (11, 'Medulla', false),
      (11, 'Glomerulus', false),
      (11, 'Pyramid', false),
      (11, 'Nephron', true),

      (12, 'Trachea', false),
      (12, 'Spinal column', false),
      (12, 'Hepatic duct', false),
      (12, 'Esophagus ', true),

      (13, 'Ответ A1 ', false),
      (13, 'Ответ A2 x', true),
      (13, 'Ответ A3 ', false),
      (13, 'Ответ A4 ', false),
      (13, 'Ответ A5 x', true),

      (14, 'Ответ B1 ', false),
      (14, 'Ответ B2 ', false),
      (14, 'Ответ B3 ', false),
      (14, 'Ответ B4 x', true),

      (15, 'Ответ C1 x', true),
      (15, 'Ответ C2 ', false),
      (15, 'Ответ C3 x', true),
      (15, 'Ответ C4 ', false),

      (16, 'Ответ D1 x', true),
      (16, 'Ответ D2 ', false),
      (16, 'Ответ D3 ', false),
      (16, 'Ответ D4 ', false),

      (17, 'Rome', false),
      (17, 'Beijing', false),
      (17, 'Moscow', false),
      (17, 'Athens', true),

      (18, 'England', true),
      (18, 'Estonia', false),
      (18, 'Stone Mountain', false),
      (18, 'Iceland', false),

      (19, 'Beijing', false),
      (19, 'Xian', true),
      (19, 'Nanjing', false),
      (19, 'Shanghai', false),

      (20, 'The Kremlin', false),
      (20, 'Stonehenge', false),
      (20, 'Cape Canaveral', false),
      (20, 'Easter Island', true),

      (21, 'England', true),
      (21, 'Italy', false),
      (21, 'Yemen', false),
      (21, 'France', false),

      (22, 'India', false),
      (22, 'Thailand', false),
      (22, 'France', false),
      (22, 'Sri Lanka', true),

      (23, 'New York', false),
      (23, 'Paris', true),
      (23, 'Rome', false),
      (23, 'London', false),

      (24, 'Istanbul', true),
      (24, 'Ismir', false),
      (24, 'Baghdad', false),
      (24, 'Berlin', false),

      (25, 'Vienna', false),
      (25, 'Paris', false),
      (25, 'Berlin', true),
      (25, 'London', false),

      (26, 'Pisa', true),
      (26, 'Katmandu', false),
      (26, 'Shanghai', false),
      (26, 'Montreal', false),

      (27, 'Karachi', false),
      (27, 'Lahore', false),
      (27, 'New Delhi', false),
      (27, 'Islamabad', true),

      (28, 'Yaoundé', false),
      (28, 'Yangon', false),
      (28, 'Yakutsk', false),
      (28, 'Yerevan', true),

      (29, 'Havana', false),
      (29, 'Edirna', false),
      (29, 'Aqaba', false),
      (29, 'Haifa', true),

      (30, 'Muscat', true),
      (30, 'Moscow', false),
      (30, 'Mandalay', false),
      (30, 'Montego', false),

      (31, 'Bering Strait', false),
      (31, 'Strait of Hormuz', true),
      (31, 'Strait of Malacca', false),
      (31, 'Torres Strait', false),

      (32, 'Mecca', false),
      (32, 'Cairo', false),
      (32, 'Jeddah', false),
      (32, 'Riyadh', true),

      (33, 'Almaty', false),
      (33, 'Samarqand', false),
      (33, 'Dushanbe', true),
      (33, 'Baku', false),

      (34, 'Mount Everest', false),
      (34, 'Pamir Mountain', false),
      (34, 'Mount Damavand', true),
      (34, 'Lenin Peak', false),

      (35, 'Caspian', true),
      (35, 'Sargasso', false),
      (35, 'Caribbean', false),
      (35, 'Mediterranean', false),

      (36, 'Atlantic', false),
      (36, 'Indian', true),
      (36, 'Southern', false),
      (36, 'Pacific', false);

SELECT * from answers;

select * from tests;

delete FROM `tests` where `id`="2";
select * from answers;
select * from questions;
delete FROM `subjects` where id="7";


select u.lastname as 'Last name', s.name as 'Subject', tests.description as 'Test', q.description as 'Question', a.description as 'Answer' from tests
JOIN users u on u.id = tests.tutor_id
JOIN subjects s on s.id = tests.subject_id
JOIN questions q on tests.id = q.test_id
JOIN answers a on a.question_id = q.id;


