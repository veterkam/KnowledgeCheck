insert into `users`(`firstName`, `lastName`, `email`, `role`, `username`, `password`, `verified`)
SELECT t.* FROM (
  (SELECT 'Bob' as a, 'Smith' as b, 'bob@gmail.com' as c, 1, 'bob' as d, '1000:786d40c39bac1d22d8226b5bf8e85237:f5fa5407b575911a2e0a99362f1c015b2ea033dac835883dd378c3fafde09b0baefced99993e92bb6b37d6174a95ac4a5ffaf8344a082863b61633632b526c36' as e, true as f) UNION ALL
  (SELECT 'Alex', 'McDonald', 'alex@gmail.com', 1, 'alex', '1000:786d40c39bac1d22d8226b5bf8e85237:f5fa5407b575911a2e0a99362f1c015b2ea033dac835883dd378c3fafde09b0baefced99993e92bb6b37d6174a95ac4a5ffaf8344a082863b61633632b526c36', true) UNION ALL
  (SELECT 'Пётр', 'Петрович', 'piter@gmail.com', 1, 'piter', '1000:786d40c39bac1d22d8226b5bf8e85237:f5fa5407b575911a2e0a99362f1c015b2ea033dac835883dd378c3fafde09b0baefced99993e92bb6b37d6174a95ac4a5ffaf8344a082863b61633632b526c36', true) UNION ALL
  (SELECT 'Robert', 'Freeman', 'rob@gmail.com', 2, 'student', '1000:786d40c39bac1d22d8226b5bf8e85237:f5fa5407b575911a2e0a99362f1c015b2ea033dac835883dd378c3fafde09b0baefced99993e92bb6b37d6174a95ac4a5ffaf8344a082863b61633632b526c36', true) UNION ALL
  (SELECT 'Mary', 'Peak', 'mary@gmail.com', 0, 'admin', '1000:786d40c39bac1d22d8226b5bf8e85237:f5fa5407b575911a2e0a99362f1c015b2ea033dac835883dd378c3fafde09b0baefced99993e92bb6b37d6174a95ac4a5ffaf8344a082863b61633632b526c36', true)) t
   WHERE NOT EXISTS (SELECT * FROM `users`);

insert into `tutor_profiles`(`id`, `position`, `scientific_degree`, `academic_title`)
SELECT t.* FROM (
  (SELECT 1, 'Teacher of Geography', 'Bachelor of Science',  'Bachelor of Geography') UNION ALL
  (SELECT 2, 'Senior lecturer of Biology', 'Master of Science',  'Master of Biology') UNION ALL
  (SELECT 3, 'Старший преподаватель информатики', 'Кандидат технических наук',  'Доцент')) t
   WHERE NOT EXISTS (SELECT * FROM `tutor_profiles`);

insert into `student_profiles`(`id`, `specialty`, `group`, `year`)
SELECT t.* FROM (SELECT 4, 'Mathematics', 'M-2016-A',  '2016') t
WHERE NOT EXISTS (SELECT * FROM `student_profiles`);

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


insert into `tests`(`subject_id`, `tutor_id`, `title`, `description`, `timeLimitation`, `update_time`)
SELECT t.* FROM (
      (SELECT 7, 2, 'Biology Practice Test', 'Check you knowledge in biology!', 3600, '2018-10-24 12:00:00') UNION ALL
      (SELECT 2, 1, 'TEst teST', 'Check you knowledge, be smart!', 3600, '2018-10-25 14:40:00') UNION ALL
			(SELECT 8, 1, 'Geography test', 'Check you knowledge in World Geography!', 3600, '2018-10-26 10:15:00') UNION ALL
			(SELECT 1, 3, 'Основы конструкции ЭВМ', 'Проверь свои знания в основах конструкции ЭВМ', 3600, '2018-11-26 15:25:00')) t
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
	(SELECT 3, 'On what ocean does Oman lie?') UNION ALL
  (SELECT 4, 'Структура компьютера — это') UNION ALL
  (SELECT 4, 'Основная функция ЭВМ') UNION ALL
  (SELECT 4, 'Персональный компьютер состоит из') UNION ALL
  (SELECT 4, 'Системный блок включает в себя') UNION ALL
  (SELECT 4, 'Микропроцессор предназначен для') UNION ALL
  (SELECT 4, 'Разрядность микропроцессора — это') UNION ALL
  (SELECT 4, 'От разрядности микропроцессора зависит') UNION ALL
  (SELECT 4, 'Тактовая частота микропроцессора измеряется в') UNION ALL
  (SELECT 4, 'Функции процессора состоят в') UNION ALL
  (SELECT 4, 'Микропроцессоры различаются между собой')) t
  WHERE NOT EXISTS (SELECT * FROM `questions`);


