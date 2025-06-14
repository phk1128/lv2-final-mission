package finalmission.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import finalmission.dto.ReservationRequest;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void saveFailTest() {
        //given
        final long memberId = 1L;
        final ReservationRequest reservationRequest = new ReservationRequest(LocalDate.of(2025, 12, 25), 8, 10, 4);

        //should
        assertThatThrownBy(() -> reservationService.save(memberId, reservationRequest)).isInstanceOf(IllegalArgumentException.class);
    }
}
