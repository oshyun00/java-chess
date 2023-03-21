package chess.service;

import static chess.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ChessServiceTest {

	private ChessService service;

	@BeforeEach
	void beforeEach() {
		service = new ChessService();
	}

	@Nested
	@DisplayName("서비스 상태 관련 테스트")
	class ServiceStateTest {

		@Test
		@DisplayName("초기화 이전에 move를 시도할 경우 예외가 발생해야 한다.")
		void moveBeforeInitializeErrorTest() {
			Exception e = assertThrows(IllegalStateException.class,
				() -> service.movePiece(B1, B3));
			assertEquals("현재 상태에서 불가능한 명령입니다.", e.getMessage());
		}

		@Test
		@DisplayName("초기화 이후에는 각 팀이 폰을 두 칸 전진시킬 수 있어야 한다.")
		void movePawnForward2EachTest() {
			service.initialize();

			assertDoesNotThrow(() -> {
				service.movePiece(B2, B4);
				service.movePiece(B7, B5);
			});
		}
	}

	@Nested
	@DisplayName("게임 진행 관련 테스트")
	class GameFlowTest {

		/*
		최종 상태
		R.BQKBNR
		PPPPPPPP
		........
		........
		.p......
		........
		p..ppppp
		rnbqkbnr
		 */
		@Test
		@DisplayName("나이트의 폰 뛰어넘기, 폰의 두 칸과 한 칸 이동, 나이트의 말 잡기, 폰의 대각선 이동을 통한 말 잡기 내용을 포함한 테스트")
		void gameFlowTest1() {
			service.initialize();

			assertDoesNotThrow(() -> {
				service.movePiece(B2, B4);
				service.movePiece(B8, C6);
				service.movePiece(C2, C3);
				service.movePiece(C6, B4);
				service.movePiece(C3, B4);
			});
		}

		/*
		아래의 예외 발생 직전 상태에서 흑의 우측 비숍이 좌하단으로 바로 내려와 예외가 발생한다.
		RNBQKBNR
		PPPPPPP.
		........
		.......P
		......p.
		.......b
		pppppp.p
		rnbqk.nr
		 */
		@Test
		@DisplayName("폰의 초기 두 칸 및 한 칸 이동, 비숍의 정상 대각 이동과 장애물로 인한 예외 발생 테스트")
		void gameFlowTest2() {
			service.initialize();

			assertDoesNotThrow(() -> {
				service.movePiece(G2, G4);
				service.movePiece(H7, H5);
				service.movePiece(F1, H3);
			});

			Exception e = assertThrows(IllegalArgumentException.class,
				() -> service.movePiece(F8, A3));
			assertEquals("말이 이동하려는 방향에 장애물이 있습니다.", e.getMessage());
		}
	}
}