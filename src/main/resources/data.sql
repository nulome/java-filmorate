INSERT INTO genre (genre) VALUES('COMEDY'), ('DRAMA'), ('CARTOON'), ('THRILLER'), ('DOCUMENTARY'), ('BLOCKBUSTER');

INSERT INTO films (name, description, rating, releasedate, duration)
VALUES ('Godzilla', 'Les', 'G', '2022-01-05', 100), ('PgAdmin', 'adm', 'PG', '2007-01-05', 50),
('DBeaver', 'Bear', 'PG-13', '2013-07-05', 120), ('Linux', 'Win', 'R', '2023-07-05', 35),
('Teatr', 'Raw', 'NC-17', '2019-02-22', 70), ('Cucumber', 'Kif', 'G', '2014-12-12', 170);

INSERT INTO users (login, name, email, birthday)
VALUES ('nulome', 'Roman', 'mmm@mm.mm', '2000-01-01'), ('mazer', 'Masta', 'rr@rr.rr', '1920-04-01'),
('Ass', null, 'gg@gg.gg', '1980-01-21'), ('ford', 'Corp', 'ass@ss.aa', '2017-02-05'),
('sda', 'dtt', 'lmk@km.nm', '2004-04-04'), ('Asura', 'Roman', 'v33@vsd.gg', '2020-05-02');

INSERT INTO film_genres (film_id, genre_id) VALUES (1, 1), (2, 4), (3, 3), (4, 2), (5, 6), (6, 5),
(1, 4), (4, 3), (2, 1), (4, 1), (6, 2), (5, 3), (4, 6);

INSERT INTO friends (id, friend_id) VALUES (3, 2), (3, 1), (3, 4) , (4, 2), (1, 3), (5, 6), (6, 5), (6, 4), (4, 6);

INSERT INTO likes (film_id, user_id) VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (2, 4), (2, 3), (4, 1), (4, 2),
(4, 5), (5, 3), (5, 5), (6, 1), (6, 2), (6, 6);