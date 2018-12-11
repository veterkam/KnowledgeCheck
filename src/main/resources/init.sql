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
  `group` varchar(10),
  `year` int(4),
  primary key (`id`),
  unique key `id_unique` (`id`),
  FOREIGN KEY (`id`)
    REFERENCES `users` (`id`)
      ON DELETE CASCADE
) ENGINE=InnoDB default charset=utf8mb4 collate = utf8mb4_general_ci;

insert into `users`(`firstName`, `lastName`, `email`, `role`, `username`, `password`, `verified`)
SELECT t.* FROM (
  (SELECT 'Bob' as a, 'Smith' as b, 'bob@gmail.com' as c, 1, 'bob' as d, '1000:786d40c39bac1d22d8226b5bf8e85237:f5fa5407b575911a2e0a99362f1c015b2ea033dac835883dd378c3fafde09b0baefced99993e92bb6b37d6174a95ac4a5ffaf8344a082863b61633632b526c36' as e, true as f) UNION ALL
  (SELECT 'Alex', 'McDonald', 'alex@gmail.com', 1, 'alex', '1000:786d40c39bac1d22d8226b5bf8e85237:f5fa5407b575911a2e0a99362f1c015b2ea033dac835883dd378c3fafde09b0baefced99993e92bb6b37d6174a95ac4a5ffaf8344a082863b61633632b526c36', true) UNION ALL
  (SELECT 'Mary', 'Peak', 'mary@gmail.com', 0, 'mary', '1000:786d40c39bac1d22d8226b5bf8e85237:f5fa5407b575911a2e0a99362f1c015b2ea033dac835883dd378c3fafde09b0baefced99993e92bb6b37d6174a95ac4a5ffaf8344a082863b61633632b526c36', true)) t
   WHERE NOT EXISTS (SELECT * FROM `users`);

insert into `tutor_profiles`(`id`, `position`, `scientific_degree`, `academic_title`)
SELECT t.* FROM (
  (SELECT 1, 'Teacher of Geography', 'Bachelor of Science',  'Bachelor of Geography') UNION ALL
  (SELECT 2, 'Senior lecturer of Biology', 'Master of Science',  'Master of Biology')) t
   WHERE NOT EXISTS (SELECT * FROM `tutor_profiles`);

create table IF NOT EXISTS `subjects` (
  `id` bigint(11) not null auto_increment,
  `name` varchar(100) not null,
  primary key (`id`),
  unique key `name_unique` (`name`)
) ENGINE=InnoDB default charset=utf8mb4 collate = utf8mb4_general_ci;

INSERT INTO `subjects`(`name`)
SELECT t.* FROM (
          (SELECT 'Computer science') UNION ALL
          (SELECT 'Mathematics') UNION ALL
          (SELECT 'Physics') UNION ALL
          (SELECT 'Chemistry') UNION ALL
          (SELECT 'English') UNION ALL
          (SELECT 'History') UNION ALL
          (SELECT 'Biology') UNION ALL
          (SELECT 'Geography')) t
WHERE NOT EXISTS (SELECT * FROM `subjects`);

