

-- Insert for Admin table
INSERT INTO admin (name, email, password)
VALUES
    ('admin', 'admin@gmail.com', 'admin123');



-- Insert for Users table
INSERT INTO users (username, email, password, created_at)
VALUES
    ('alparslan', 'alparslan@gmail.com', 'alparslan123', CURRENT_TIMESTAMP),
    ('ozan', 'ozan@gmail.com', 'ozan123', CURRENT_TIMESTAMP),
    ('sahin', 'sahin@gmail.com', 'sahin123', CURRENT_TIMESTAMP),
    ('ahmet', 'ahmet@gmail.com', 'ahmet123', CURRENT_TIMESTAMP),
    ('mehmet', 'mehmet@gmail.com', 'mehmet123', CURRENT_TIMESTAMP),
    ('ayse', 'ayse@gmail.com', 'ayse123', CURRENT_TIMESTAMP),
    ('fatma', 'fatma@gmail.com', 'fatma123', CURRENT_TIMESTAMP),
    ('hayriye', 'hayriye@gmail.com', 'hayriye123', CURRENT_TIMESTAMP),
    ('ali', 'ali@gmail.com', 'ali123', CURRENT_TIMESTAMP),
    ('recep', 'recep@gmail.com', 'recep123', CURRENT_TIMESTAMP);



-- Insert for Events table
INSERT INTO events (user_id, event_name, event_date, created_at)
VALUES
    (1, 'Sinema Şöleni', '2025-01-15', CURRENT_TIMESTAMP),
    (2, 'Film Rüzgarı', '2025-01-16', CURRENT_TIMESTAMP),
    (3, 'Yıldızlı Gece', '2025-01-17', CURRENT_TIMESTAMP),
    (4, 'Popcorn Partisi', '2025-01-18', CURRENT_TIMESTAMP),
    (1, 'Perde Arkası Akşamı', '2025-01-19', CURRENT_TIMESTAMP),
    (1, 'Film Tutkunları Buluşması', '2025-01-20', CURRENT_TIMESTAMP),
    (2, 'Film ve Sohbet Gecesi', '2025-01-21', CURRENT_TIMESTAMP),
    (2, 'Unutulmaz Filmler', '2025-01-22', CURRENT_TIMESTAMP),
    (1, 'Son Perde', '2025-01-23', CURRENT_TIMESTAMP),
    (1, 'Işıkların Altında', '2025-01-24', CURRENT_TIMESTAMP);


-- Insert for Invitation table
INSERT INTO invitations (event_id, user_id, status)
VALUES
    (1, 6, 'PENDING'),
    (2, 7, 'PENDING'),
    (1, 8, 'PENDING'),
    (1, 9, 'PENDING'),
    (1, 2, 'PENDING'),
    (2, 3, 'PENDING'),
    (2, 4, 'PENDING'),
    (3, 1, 'PENDING'),
    (4, 1, 'PENDING'),
    (5, 5, 'PENDING');


-- Insert for Movies table
INSERT INTO movies (title, genre, release_date, rating)
VALUES
    ('Zor Ölüm 5', 'Aksiyon', '2025-01-01',0),
    ('Piyanist', 'Drama', '2025-01-02', 0),
    ('Kolpaçino', 'Komedi', '2025-01-03', 0),
    ('Nefesini Tut', 'Gerilim', '2025-01-04', 0),
    ('Korku Seansı', 'Korku', '2025-01-05', 0),
    ('Kara Şövalye Yükselliyor', 'Bilim Kurgu', '2025-01-06', 7.8),
    ('Yukarı Bak', 'Animasyon', '2025-01-07', 6.9),
    ('Fetih 1453', 'Tarih', '2025-01-08', 7.1),
    ('Aşk Sarhoşu', 'Romance', '2025-01-09', 8.3),
    ('Hızlı ve Öfkeli 10', 'Adventure', '2025-01-10', 7.4);


-- Insert for Votes table
INSERT INTO votes (movie_id, user_id, rating, created_at)
VALUES
    (1, 3, 8, CURRENT_TIMESTAMP),
    (2, 4, 7, CURRENT_TIMESTAMP),
    (3, 5, 6, CURRENT_TIMESTAMP),
    (1, 6, 9, CURRENT_TIMESTAMP),
    (1, 7, 8, CURRENT_TIMESTAMP),
    (1, 8, 7, CURRENT_TIMESTAMP),
    (2, 9, 6, CURRENT_TIMESTAMP),
    (2, 10, 7, CURRENT_TIMESTAMP),
    (2, 1, 8, CURRENT_TIMESTAMP),
    (5, 1, 7, CURRENT_TIMESTAMP);


-- Insert for Suggestions table
INSERT INTO suggestions (event_id, user_id, movie_id , created_at)
VALUES
    (1, 1, 1, CURRENT_TIMESTAMP),
    (1, 1, 2, CURRENT_TIMESTAMP),
    (2, 1, 2, CURRENT_TIMESTAMP),
    (3, 2, 1, CURRENT_TIMESTAMP),
    (3, 2, 3, CURRENT_TIMESTAMP),
    (4, 3, 3, CURRENT_TIMESTAMP),
    (5, 4, 4, CURRENT_TIMESTAMP),
    (1, 5, 2, CURRENT_TIMESTAMP),
    (2, 1, 3, CURRENT_TIMESTAMP),
    (1, 2, 1, CURRENT_TIMESTAMP);

