package finalmission.domain;

import jakarta.persistence.Embeddable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationDate {

    private LocalDate reservationDate;

    public ReservationDate(final LocalDate reservationDate) {
        validateWeekDay(reservationDate);
        this.reservationDate = reservationDate;
    }

    private void validateWeekDay(final LocalDate reservationDate) {
        if (reservationDate.getDayOfWeek() == DayOfWeek.SUNDAY || reservationDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
            throw new IllegalArgumentException("주말 예약 불가능");
        }
    }

    public boolean isEqual(final ReservationDate other) {
        return this.reservationDate.isEqual(other.reservationDate);
    }
}
