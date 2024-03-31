USE `chess-test`;

DROP TABLE IF EXISTS chess_boards;
DROP TABLE IF EXISTS game_information;
DROP TABLE IF EXISTS pieces;

CREATE TABLE IF NOT EXISTS game_information
(
    id                 BIGINT      NOT NULL AUTO_INCREMENT,
    game_name          VARCHAR(10) NOT NULL UNIQUE,
    current_turn_color VARCHAR(5)  NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pieces
(
    id    TINYINT     NOT NULL AUTO_INCREMENT,
    type  VARCHAR(10) NOT NULL,
    color VARCHAR(5)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (type, color)
);

CREATE TABLE IF NOT EXISTS chess_boards
(
    id        BIGINT      NOT NULL AUTO_INCREMENT,
    file      VARCHAR(5)  NOT NULL,
    `rank`    TINYINT     NOT NULL,
    piece_id  TINYINT     NOT NULL,
    game_name VARCHAR(10) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (piece_id) REFERENCES pieces (id),
    FOREIGN KEY (game_name) REFERENCES game_information (game_name) ON DELETE CASCADE
);

INSERT INTO game_information (game_name, current_turn_color)
VALUES ('ella', 'WHITE'),
       ('ash', 'WHITE');

INSERT INTO pieces (type, color)
VALUES ('ROOK', 'WHITE'),
       ('KNIGHT', 'WHITE'),
       ('BISHOP', 'WHITE'),
       ('QUEEN', 'WHITE'),
       ('KING', 'WHITE'),
       ('PAWN', 'WHITE'),
       ('ROOK', 'BLACK'),
       ('KNIGHT', 'BLACK'),
       ('BISHOP', 'BLACK'),
       ('QUEEN', 'BLACK'),
       ('KING', 'BLACK'),
       ('PAWN', 'BLACK');

INSERT INTO chess_boards (file, `rank`, piece_id, game_name)
VALUES ('a', 1, 1, 'ella'),
       ('b', 1, 2, 'ella'),
       ('c', 1, 3, 'ella'),
       ('d', 1, 4, 'ella'),
       ('e', 1, 5, 'ella'),
       ('f', 1, 3, 'ella'),
       ('g', 1, 2, 'ella'),
       ('h', 1, 1, 'ella'),
       ('a', 2, 6, 'ella'),
       ('b', 2, 6, 'ella'),
       ('c', 2, 6, 'ella'),
       ('d', 2, 6, 'ella'),
       ('e', 2, 6, 'ella'),
       ('f', 2, 6, 'ella'),
       ('g', 2, 6, 'ella'),
       ('h', 2, 6, 'ella'),
       ('a', 8, 7, 'ella'),
       ('b', 8, 8, 'ella'),
       ('c', 8, 9, 'ella'),
       ('d', 8, 10, 'ella'),
       ('e', 8, 11, 'ella'),
       ('f', 8, 9, 'ella'),
       ('g', 8, 8, 'ella'),
       ('h', 8, 7, 'ella'),
       ('a', 7, 12, 'ella'),
       ('b', 7, 12, 'ella'),
       ('c', 7, 12, 'ella'),
       ('d', 7, 12, 'ella'),
       ('e', 7, 12, 'ella'),
       ('f', 7, 12, 'ella'),
       ('g', 7, 12, 'ella'),
       ('h', 7, 12, 'ella');
