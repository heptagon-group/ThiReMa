INSERT INTO t_user
       (username, email, password, telegram_username, telegram_user_id, vat_number)
VALUES
       ('user1', 'daniel@gmail.com', '$2y$10$dhUruuyYXRjh90z.kYIH7u9sOeAWR64mCRtftQw5xtc6qhoKQgBbi', 'DTJ98', NULL, NULL),
       ('user2', 'user2@mail.com', '$2y$10$dhUruuyYXRjh90z.kYIH7u9sOeAWR64mCRtftQw5xtc6qhoKQgBbi', 'user2', NULL, NULL),
       ('user3', 'user3@mail.com', '$2y$10$dhUruuyYXRjh90z.kYIH7u9sOeAWR64mCRtftQw5xtc6qhoKQgBbi', 'user3', NULL, '64323487235'),
       ('user4', 'user4@mail.com', '$2y$10$dhUruuyYXRjh90z.kYIH7u9sOeAWR64mCRtftQw5xtc6qhoKQgBbi', 'user4', NULL, NULL),
       ('user5', 'user5@mail.com', '$2y$10$dhUruuyYXRjh90z.kYIH7u9sOeAWR64mCRtftQw5xtc6qhoKQgBbi', 'user5', NULL, NULL),
       ('user6', 'user6@mail.com', '$2y$10$dhUruuyYXRjh90z.kYIH7u9sOeAWR64mCRtftQw5xtc6qhoKQgBbi', 'user6', NULL, NULL),
       ('user7', 'user7@mail.com', '$2y$10$dhUruuyYXRjh90z.kYIH7u9sOeAWR64mCRtftQw5xtc6qhoKQgBbi', 'user7', NULL, NULL),
       ('user8', 'user8@mail.com', '$2y$10$dhUruuyYXRjh90z.kYIH7u9sOeAWR64mCRtftQw5xtc6qhoKQgBbi', 'user8', NULL, NULL),
       ('user9', 'user9@mail.com', '$2y$10$dhUruuyYXRjh90z.kYIH7u9sOeAWR64mCRtftQw5xtc6qhoKQgBbi', 'user9', NULL, NULL),
       ('user10', 'user10@mail.com', '$2y$10$dhUruuyYXRjh90z.kYIH7u9sOeAWR64mCRtftQw5xtc6qhoKQgBbi', 'user10', NULL, NULL),
       ('admin', 'admin@mail.com', '$2y$10$/ktp3KTDEoSe7gvONj7sh.WXHifap/glCaAfGwxx70cre0x2hHQSO', 'admin', NULL, NULL);

INSERT INTO gateway
       (owner_id)
VALUES
       (1),
       (1),
       (2),
       (3);

INSERT INTO device
       (name, ip_address, brand, model, frequency, gateway_id, owner_id)
VALUES
       ('Sensori taverna', '123.12.1.1', 'Brand1', 'Model1', 60, 1, 1),
       ('Sensori piano terra', '123.12.1.3', 'Brand1', 'Model2', 60, 1, 1),
       ('Acquario', '123.12.1.2', 'Brand1', 'Model3', 60, 1, 1);

INSERT INTO measure_config
       (name, format, threshold, threshold_greater, influential, device_id)
VALUES
       ('Temperatura', 'C', 29, true, true, 1),
       ('Umidità', '%', 60, true, false, 1),
       ('Tensione corrente', 'V', 200, false, false, 1),
       ('Temperatura', 'C', 27, false, true, 2),
       ('Umidità', '%', 60, true, false, 2),
       ('Temperatura', 'C', 27.5, true, true, 3),
       ('PH acqua', 'C', 8, false, false, 3);

INSERT INTO measure_data
       (time, value, measure_id)
