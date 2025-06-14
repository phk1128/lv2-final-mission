package finalmission.external;

import finalmission.dto.HolidayApiResponse;
import finalmission.dto.HolidayResponse;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class HolidayRequesterImpl implements HolidaysRequester {

    private final RestClient restClient;
    private final String serviceKey;

    public HolidayRequesterImpl(final RestClient holidaysRestClient,
                               final @Value("${public.api.key}") String serviceKey) {
        this.restClient = holidaysRestClient;
        this.serviceKey = serviceKey;
    }

    @Override
    public List<HolidayResponse> getHolidays(final LocalDate date) {
        return getHolidaysForMonth(date.getYear(), date.getMonthValue());
    }

    public List<HolidayResponse> getHolidaysForMonth(int year, int month) {
        try {
            HolidayApiResponse response = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/getAnniversaryInfo")
                            .queryParam("ServiceKey", serviceKey)
                            .queryParam("pageNo", 1)
                            .queryParam("numOfRows", 100)
                            .queryParam("solYear", year)
                            .queryParam("solMonth", String.format("%02d", month))
                            .build())
                    .retrieve()
                    .body(HolidayApiResponse.class);

            if (response == null || response.body() == null || response.body().items() == null) {
                return Collections.emptyList();
            }

            return response.body().items().item();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
