package dev.tripdraw.trip.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.query.TripCustomRepository;
import dev.tripdraw.trip.query.TripPaging;
import dev.tripdraw.trip.query.TripQueryConditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class TripQueryServiceTest {

    @Mock
    private TripCustomRepository tripCustomRepository;

    @InjectMocks
    private TripQueryService tripQueryService;

    @Test
    void 쿼리_조건에_따라_여행을_조회한다() {
        // given
        TripQueryConditions tripQueryConditions = new TripQueryConditions(
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                ""
        );
        TripPaging tripPaging = new TripPaging(1L, 10);

        given(tripCustomRepository.findAllByConditions(tripQueryConditions, tripPaging))
                .willReturn(new ArrayList<>());

        // when
        List<Trip> trips = tripQueryService.readAllByQueryConditions(tripQueryConditions, tripPaging);

        // then
        assertThat(trips).isEmpty();
        then(tripCustomRepository)
                .should(times(1))
                .findAllByConditions(tripQueryConditions, tripPaging);
    }
}
