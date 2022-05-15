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

INSERT INTO RESTAURANT (NAME)
VALUES
       ('BELORYSKIY'),
       ('ASIATKIY'),
       ('ARGENTINSKIY');

INSERT INTO VOTE (USER_ID, REST_ID, VOTE_DATE)
VALUES (1, 1, now() - interval 1 day),
       (2, 1, now() - interval 1 day),
       (3, 2, now() - interval 1 day),
       (4, 2, now() - interval 1 day),
       (5, 3, now() - interval 1 day),
       (6, 3, now() - interval 1 day),

       (2, 1, now()),
       (3, 1, now()),
       (4, 2, now()),
       (5, 2, now()),
       (6, 2, now());

INSERT INTO DISH (NAME, PRICE, REST_ID, LUNCH_DATE)
VALUES ('JUK', 10, 1, now() - interval 1 day),
       ('BOBI', 20, 1, now() - interval 1 day),
       ('KENGURU', 90, 2, now() - interval 1 day),
       ('MEDUZA', 20, 2, now() - interval 1 day),
       ('LEPECHKA', 10, 3, now() - interval 1 day),
       ('POHLEBKA', 30, 3, now() - interval 1 day),

       ('HACHAPURI', 50, 1, now()),
       ('LEPECHKA', 20, 1, now()),
       ('KASHA', 40, 2, now()),
       ('KOMPOT', 20, 2, now()),
       ('SHASHLIK', 80, 3, now()),
       ('BANAN', 10, 3, now()),

       ('PURE', 20, 1, now() + interval 1 day),
       ('SIRNIKI', 30, 1, now() + interval 1 day),
       ('LAVASH', 10, 2, now() + interval 1 day),
       ('BARAN', 70, 2, now() + interval 1 day),
       ('KREVETKA', 20, 3, now() + interval 1 day),
       ('MASAMORA', 30, 3, now() + interval 1 day);