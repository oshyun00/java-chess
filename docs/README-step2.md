### 기능 구현 목록

### 입력

- [x] 체스 게임 시작을 위해 입력해야 할 명령어의 문구를 출력한다.
    ```text
  > 체스 게임을 시작합니다.
  > 게임 시작 : start
  > 게임 종료 : end
  > 게임 상태 : status
  > 게임 이동 : move source위치 target위치 - 예. move b2 b3
    ```

- [x] 체스 게임 시작을 위해서 명령을 입력받는다.
    - [x] 게임 명령은 start, move, status, end로 구성된다.
        - [x] [예외처리] 구성외의 입력은 받을 수 없다.
    - [x] move 명령은 `move {원래 칸} {이동할 칸}` 의 형태로 입력받는다.
        - [x] [예외처리] 구성외의 입력은 받을 수 없다.

<br>

### 출력

- [x] 체스판을 출력한다
- [x] 각 진영의 점수와 출력한다.
  ```text
  > BLACK 점수 : 10.5점
  > WHITE 점수 : 15점
  ```
- [x] 어느 진영이 이겼는지 출력한다.
   ```text
  > 게임 결과 : WHITE 승
   ```

<br>

### 게임 진행

- [x] Application을 동작 할 경우 체스 게임을 생성한다.
    - [x] 최초 상태를 `Ready`로 설정한다.

- [x] start 명령이 들어 올 경우 체크 게임을 실행한다.
    - [x] 상태를 `Running`으로 변경한다.
    - [x] 체스 판을 생성한다.

    - [x] move 명령이 들어 올 경우 체스 말을 이동을 실행한다.
        - [x] 체스 판에 킹이 살아있는지 확인한다.
            - [x] 체스 판에 킹이 살아있지 않은 경우 게임을 종료한다.
            - [x] 상태를 END로 변경한다.
        - [x] 체스 말을 이동한다.
        - [x] 체스 말 이동이 완료되면 Running의 상태를 변경한다.
            - WhiteTurn -> BlackTurn
            - BlackTurn -> WhiteTurn

- [x] Status 명령이 들어 올 경우 현재 각 팀에 생존해 있는 각 기물의 점수의 합을 계산한다.

- [x] END 명령이 들어 올 경우 게임을 종료한다.
    - [x] 상태를 END로 변경한다.

<br>

### 체스판 초기 세팅

- [x] 체스 판은 게임 시작시 초기화 된다.
    - [x] 체스 판은 가로 8, 세로 8의 길이로 구성한다.
    - [x] 체스 말을 배치한다
        - [x] 위 : 대문자 줄 RNBQKBNR , 둘째줄은 P로 이뤄진다
        - [x] 중간: 나머지 부분은 `.`으로 배치한다.
        - [x] 아래 : 소문자 첫줄 p, 둘째줄은 rnbqkbnr

- [x] 각 기물의 최초 위치는 다음과 같다.

```text
  RNBQKBNR  8
  PPPPPPPP  7
  ........  6
  ........  5
  ........  4
  ........  3
  pppppppp  2
  rnbqkbnr  1

  abcdefgh
```

<br>

### 기물 이동 규칙

- 폰(Pawn)
    - 상대방 진영 방향으로 1칸 움직일 수 있다.
    - 최초 시작지점에서는 2칸 움직일 수 있다.
    - 이동 방향의 양쪽 대각선에 상대편 기물이 있을 경우 1칸 움직일 수 있다.

- 룩(Rook)
    - 상,하,좌,우 방향으로 칸 수 제한없이 움직일 수 있다.

- 비숍(Bishop)
    - 상,하,좌,우의 대각선 방향으로 칸 수 제한없이 움직일 수 있다.

- 퀸(Queen)
    - 상,하,좌,우 방향과 대각선 방향으로 칸 수 제한없이 움직일 수 있다.

- 나이트(Knight)
    - 상,하,좌,우 방향으로 1칸 움직인 경우 움직인 방향 기준의 좌, 우 방향으로 2칸 움직일 수 있다.
    - 상,하,좌,우 방향으로 2칸 움직인 경우 움직인 방향 기준의 좌, 우 방향으로 1칸 움직일 수 있다.

- 킹(King)
    - 상,하,좌,우 방향과 대각선 방향으로 1칸 움직일 수 있다.

<br>

### 기물이 이동할 수 없는 규칙

- 공통 규칙
    - 목적지에 우리편 기물이 있는 경우 움직일 수 없다.
    - 이동경로 상대편 혹은 우리편 기물이 있는 경우 움직일 수 없다.
    - 각 기물의 지정된 규칙 이외의 경로로 움직일 수 없다.

- 특수 규칙
    - [나이트] 이동 경로에 상대편 기물이 있는 경우에도 움직일 수 있다.
    - [나이트] 이동 경로에 우리편 기물이 있는 경우에도 움직일 수 있다.

<br>

### 점수 계산

- [x] 각 말들이 점수를 가질 수 있다.
    - [x] 퀸 : 9점
    - [x] 룩 : 5점
    - [x] 비숍 : 3점
    - [x] 나이트 : 2.5점
    - [x] 폰 : 1점
    - [x] 킹 : 0점
- [x] 점수를 계산한다
    - [x] 같은 세로줄에 같은 색의 폰이 있는 경우, 폰은 0.5점이다.

<br>

### DB 설계도

```mysql

CREATE TABLE chess_game
(
    game_id    INT        NOT NULL AUTO_INCREMENT,
    game_state VARCHAR(10) NOT NULL,
    game_turn  VARCHAR(30) NOT NULL,
    
    PRIMARY KEY (game_id)
);

CREATE TABLE chess_board
(
    board_id       INT       NOT NULL AUTO_INCREMENT,
    game_id        INT        NOT NULL,
    board_position VARCHAR(10) NOT NULL,
    board_piece    VARCHAR(10) NOT NULL,
    board_color    VARCHAR(10) NOT NULL,
    
    PRIMARY KEY (board_id)
);
```