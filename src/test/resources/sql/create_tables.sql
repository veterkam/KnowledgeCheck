
create table IF NOT EXISTS `users` (
  `id` bigint(11) not null auto_increment,
  `firstName` varchar(50) not null,
  `lastName` varchar(50) not null,
  `email` varchar(50) not null,
  `role` int(11) not null,
  `username` varchar(50) not null,
  `password` varchar(200) not null,
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  primary key (`id`),
  unique key `username_unique` (`username`)
  ) ENGINE=InnoDB default charset=utf8mb4 collate = utf8mb4_general_ci;

create table IF NOT EXISTS `tutor_profiles` (
  `id` bigint(11) not null,
  `position` varchar(100),
  `scientific_degree` varchar(100),
  `academic_title` varchar(100),
  primary key (`id`),
  unique key `id_unique` (`id`),
  FOREIGN KEY (`id`)
  REFERENCES `users` (`id`)
  ON DELETE CASCADE
  ) ENGINE=InnoDB default charset=utf8mb4 collate = utf8mb4_general_ci;

create table IF NOT EXISTS `student_profiles` (
  `id` bigint(11) not null,
  `specialty` varchar(100),
  `group` varchar(100),
  `year` int(4),
  primary key (`id`),
  unique key `id_unique` (`id`),
  FOREIGN KEY (`id`)
  REFERENCES `users` (`id`)
  ON DELETE CASCADE
  ) ENGINE=InnoDB default charset=utf8mb4 collate = utf8mb4_general_ci;


create table IF NOT EXISTS `subjects` (
  `id` bigint(11) not null auto_increment,
  `name` varchar(100) not null,
  primary key (`id`),
  unique key `name_unique` (`name`)
  ) ENGINE=InnoDB default charset=utf8mb4 collate = utf8mb4_general_ci;


create table IF NOT EXISTS `tests` (
  `id` bigint(11) not null auto_increment primary key,
  `subject_id` int(11),
  `tutor_id` int(11),
  `update_time` datetime  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `title` varchar(100) NOT NULL,
  `description` varchar(500) NOT NULL,
  `timeLimitation` int
  ) ENGINE=InnoDB default charset=utf8mb4 collate = utf8mb4_general_ci;

create table IF NOT EXISTS `questions` (
  `id` bigint(11) not null auto_increment primary key,
  `test_id` bigint(11) not null,
  `description` varchar(500) not null,
  FOREIGN KEY (`test_id`)
  REFERENCES `tests` (`id`)
  ON DELETE CASCADE
  ) ENGINE=InnoDB default charset=utf8mb4 collate = utf8mb4_general_ci;

create table IF NOT EXISTS `answers` (
  `id` bigint(11) not null auto_increment primary key,
  `question_id` bigint(11) not null,
  `description` varchar(500) not null,
  `correct` boolean not null,
  FOREIGN KEY (`question_id`)
  REFERENCES `questions` (`id`)
  ON DELETE CASCADE
  ) ENGINE=InnoDB default charset=utf8mb4 collate = utf8mb4_general_ci;

create table IF NOT EXISTS `testing_results` (
  `student_id` bigint(11) not null,
  `question_id` bigint(11) not null,
  `correct` boolean,
  UNIQUE (`student_id`, `question_id`),
  FOREIGN KEY (`student_id`)
  REFERENCES `users` (`id`)
  ON DELETE CASCADE,
  FOREIGN KEY (`question_id`)
  REFERENCES `questions` (`id`)
  ON DELETE CASCADE
  ) ENGINE=InnoDB default charset=utf8mb4 collate = utf8mb4_general_ci;