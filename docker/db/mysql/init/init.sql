GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost';
FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS `chess` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE DATABASE IF NOT EXISTS `chess-test` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE chess;

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
