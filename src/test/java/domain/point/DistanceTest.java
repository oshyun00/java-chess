package domain.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static testAssistant.creationAssistant.createDistance;
import static testAssistant.creationAssistant.createPoint;

class DistanceTest {

	@Test
	@DisplayName("수직과 수평일 시 테스트")
	void of_VerticalOrHorizontal() {
		Distance expect = createDistance("one");
		Point from = createPoint("a1");
		Point to = createPoint("a2");

		assertThat(Distance.of(from, to)).isEqualTo(expect);
	}

	@Test
	@DisplayName("대각선일 시 테스트")
	void of_Diagonal() {
		Distance expect = createDistance("one");
		Point from = createPoint("a1");
		Point to = createPoint("b2");

		assertThat(Distance.of(from, to)).isEqualTo(expect);
	}

	@Test
	@DisplayName("수직 2칸일 시 테스트")
	void of_VerticalTwo() {
		Distance expect = createDistance("vertical_two");
		Point from = createPoint("a1");
		Point to = createPoint("a3");

		assertThat(Distance.of(from, to)).isEqualTo(expect);
	}

	@Test
	@DisplayName("수평 2칸일 시 Else가 나오는지 테스트")
	void of_HorizontalTwo() {
		Distance expect = createDistance("else");
		Point from = createPoint("a1");
		Point to = createPoint("c1");

		assertThat(Distance.of(from, to)).isEqualTo(expect);
	}

	@Test
	@DisplayName("그 외 Else가 나오는지 테스트")
	void of_Else() {
		Distance expect = createDistance("else");
		Point from = createPoint("a1");
		Point to = createPoint("c2");

		assertThat(Distance.of(from, to)).isEqualTo(expect);
	}
}