VALUES
       ('2020-05-12 11:05:44 GMT+10', 20.0 , 1 ),
       ('2020-05-12 10:05:44 GMT+10', 25.0 , 1 ),
       ('2020-05-12 09:05:44 GMT+10', 23.0 , 1 ),
       ('2020-05-12 08:05:44 GMT+10', 20.0 , 1 ),
       ('2020-05-12 07:05:44 GMT+10', 26.0 , 1 ),
       ('2020-05-12 06:05:44 GMT+10', 19.0 , 1 ),
       ('2020-05-12 05:05:44 GMT+10', 21.0 , 1 ),
       ('2020-05-12 04:05:44 GMT+10', 26.0 , 1 ),
       ('2020-05-12 03:05:44 GMT+10', 22.0 , 1 ),
       ('2020-05-12 02:05:44 GMT+10', 27.0 , 1 ),
       ('2020-05-12 01:05:44 GMT+10', 25.0 , 1 ),
       ('2020-05-12 00:05:44 GMT+10', 22.0 , 1 ),
       ('2020-05-11 23:05:44 GMT+10', 25.0 , 1 ),
       ('2020-05-11 22:05:44 GMT+10', 27.0 , 1 ),
       ('2020-05-11 21:05:44 GMT+10', 22.0 , 1 ),
       ('2020-05-11 20:05:44 GMT+10', 25.0 , 1 ),
       ('2020-05-11 19:05:44 GMT+10', 22.0 , 1 ),
       ('2020-05-11 18:05:44 GMT+10', 22.0 , 1 ),
       ('2020-05-11 17:05:44 GMT+10', 22.0 , 1 ),
       ('2020-05-11 16:05:44 GMT+10', 27.0 , 1 ),
       ('2020-05-11 15:05:44 GMT+10', 18.0 , 1 ),
       ('2020-05-11 14:05:44 GMT+10', 22.0 , 1 ),
       ('2020-05-11 13:05:44 GMT+10', 23.0 , 1 ),
       ('2020-05-11 12:05:44 GMT+10', 24.0 , 1 ),
       ('2020-05-11 11:05:44 GMT+10', 22.0 , 1 ),
       ('2020-05-11 10:05:44 GMT+10', 19.0 , 1 ),
       ('2020-05-11 09:05:44 GMT+10', 23.0 , 1 ),
       ('2020-05-11 08:05:44 GMT+10', 27.0 , 1 ),
       ('2020-05-11 07:05:44 GMT+10', 23.0 , 1 ),
       ('2020-05-11 06:05:44 GMT+10', 27.0 , 1 ),
       ('2020-05-11 05:05:44 GMT+10', 20.0 , 1 ),
       ('2020-05-11 04:05:44 GMT+10', 20.0 , 1 ),
       ('2020-05-11 03:05:44 GMT+10', 23.0 , 1 ),
       ('2020-05-11 02:05:44 GMT+10', 22.0 , 1 ),
       ('2020-05-11 01:05:44 GMT+10', 20.0 , 1 ),
       ('2020-05-11 00:05:44 GMT+10', 22.0 , 1 ),
       ('2020-05-10 23:05:44 GMT+10', 27.0 , 1 ),
       ('2020-05-10 22:05:44 GMT+10', 21.0 , 1 ),
       ('2020-05-10 21:05:44 GMT+10', 27.0 , 1 ),
       ('2020-05-10 20:05:44 GMT+10', 20.0 , 1 ),
       ('2020-05-10 19:05:44 GMT+10', 23.0 , 1 ),
       ('2020-05-10 18:05:44 GMT+10', 21.0 , 1 ),
       ('2020-05-10 17:05:44 GMT+10', 19.0 , 1 ),
       ('2020-05-10 16:05:44 GMT+10', 19.0 , 1 ),
       ('2020-05-10 15:05:44 GMT+10', 19.0 , 1 ),
       ('2020-05-10 14:05:44 GMT+10', 19.0 , 1 ),
       ('2020-05-10 13:05:44 GMT+10', 27.0 , 1 ),
       ('2020-05-10 12:05:44 GMT+10', 23.0 , 1 ),
       ('2020-05-10 11:05:44 GMT+10', 20.0 , 1 ),
       ('2020-05-10 10:05:44 GMT+10', 20.0 , 1 ),
       ('2020-05-10 09:05:44 GMT+10', 23.0 , 1 ),
       ('2020-05-10 08:05:44 GMT+10', 20.0 , 1 ),
       ('2020-05-10 07:05:44 GMT+10', 20.0 , 1 ),
       ('2020-05-10 06:05:44 GMT+10', 28.0 , 1 ),
       ('2020-05-10 05:05:44 GMT+10', 23.0 , 1 ),
       ('2020-05-10 04:05:44 GMT+10', 18.0 , 1 ),
       ('2020-05-10 03:05:44 GMT+10', 19.0 , 1 ),
       ('2020-05-10 02:05:44 GMT+10', 25.0 , 1 ),
       ('2020-05-10 01:05:44 GMT+10', 19.0 , 1 ),
       ('2020-05-10 00:05:44 GMT+10', 19.0 , 1 ),
       ('2020-05-12 11:05:44 GMT+10', 47.0 , 2 ),
       ('2020-05-12 10:05:44 GMT+10', 47.0 , 2 ),
       ('2020-05-12 09:05:44 GMT+10', 54.0 , 2 ),
       ('2020-05-12 08:05:44 GMT+10', 52.0 , 2 ),
       ('2020-05-12 07:05:44 GMT+10', 51.0 , 2 ),
       ('2020-05-12 06:05:44 GMT+10', 46.0 , 2 ),
       ('2020-05-12 05:05:44 GMT+10', 42.0 , 2 ),
       ('2020-05-12 04:05:44 GMT+10', 53.0 , 2 ),
       ('2020-05-12 03:05:44 GMT+10', 41.0 , 2 ),
       ('2020-05-12 02:05:44 GMT+10', 54.0 , 2 ),
       ('2020-05-12 01:05:44 GMT+10', 45.0 , 2 ),
       ('2020-05-12 00:05:44 GMT+10', 40.0 , 2 ),
       ('2020-05-11 23:05:44 GMT+10', 48.0 , 2 ),
       ('2020-05-11 22:05:44 GMT+10', 54.0 , 2 ),
       ('2020-05-11 21:05:44 GMT+10', 48.0 , 2 ),
       ('2020-05-11 20:05:44 GMT+10', 45.0 , 2 ),
       ('2020-05-11 19:05:44 GMT+10', 46.0 , 2 ),
       ('2020-05-11 18:05:44 GMT+10', 51.0 , 2 ),
       ('2020-05-11 17:05:44 GMT+10', 48.0 , 2 ),
       ('2020-05-11 16:05:44 GMT+10', 49.0 , 2 ),
       ('2020-05-11 15:05:44 GMT+10', 45.0 , 2 ),
       ('2020-05-11 14:05:44 GMT+10', 49.0 , 2 ),
       ('2020-05-11 13:05:44 GMT+10', 49.0 , 2 ),
       ('2020-05-11 12:05:44 GMT+10', 46.0 , 2 ),
       ('2020-05-11 11:05:44 GMT+10', 55.0 , 2 ),
       ('2020-05-11 10:05:44 GMT+10', 41.0 , 2 ),
       ('2020-05-11 09:05:44 GMT+10', 42.0 , 2 ),
       ('2020-05-11 08:05:44 GMT+10', 55.0 , 2 ),
       ('2020-05-11 07:05:44 GMT+10', 44.0 , 2 ),
       ('2020-05-11 06:05:44 GMT+10', 42.0 , 2 ),
       ('2020-05-11 05:05:44 GMT+10', 46.0 , 2 ),
       ('2020-05-11 04:05:44 GMT+10', 50.0 , 2 ),
       ('2020-05-11 03:05:44 GMT+10', 54.0 , 2 ),
       ('2020-05-11 02:05:44 GMT+10', 51.0 , 2 ),
       ('2020-05-11 01:05:44 GMT+10', 42.0 , 2 ),
       ('2020-05-11 00:05:44 GMT+10', 48.0 , 2 ),
       ('2020-05-10 23:05:44 GMT+10', 53.0 , 2 ),
       ('2020-05-10 22:05:44 GMT+10', 41.0 , 2 ),
       ('2020-05-10 21:05:44 GMT+10', 53.0 , 2 ),
       ('2020-05-10 20:05:44 GMT+10', 51.0 , 2 ),
       ('2020-05-10 19:05:44 GMT+10', 54.0 , 2 ),
       ('2020-05-10 18:05:44 GMT+10', 45.0 , 2 ),
       ('2020-05-10 17:05:44 GMT+10', 43.0 , 2 ),
       ('2020-05-10 16:05:44 GMT+10', 44.0 , 2 ),
       ('2020-05-10 15:05:44 GMT+10', 43.0 , 2 ),
       ('2020-05-10 14:05:44 GMT+10', 41.0 , 2 ),
       ('2020-05-10 13:05:44 GMT+10', 53.0 , 2 ),
       ('2020-05-10 12:05:44 GMT+10', 43.0 , 2 ),
       ('2020-05-10 11:05:44 GMT+10', 47.0 , 2 ),
       ('2020-05-10 10:05:44 GMT+10', 48.0 , 2 ),
       ('2020-05-10 09:05:44 GMT+10', 47.0 , 2 ),
       ('2020-05-10 08:05:44 GMT+10', 44.0 , 2 ),
       ('2020-05-10 07:05:44 GMT+10', 44.0 , 2 ),
       ('2020-05-10 06:05:44 GMT+10', 54.0 , 2 ),
       ('2020-05-10 05:05:44 GMT+10', 47.0 , 2 ),
       ('2020-05-10 04:05:44 GMT+10', 42.0 , 2 ),
       ('2020-05-10 03:05:44 GMT+10', 41.0 , 2 ),
       ('2020-05-10 02:05:44 GMT+10', 50.0 , 2 ),
       ('2020-05-10 01:05:44 GMT+10', 50.0 , 2 ),
       ('2020-05-10 00:05:44 GMT+10', 47.0 , 2 ),
       ('2020-05-05 12:05:46 GMT+10', 18.0 , 4 ),
       ('2020-05-05 11:05:46 GMT+10', 20.0 , 4 ),
       ('2020-05-05 10:05:46 GMT+10', 23.0 , 4 ),
       ('2020-05-05 09:05:46 GMT+10', 21.0 , 4 ),
       ('2020-05-05 08:05:46 GMT+10', 24.0 , 4 ),
       ('2020-05-05 07:05:46 GMT+10', 25.0 , 4 ),
       ('2020-05-05 06:05:46 GMT+10', 25.0 , 4 ),
       ('2020-05-05 05:05:46 GMT+10', 20.0 , 4 ),
       ('2020-05-05 04:05:46 GMT+10', 23.0 , 4 ),
       ('2020-05-05 03:05:46 GMT+10', 24.0 , 4 ),
       ('2020-05-05 02:05:46 GMT+10', 23.0 , 4 ),
       ('2020-05-05 01:05:46 GMT+10', 18.0 , 4 ),
       ('2020-05-05 00:05:46 GMT+10', 22.0 , 4 ),
       ('2020-05-04 23:05:46 GMT+10', 20.0 , 4 ),
       ('2020-05-04 22:05:46 GMT+10', 24.0 , 4 ),
       ('2020-05-04 21:05:46 GMT+10', 20.0 , 4 ),
       ('2020-05-04 20:05:46 GMT+10', 21.0 , 4 ),
       ('2020-05-04 19:05:46 GMT+10', 23.0 , 4 ),
       ('2020-05-04 18:05:46 GMT+10', 21.0 , 4 ),
       ('2020-05-04 17:05:46 GMT+10', 24.0 , 4 ),
       ('2020-05-04 16:05:46 GMT+10', 19.0 , 4 ),
       ('2020-05-04 15:05:46 GMT+10', 21.0 , 4 ),
       ('2020-05-04 14:05:46 GMT+10', 21.0 , 4 ),
       ('2020-05-04 13:05:46 GMT+10', 24.0 , 4 ),
       ('2020-05-04 12:05:46 GMT+10', 21.0 , 4 ),
       ('2020-05-04 11:05:46 GMT+10', 19.0 , 4 ),
       ('2020-05-04 10:05:46 GMT+10', 23.0 , 4 ),
       ('2020-05-04 09:05:46 GMT+10', 23.0 , 4 ),
       ('2020-05-04 08:05:46 GMT+10', 25.0 , 4 ),
       ('2020-05-04 07:05:46 GMT+10', 24.0 , 4 ),
       ('2020-05-04 06:05:46 GMT+10', 24.0 , 4 ),
       ('2020-05-04 05:05:46 GMT+10', 24.0 , 4 ),
       ('2020-05-04 04:05:46 GMT+10', 25.0 , 4 ),
       ('2020-05-04 03:05:46 GMT+10', 18.0 , 4 ),
       ('2020-05-04 02:05:46 GMT+10', 19.0 , 4 ),
       ('2020-05-04 01:05:46 GMT+10', 20.0 , 4 ),
       ('2020-05-04 00:05:46 GMT+10', 22.0 , 4 ),
       ('2020-05-03 23:05:46 GMT+10', 24.0 , 4 ),
       ('2020-05-03 22:05:46 GMT+10', 25.0 , 4 ),
       ('2020-05-03 21:05:46 GMT+10', 22.0 , 4 ),
       ('2020-05-03 20:05:46 GMT+10', 22.0 , 4 ),
       ('2020-05-03 19:05:46 GMT+10', 20.0 , 4 ),
       ('2020-05-03 18:05:46 GMT+10', 23.0 , 4 ),
       ('2020-05-03 17:05:46 GMT+10', 24.0 , 4 ),
       ('2020-05-03 16:05:46 GMT+10', 19.0 , 4 ),
       ('2020-05-03 15:05:46 GMT+10', 24.0 , 4 ),
       ('2020-05-03 14:05:46 GMT+10', 25.0 , 4 ),
       ('2020-05-03 13:05:46 GMT+10', 22.0 , 4 ),
       ('2020-05-03 12:05:46 GMT+10', 19.0 , 4 ),
       ('2020-05-03 11:05:46 GMT+10', 19.0 , 4 ),
       ('2020-05-03 10:05:46 GMT+10', 23.0 , 4 ),
       ('2020-05-03 09:05:46 GMT+10', 19.0 , 4 ),
       ('2020-05-03 08:05:46 GMT+10', 19.0 , 4 ),
       ('2020-05-03 07:05:46 GMT+10', 23.0 , 4 ),
       ('2020-05-03 06:05:46 GMT+10', 20.0 , 4 ),
       ('2020-05-03 05:05:46 GMT+10', 21.0 , 4 ),
       ('2020-05-03 04:05:46 GMT+10', 22.0 , 4 ),
       ('2020-05-03 03:05:46 GMT+10', 22.0 , 4 ),
       ('2020-05-03 02:05:46 GMT+10', 24.0 , 4 ),
       ('2020-05-03 01:05:46 GMT+10', 23.0 , 4 ),
       ('2020-05-05 12:05:46 GMT+10', 40.0 , 5 ),
       ('2020-05-05 11:05:46 GMT+10', 43.0 , 5 ),
       ('2020-05-05 10:05:46 GMT+10', 49.0 , 5 ),
       ('2020-05-05 09:05:46 GMT+10', 46.0 , 5 ),
       ('2020-05-05 08:05:46 GMT+10', 51.0 , 5 ),
       ('2020-05-05 07:05:46 GMT+10', 46.0 , 5 ),
       ('2020-05-05 06:05:46 GMT+10', 46.0 , 5 ),
       ('2020-05-05 05:05:46 GMT+10', 45.0 , 5 ),
       ('2020-05-05 04:05:46 GMT+10', 43.0 , 5 ),
       ('2020-05-05 03:05:46 GMT+10', 55.0 , 5 ),
       ('2020-05-05 02:05:46 GMT+10', 42.0 , 5 ),
       ('2020-05-05 01:05:46 GMT+10', 42.0 , 5 ),
       ('2020-05-05 00:05:46 GMT+10', 44.0 , 5 ),
       ('2020-05-04 23:05:46 GMT+10', 42.0 , 5 ),
       ('2020-05-04 22:05:46 GMT+10', 43.0 , 5 ),
       ('2020-05-04 21:05:46 GMT+10', 46.0 , 5 ),
       ('2020-05-04 20:05:46 GMT+10', 52.0 , 5 ),
       ('2020-05-04 19:05:46 GMT+10', 45.0 , 5 ),
       ('2020-05-04 18:05:46 GMT+10', 41.0 , 5 ),
       ('2020-05-04 17:05:46 GMT+10', 48.0 , 5 ),
       ('2020-05-04 16:05:46 GMT+10', 40.0 , 5 ),
       ('2020-05-04 15:05:46 GMT+10', 50.0 , 5 ),
       ('2020-05-04 14:05:46 GMT+10', 51.0 , 5 ),
       ('2020-05-04 13:05:46 GMT+10', 55.0 , 5 ),
       ('2020-05-04 12:05:46 GMT+10', 44.0 , 5 ),
       ('2020-05-04 11:05:46 GMT+10', 41.0 , 5 ),
       ('2020-05-04 10:05:46 GMT+10', 52.0 , 5 ),
       ('2020-05-04 09:05:46 GMT+10', 44.0 , 5 ),
       ('2020-05-04 08:05:46 GMT+10', 50.0 , 5 ),
       ('2020-05-04 07:05:46 GMT+10', 50.0 , 5 ),
       ('2020-05-04 06:05:46 GMT+10', 53.0 , 5 ),
       ('2020-05-04 05:05:46 GMT+10', 49.0 , 5 ),
       ('2020-05-04 04:05:46 GMT+10', 54.0 , 5 ),
       ('2020-05-04 03:05:46 GMT+10', 40.0 , 5 ),
       ('2020-05-04 02:05:46 GMT+10', 46.0 , 5 ),
       ('2020-05-04 01:05:46 GMT+10', 44.0 , 5 ),
       ('2020-05-04 00:05:46 GMT+10', 47.0 , 5 ),
       ('2020-05-03 23:05:46 GMT+10', 50.0 , 5 ),
       ('2020-05-03 22:05:46 GMT+10', 48.0 , 5 ),
       ('2020-05-03 21:05:46 GMT+10', 43.0 , 5 ),
       ('2020-05-03 20:05:46 GMT+10', 53.0 , 5 ),
       ('2020-05-03 19:05:46 GMT+10', 44.0 , 5 ),
       ('2020-05-03 18:05:46 GMT+10', 44.0 , 5 ),
       ('2020-05-03 17:05:46 GMT+10', 47.0 , 5 ),
       ('2020-05-03 16:05:46 GMT+10', 40.0 , 5 ),
       ('2020-05-03 15:05:46 GMT+10', 49.0 , 5 ),
       ('2020-05-03 14:05:46 GMT+10', 54.0 , 5 ),
       ('2020-05-03 13:05:46 GMT+10', 51.0 , 5 ),
       ('2020-05-03 12:05:46 GMT+10', 43.0 , 5 ),
       ('2020-05-03 11:05:46 GMT+10', 44.0 , 5 ),
       ('2020-05-03 10:05:46 GMT+10', 41.0 , 5 ),
       ('2020-05-03 09:05:46 GMT+10', 45.0 , 5 ),
       ('2020-05-03 08:05:46 GMT+10', 50.0 , 5 ),
       ('2020-05-03 07:05:46 GMT+10', 51.0 , 5 ),
       ('2020-05-03 06:05:46 GMT+10', 44.0 , 5 ),
       ('2020-05-03 05:05:46 GMT+10', 48.0 , 5 ),
       ('2020-05-03 04:05:46 GMT+10', 42.0 , 5 ),
       ('2020-05-03 03:05:46 GMT+10', 51.0 , 5 ),
       ('2020-05-03 02:05:46 GMT+10', 51.0 , 5 ),
       ('2020-05-03 01:05:46 GMT+10', 48.0 , 5 ),
       ('2020-05-05 10:05:25 GMT+10', 22.0 , 6 ),
       ('2020-05-05 09:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-05 08:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-05 07:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-05 06:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-05 05:05:25 GMT+10', 25.0 , 6 ),
       ('2020-05-05 04:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-05 03:05:25 GMT+10', 25.0 , 6 ),
       ('2020-05-05 02:05:25 GMT+10', 25.0 , 6 ),
       ('2020-05-05 01:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-05 00:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-04 23:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-04 22:05:25 GMT+10', 26.0 , 6 ),
       ('2020-05-04 21:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-04 20:05:25 GMT+10', 26.0 , 6 ),
       ('2020-05-04 19:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-04 18:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-04 17:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-04 16:05:25 GMT+10', 22.0 , 6 ),
       ('2020-05-04 15:05:25 GMT+10', 26.0 , 6 ),
       ('2020-05-04 14:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-04 13:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-04 12:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-04 11:05:25 GMT+10', 28.0 , 6 ),
       ('2020-05-04 10:05:25 GMT+10', 22.0 , 6 ),
       ('2020-05-04 09:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-04 08:05:25 GMT+10', 26.0 , 6 ),
       ('2020-05-04 07:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-04 06:05:25 GMT+10', 25.0 , 6 ),
       ('2020-05-04 05:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-04 04:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-04 03:05:25 GMT+10', 22.0 , 6 ),
       ('2020-05-04 02:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-04 01:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-04 00:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-03 23:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-03 22:05:25 GMT+10', 22.0 , 6 ),
       ('2020-05-03 21:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-03 20:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-03 19:05:25 GMT+10', 25.0 , 6 ),
       ('2020-05-03 18:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-03 17:05:25 GMT+10', 28.0 , 6 ),
       ('2020-05-03 16:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-03 15:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-03 14:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-03 13:05:25 GMT+10', 25.0 , 6 ),
       ('2020-05-03 12:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-03 11:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-03 10:05:25 GMT+10', 28.0 , 6 ),
       ('2020-05-03 09:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-03 08:05:25 GMT+10', 25.0 , 6 ),
       ('2020-05-03 07:05:25 GMT+10', 26.0 , 6 ),
       ('2020-05-03 06:05:25 GMT+10', 26.0 , 6 ),
       ('2020-05-03 05:05:25 GMT+10', 26.0 , 6 ),
       ('2020-05-03 04:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-03 03:05:25 GMT+10', 28.0 , 6 ),
       ('2020-05-03 02:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-03 01:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-03 00:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-02 23:05:25 GMT+10', 26.0 , 6 ),
       ('2020-05-02 22:05:25 GMT+10', 25.0 , 6 ),
       ('2020-05-02 21:05:25 GMT+10', 25.0 , 6 ),
       ('2020-05-02 20:05:25 GMT+10', 22.0 , 6 ),
       ('2020-05-02 19:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-02 18:05:25 GMT+10', 22.0 , 6 ),
       ('2020-05-02 17:05:25 GMT+10', 22.0 , 6 ),
       ('2020-05-02 16:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-02 15:05:25 GMT+10', 28.0 , 6 ),
       ('2020-05-02 14:05:25 GMT+10', 25.0 , 6 ),
       ('2020-05-02 13:05:25 GMT+10', 25.0 , 6 ),
       ('2020-05-02 12:05:25 GMT+10', 25.0 , 6 ),
       ('2020-05-02 11:05:25 GMT+10', 22.0 , 6 ),
       ('2020-05-02 10:05:25 GMT+10', 22.0 , 6 ),
       ('2020-05-02 09:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-02 08:05:25 GMT+10', 28.0 , 6 ),
       ('2020-05-02 07:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-02 06:05:25 GMT+10', 28.0 , 6 ),
       ('2020-05-02 05:05:25 GMT+10', 27.0 , 6 ),
       ('2020-05-02 04:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-02 03:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-02 02:05:25 GMT+10', 25.0 , 6 ),
       ('2020-05-02 01:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-02 00:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-01 23:05:25 GMT+10', 23.0 , 6 ),
       ('2020-05-01 22:05:25 GMT+10', 24.0 , 6 ),
       ('2020-05-05 10:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-05 09:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-05 08:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-05 07:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-05 06:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-05 05:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-05 04:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-05 03:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-05 02:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-05 01:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-05 00:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-04 23:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-04 22:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-04 21:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-04 20:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-04 19:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-04 18:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-04 17:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-04 16:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-04 15:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-04 14:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-04 13:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-04 12:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-04 11:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-04 10:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-04 09:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-04 08:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-04 07:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-04 06:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-04 05:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-04 04:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-04 03:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-04 02:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-04 01:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-04 00:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-03 23:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-03 22:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-03 21:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-03 20:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-03 19:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-03 18:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-03 17:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-03 16:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-03 15:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-03 14:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-03 13:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-03 12:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-03 11:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-03 10:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-03 09:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-03 08:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-03 07:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-03 06:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-03 05:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-03 04:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-03 03:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-03 02:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-03 01:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-03 00:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-02 23:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-02 22:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-02 21:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-02 20:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-02 19:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-02 18:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-02 17:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-02 16:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-02 15:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-02 14:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-02 13:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-02 12:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-02 11:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-02 10:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-02 09:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-02 08:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-02 07:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-02 06:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-02 05:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-02 04:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-02 03:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-02 02:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-02 01:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-02 00:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-01 23:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-01 22:05:25 GMT+10', 7.0 , 7 ),
       /* dati aggiunti il 13 per fare prove */
       ('2020-05-13 10:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-13 09:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-13 08:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-13 07:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-13 06:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-13 05:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-13 04:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-12 03:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-12 02:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-12 01:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-12 00:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-12 23:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-12 22:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-12 21:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-12 20:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-12 19:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-11 18:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-11 17:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-11 16:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-11 15:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-11 14:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-11 13:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-11 12:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-11 11:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-11 10:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-11 09:05:25 GMT+10', 8.0 , 7 ),
       ('2020-05-11 08:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-11 07:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-11 06:05:25 GMT+10', 6.0 , 7 ),
       ('2020-05-11 05:05:25 GMT+10', 7.0 , 7 ),
       ('2020-05-11 04:05:25 GMT+10', 5.0 , 7 ),
       ('2020-05-11 03:05:25 GMT+10', 5.0 , 7 );