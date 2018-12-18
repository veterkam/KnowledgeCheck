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


