INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('User', 'user@gmail.com', '{noop}user'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Masha', 'masha@gmail.com', '{noop}masha'),
       ('Dasha', 'dasha@gmail.com', '{noop}dasha'),
       ('Sasha', 'sasha@gmail.com', '{noop}sasha'),
       ('Pasha', 'pasha@gmail.com', '{noop}pasha');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 3),
       ('USER', 4),
       ('USER', 5),
       ('USER', 6);

INSERT INTO VOTES (USER_ID, RESTAURANT_ID, VOTE_DATE)
VALUES (1, 1, now() - interval 1 day),
       (2, 1, now() - interval 1 day),
       (3, 2, now() - interval 1 day),
       (4, 2, now() - interval 1 day),
       (5, 3, now() - interval 1 day),
       (6, 3, now() - interval 1 day),

       (2, 4, now()),
       (3, 6, now()),
       (4, 4, now()),
       (5, 5, now()),
       (6, 4, now()),

       (2, 8, now() + interval 1 day);

INSERT INTO RESTAURANTS (NAME, LUNCH_DATE)
VALUES ('AFRIKANSKIY', now() - interval 1 day),
       ('AVSTRALIYSKIY', now() - interval 1 day),
       ('SLOVACKIY', now() - interval 1 day),

       ('GRUZINSKIY', now()),
       ('RUSSKIY', now()),
       ('ARMANSKIY', now()),

       ('BELORYSKIY', now() + interval 1 day),
       ('ASIATKIY', now() + interval 1 day),
       ('ARGENTINSKIY', now() + interval 1 day);

INSERT INTO MEALS (NAME, PRICE, RESTAURANT_ID)
VALUES ('JUK', 10, 1),
       ('BOBI', 20, 1),
       ('KENGURU', 90, 2),
       ('MEDUZA', 20, 2),
       ('LEPECHKA', 10, 3),
       ('POHLEBKA', 30, 3),

       ('HACHAPURI', 50, 4),
       ('LEPECHKA', 10, 4),
       ('KASHA', 40, 5),
       ('KOMPOT', 20, 5),
       ('SHAHLIK', 80, 6),
       ('BANAN', 10, 6),

       ('PURE', 20, 7),
       ('SIRNIKI', 30, 7),
       ('LAVASH', 10, 8),
       ('BARAN', 70, 8),
       ('KREVETKA', 20, 9),
       ('MASAMORA', 30, 9);