create table IF NOT EXISTS `tests` (
  `id` bigint(11) not null auto_increment primary key,
  `subject_id` int(11),
  `tutor_id` int(11),
  `update_time` datetime  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `title` varchar(100) NOT NULL,
  `description` varchar(500) NOT NULL
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

insert into `tests`(`subject_id`, `tutor_id`, `title`, `description`, `update_time`)
SELECT t.* FROM (
      (SELECT 7, 2, 'Biology Practice Test', 'Check you knowledge in biology!',  '2018-10-24 12:00:00') UNION ALL
      (SELECT 2, 1, 'TEst teST', 'Check you knowledge, be smart!', '2018-10-25 14:40:00') UNION ALL
      (SELECT 8, 1, 'Geography test', 'Check you knowledge in World Geography!', '2018-10-26 10:15:00')) t
WHERE NOT EXISTS (SELECT * FROM `tests`);

insert into `questions`(`test_id`, `description`)
SELECT t.* FROM (
	(SELECT 1, 'When the chromosomes line up in mitosis this is known as which phase?') UNION ALL
	(SELECT 1, 'Which cellular organelle contains enzymes that are considered digestive?') UNION ALL
	(SELECT 1, 'Organs repair themselves through a process of?') UNION ALL
	(SELECT 1, 'Which of the following is considered a data for enzyme action?') UNION ALL
	(SELECT 1, 'Which of the following statements about enzymes is not true?') UNION ALL
	(SELECT 1, 'Which of the following statements about prostaglandins is not true?') UNION ALL
	(SELECT 1, 'Cholesterol that is known as (LDL) stands for:') UNION ALL
	(SELECT 1, 'Hardening of the arteries is known as:') UNION ALL
	(SELECT 1, 'Breathing properly requires the presence of what compound that affects surface tension of alveoli in the lungs?') UNION ALL
	(SELECT 1, 'Which of the following is not considered a function of the kidneys?') UNION ALL
	(SELECT 1, 'The functional unit of the kidney is known as?') UNION ALL
	(SELECT 1, 'What anatomical structure connects the stomach and the mouth?') UNION ALL
	(SELECT 2, 'Вопрос А') UNION ALL
	(SELECT 2, 'Вопрос B') UNION ALL
	(SELECT 2, 'Вопрос C') UNION ALL
	(SELECT 2, 'Вопрос D') UNION ALL
	(SELECT 3, 'Where might one find the Acropolis?') UNION ALL
	(SELECT 3, 'Where is Stonehenge located?') UNION ALL
	(SELECT 3, 'Near what Chinese city are the terracotta warriors buried?') UNION ALL
	(SELECT 3, 'Where might one go to see giant stone heads?') UNION ALL
	(SELECT 3, 'Where is Hadrian’s Wall?') UNION ALL
	(SELECT 3, 'Where is the Temple of the Tooth found?') UNION ALL
	(SELECT 3, 'Where was the Bastille?') UNION ALL
	(SELECT 3, 'Where would one find the Topkapi Palace?') UNION ALL
	(SELECT 3, 'In what city would one find the Brandenburg Gate?') UNION ALL
	(SELECT 3, 'Where might one find a leaning tower?') UNION ALL
	(SELECT 3, 'What is the capital of Pakistan?') UNION ALL
	(SELECT 3, 'What is the capital of Armenia?') UNION ALL
	(SELECT 3, 'What is the chief port of Israel?') UNION ALL
	(SELECT 3, 'What is the capital of Oman?') UNION ALL
	(SELECT 3, 'The gateway to the Persian Gulf or Gulf of Iran is the:') UNION ALL
	(SELECT 3, 'What is the capital of Saudi Arabia?') UNION ALL
	(SELECT 3, 'What is the capital of Tajikistan?') UNION ALL
	(SELECT 3, 'The highest mountain in Iran is:') UNION ALL
	(SELECT 3, 'Which of these seas does Iran border?') UNION ALL
	(SELECT 3, 'On what ocean does Oman lie?')) t
  WHERE NOT EXISTS (SELECT * FROM `questions`);

insert into `answers` (`question_id`, `description`, `correct`)
SELECT t.* FROM (
  (SELECT 1, 'Telophase ', false) UNION ALL
	(SELECT 1, 'Anaphase ', false) UNION ALL
	(SELECT 1, 'Metaphase ', true) UNION ALL
	(SELECT 1, 'Prophase', false) UNION ALL

	(SELECT 2, 'Golgi Apparatus ', false) UNION ALL
	(SELECT 2, 'Lysosomes ', true) UNION ALL
	(SELECT 2, 'Nucleus ', false) UNION ALL
	(SELECT 2, 'Ribosomes', false) UNION ALL

	(SELECT 3, 'Meiosis ', false) UNION ALL
	(SELECT 3, 'Mitosis ', true) UNION ALL
	(SELECT 3, 'Cellular differentiation ', false) UNION ALL
	(SELECT 3, 'Transformation ', false) UNION ALL

	(SELECT 4, 'Lock and Key data ', true) UNION ALL
	(SELECT 4, 'Enzyme interaction data', false) UNION ALL
	(SELECT 4, 'Transformation data ', false) UNION ALL
	(SELECT 4, 'Transcription data ', false) UNION ALL

	(SELECT 5, 'Enzymes are catalysts.', false) UNION ALL
	(SELECT 5, 'Almost all enzymes are proteins.', false) UNION ALL
	(SELECT 5, 'Enzymes operate most efficiently at optimum pH. ', false) UNION ALL
	(SELECT 5, 'Enzymes are destroyed during chemical reactions. ', true) UNION ALL

	(SELECT 6, 'Prostaglandins promote inflammation. ', false) UNION ALL
	(SELECT 6, 'Prostaglandins can only constrict blood vessels. ', true) UNION ALL
	(SELECT 6, 'Prostaglandins are made in the renal medulla. ', false) UNION ALL
	(SELECT 6, 'Prostaglandins can lead to pain and fever.', false) UNION ALL

	(SELECT 7, 'Low-density lipoproteins', true) UNION ALL
	(SELECT 7, 'Low-density lysosomes', false) UNION ALL
	(SELECT 7, 'Level-density lipoproteins', false) UNION ALL
	(SELECT 7, 'Level-density lysosomes', false) UNION ALL

	(SELECT 8, 'Atheriosclerosis', true) UNION ALL
	(SELECT 8, 'Venous narrowing', false) UNION ALL
	(SELECT 8, 'Micro-circulation', false) UNION ALL
	(SELECT 8, 'Hypertension', false) UNION ALL

	(SELECT 9, 'Potassium ', true) UNION ALL
	(SELECT 9, 'Plasma', false) UNION ALL
	(SELECT 9, 'Surfactant', false) UNION ALL
	(SELECT 9, 'Sodium Chloride', false) UNION ALL

	(SELECT 10, 'Secretion', false) UNION ALL
	(SELECT 10, 'Reabsorption', false) UNION ALL
	(SELECT 10, 'Transport', true) UNION ALL
	(SELECT 10, 'Filtration', false) UNION ALL

	(SELECT 11, 'Medulla', false) UNION ALL
	(SELECT 11, 'Glomerulus', false) UNION ALL
	(SELECT 11, 'Pyramid', false) UNION ALL
	(SELECT 11, 'Nephron', true) UNION ALL

	(SELECT 12, 'Trachea', false) UNION ALL
	(SELECT 12, 'Spinal column', false) UNION ALL
	(SELECT 12, 'Hepatic duct', false) UNION ALL
	(SELECT 12, 'Esophagus ', true) UNION ALL

	(SELECT 13, 'Ответ A1 ', false) UNION ALL
	(SELECT 13, 'Ответ A2 x', true) UNION ALL
	(SELECT 13, 'Ответ A3 ', false) UNION ALL
	(SELECT 13, 'Ответ A4 ', false) UNION ALL
	(SELECT 13, 'Ответ A5 x', true) UNION ALL

	(SELECT 14, 'Ответ B1 ', false) UNION ALL
	(SELECT 14, 'Ответ B2 ', false) UNION ALL
	(SELECT 14, 'Ответ B3 ', false) UNION ALL
	(SELECT 14, 'Ответ B4 x', true) UNION ALL

	(SELECT 15, 'Ответ C1 x', true) UNION ALL
	(SELECT 15, 'Ответ C2 ', false) UNION ALL
	(SELECT 15, 'Ответ C3 x', true) UNION ALL
	(SELECT 15, 'Ответ C4 ', false) UNION ALL

	(SELECT 16, 'Ответ D1 x', true) UNION ALL
	(SELECT 16, 'Ответ D2 ', false) UNION ALL
	(SELECT 16, 'Ответ D3 ', false) UNION ALL
	(SELECT 16, 'Ответ D4 ', false) UNION ALL

	(SELECT 17, 'Rome', false) UNION ALL
	(SELECT 17, 'Beijing', false) UNION ALL
	(SELECT 17, 'Moscow', false) UNION ALL
	(SELECT 17, 'Athens', true) UNION ALL

	(SELECT 18, 'England', true) UNION ALL
	(SELECT 18, 'Estonia', false) UNION ALL
	(SELECT 18, 'Stone Mountain', false) UNION ALL
	(SELECT 18, 'Iceland', false) UNION ALL

	(SELECT 19, 'Beijing', false) UNION ALL
	(SELECT 19, 'Xian', true) UNION ALL
	(SELECT 19, 'Nanjing', false) UNION ALL
	(SELECT 19, 'Shanghai', false) UNION ALL

	(SELECT 20, 'The Kremlin', false) UNION ALL
	(SELECT 20, 'Stonehenge', false) UNION ALL
	(SELECT 20, 'Cape Canaveral', false) UNION ALL
	(SELECT 20, 'Easter Island', true) UNION ALL

	(SELECT 21, 'England', true) UNION ALL
	(SELECT 21, 'Italy', false) UNION ALL
	(SELECT 21, 'Yemen', false) UNION ALL
	(SELECT 21, 'France', false) UNION ALL

	(SELECT 22, 'India', false) UNION ALL
	(SELECT 22, 'Thailand', false) UNION ALL
	(SELECT 22, 'France', false) UNION ALL
	(SELECT 22, 'Sri Lanka', true) UNION ALL

	(SELECT 23, 'New York', false) UNION ALL
	(SELECT 23, 'Paris', true) UNION ALL
	(SELECT 23, 'Rome', false) UNION ALL
	(SELECT 23, 'London', false) UNION ALL

	(SELECT 24, 'Istanbul', true) UNION ALL
	(SELECT 24, 'Ismir', false) UNION ALL
	(SELECT 24, 'Baghdad', false) UNION ALL
	(SELECT 24, 'Berlin', false) UNION ALL

	(SELECT 25, 'Vienna', false) UNION ALL
	(SELECT 25, 'Paris', false) UNION ALL
	(SELECT 25, 'Berlin', true) UNION ALL
	(SELECT 25, 'London', false) UNION ALL

	(SELECT 26, 'Pisa', true) UNION ALL
	(SELECT 26, 'Katmandu', false) UNION ALL
	(SELECT 26, 'Shanghai', false) UNION ALL
	(SELECT 26, 'Montreal', false) UNION ALL

	(SELECT 27, 'Karachi', false) UNION ALL
	(SELECT 27, 'Lahore', false) UNION ALL
	(SELECT 27, 'New Delhi', false) UNION ALL
	(SELECT 27, 'Islamabad', true) UNION ALL

	(SELECT 28, 'Yaoundé', false) UNION ALL
	(SELECT 28, 'Yangon', false) UNION ALL
	(SELECT 28, 'Yakutsk', false) UNION ALL
	(SELECT 28, 'Yerevan', true) UNION ALL

	(SELECT 29, 'Havana', false) UNION ALL
	(SELECT 29, 'Edirna', false) UNION ALL
	(SELECT 29, 'Aqaba', false) UNION ALL
	(SELECT 29, 'Haifa', true) UNION ALL

	(SELECT 30, 'Muscat', true) UNION ALL
	(SELECT 30, 'Moscow', false) UNION ALL
	(SELECT 30, 'Mandalay', false) UNION ALL
	(SELECT 30, 'Montego', false) UNION ALL

	(SELECT 31, 'Bering Strait', false) UNION ALL
	(SELECT 31, 'Strait of Hormuz', true) UNION ALL
	(SELECT 31, 'Strait of Malacca', false) UNION ALL
	(SELECT 31, 'Torres Strait', false) UNION ALL

	(SELECT 32, 'Mecca', false) UNION ALL
	(SELECT 32, 'Cairo', false) UNION ALL
	(SELECT 32, 'Jeddah', false) UNION ALL
	(SELECT 32, 'Riyadh', true) UNION ALL

	(SELECT 33, 'Almaty', false) UNION ALL
	(SELECT 33, 'Samarqand', false) UNION ALL
	(SELECT 33, 'Dushanbe', true) UNION ALL
	(SELECT 33, 'Baku', false) UNION ALL

	(SELECT 34, 'Mount Everest', false) UNION ALL
	(SELECT 34, 'Pamir Mountain', false) UNION ALL
	(SELECT 34, 'Mount Damavand', true) UNION ALL
	(SELECT 34, 'Lenin Peak', false) UNION ALL

	(SELECT 35, 'Caspian', true) UNION ALL
	(SELECT 35, 'Sargasso', false) UNION ALL
	(SELECT 35, 'Caribbean', false) UNION ALL
	(SELECT 35, 'Mediterranean', false) UNION ALL

	(SELECT 36, 'Atlantic', false) UNION ALL
	(SELECT 36, 'Indian', true) UNION ALL
	(SELECT 36, 'Southern', false) UNION ALL
	(SELECT 36, 'Pacific', false)) t
WHERE NOT EXISTS (SELECT * FROM `answers`);

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
