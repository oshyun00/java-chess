USE `chess-test`;

DROP TABLE IF EXISTS chess_boards;
DROP TABLE IF EXISTS game_information;

CREATE TABLE IF NOT EXISTS game_information
(
    id                 BIGINT      NOT NULL AUTO_INCREMENT,
    game_name          VARCHAR(10) NOT NULL UNIQUE,
    current_turn_color VARCHAR(5)  NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS chess_boards
(
    id        BIGINT      NOT NULL AUTO_INCREMENT,
    file      VARCHAR(5)  NOT NULL,
    `rank`    TINYINT     NOT NULL,
    type      VARCHAR(10) NOT NULL,
    color     VARCHAR(5)  NOT NULL,
    game_name VARCHAR(10) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (game_name) REFERENCES game_information (game_name) ON DELETE CASCADE
);

INSERT INTO game_information (game_name, current_turn_color)
VALUES ('ella', 'WHITE');

INSERT INTO chess_boards (file, `rank`, type, color, game_name)
VALUES ('a', 1, 'ROOK', 'WHITE', "ella"),
       ('b', 1, 'KNIGHT', 'WHITE', "ella"),
       ('c', 1, 'BISHOP', 'WHITE', "ella"),
       ('d', 1, 'QUEEN', 'WHITE', "ella"),
       ('e', 1, 'KING', 'WHITE', "ella"),
       ('g', 1, 'KNIGHT', 'WHITE', "ella"),
       ('f', 1, 'BISHOP', 'WHITE', "ella"),
       ('h', 1, 'ROOK', 'WHITE', "ella"),
       ('a', 2, 'PAWN', 'WHITE', "ella"),
       ('b', 2, 'PAWN', 'WHITE', "ella"),
       ('c', 2, 'PAWN', 'WHITE', "ella"),
       ('d', 2, 'PAWN', 'WHITE', "ella"),
       ('e', 2, 'PAWN', 'WHITE', "ella"),
       ('f', 2, 'PAWN', 'WHITE', "ella"),
       ('g', 2, 'PAWN', 'WHITE', "ella"),
       ('h', 2, 'PAWN', 'WHITE', "ella"),
       ('a', 8, 'ROOK', 'BLACK', "ella"),
       ('b', 8, 'KNIGHT', 'BLACK', "ella"),
       ('c', 8, 'BISHOP', 'BLACK', "ella"),
       ('d', 8, 'QUEEN', 'BLACK', "ella"),
       ('e', 8, 'KING', 'BLACK', "ella"),
       ('f', 8, 'BISHOP', 'BLACK', "ella"),
       ('g', 8, 'KNIGHT', 'BLACK', "ella"),
       ('h', 8, 'ROOK', 'BLACK', "ella"),
       ('a', 7, 'PAWN', 'BLACK', "ella"),
       ('b', 7, 'PAWN', 'BLACK', "ella"),
       ('c', 7, 'PAWN', 'BLACK', "ella"),
       ('d', 7, 'PAWN', 'BLACK', "ella"),
       ('e', 7, 'PAWN', 'BLACK', "ella"),
       ('f', 7, 'PAWN', 'BLACK', "ella"),
       ('g', 7, 'PAWN', 'BLACK', "ella"),
       ('h', 7, 'PAWN', 'BLACK', "ella");
