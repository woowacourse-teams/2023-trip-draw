package dev.tripdraw.trip.query;

import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.dto.TripSearchConditions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.test.fixture.TestFixture.위치정보;
import static dev.tripdraw.test.fixture.TripSearchConditionsFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class TripCustomRepositoryImplTest {

    @Autowired
    private TripCustomRepository tripCustomRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
    }

    @Nested
    class 조건에_따라_여행을_조회할_때 {

        @Nested
        class 개수_제한이 {

            @ParameterizedTest
            @CsvSource({"0, 1", "1, 2", "2, 3"})
            void 여행의_개수보다_적으면_개수_제한보다_하나_더_반환한다(int limit, int expectedSize) {
                // given
                jeju_2023_1_1_Sun();
                jeju_2023_1_1_Sun();
                jeju_2023_1_1_Sun();

                TripPaging tripPaging = new TripPaging(null, limit);

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(emptyTripSearchConditions(), tripPaging);

                // then
                assertThat(trips).hasSize(expectedSize);
            }

            @ParameterizedTest
            @CsvSource({"1, 1", "2, 1"})
            void 여행의_개수보다_많거나_같으면_모든_여행을_반환한다(int limit, int expectedSize) {
                // given
                jeju_2023_1_1_Sun();

                TripPaging tripPaging = new TripPaging(null, limit);

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(emptyTripSearchConditions(), tripPaging);

                // then
                assertThat(trips).hasSize(expectedSize);
            }
        }

        @Nested
        class 마지막으로_조회한_Id를 {

            @Test
            void 입력하지_않으면_가장_최신_여행부터_내림차순으로_반환한다() {
                // given
                Trip jeju_2023_1_1_sun = jeju_2023_1_1_Sun();
                Trip seoul2023_1_1_Sun = seoul_2023_1_1_Sun();
                Trip jeju2023_2_1_Wed = jeju_2023_2_1_Wed();

                Long lastViewedId = null;

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(
                        emptyTripSearchConditions(),
                        new TripPaging(lastViewedId, 10)
                );

                // then
                assertThat(trips).containsExactly(
                        jeju2023_2_1_Wed,
                        seoul2023_1_1_Sun,
                        jeju_2023_1_1_sun
                );
            }

            @Test
            void 입력하면_해당_Id_이하의_최신_여행을_내림차순으로_반환한다() {
                // given
                Trip jeju2023_1_1_Sun = jeju_2023_1_1_Sun();
                Trip seoul2023_1_1_Sun = seoul_2023_1_1_Sun();
                Trip jeju2023_2_1_Wed = jeju_2023_2_1_Wed();

                Long lastViewedId = jeju2023_2_1_Wed.id();

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(
                        emptyTripSearchConditions(),
                        new TripPaging(lastViewedId, 10)
                );

                // then
                assertThat(trips).containsExactly(seoul2023_1_1_Sun, jeju2023_1_1_Sun);
            }
        }

        @Nested
        class 조건으로_연도를 {

            @Test
            void 한_개_설정하면_해당_연도의_여행을_반환한다() {
                // given
                seoul_2022_1_2_Sun();
                Trip jeju2023_1_1_Sun = jeju_2023_1_1_Sun();
                Trip seoul2023_1_1_Sun = seoul_2023_1_1_Sun();
                Trip jeju2023_2_1_Wed = jeju_2023_2_1_Wed();

                TripSearchConditions tripSearchConditions = yearsTripSearchConditions(Set.of(2023));

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(
                        tripSearchConditions,
                        new TripPaging(null, 10)
                );

                // then
                assertThat(trips).containsExactly(jeju2023_2_1_Wed, seoul2023_1_1_Sun, jeju2023_1_1_Sun);
            }

            @Test
            void 여러_개_설정하면_해당_연도들의_여행을_반환한다() {
                // given
                Trip yangyang2021_3_2_Tue = yangyang_2021_3_2_Tue();
                seoul_2022_1_2_Sun();
                Trip jeju2023_1_1_Sun = jeju_2023_1_1_Sun();
                Trip seoul2023_1_1_Sun = seoul_2023_1_1_Sun();
                Trip jeju2023_2_1_Wed = jeju_2023_2_1_Wed();

                TripSearchConditions tripSearchConditions = yearsTripSearchConditions(Set.of(2023, 2021));

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(
                        tripSearchConditions,
                        new TripPaging(null, 10)
                );

                // then
                assertThat(trips).containsExactly(
                        jeju2023_2_1_Wed,
                        seoul2023_1_1_Sun,
                        jeju2023_1_1_Sun,
                        yangyang2021_3_2_Tue
                );
            }
        }

        @Nested
        class 조건으로_월을 {

            @Test
            void 한_개_설정하면_해당_월의_여행을_반환한다() {
                // given
                Trip seoul2022_1_2_Sun = seoul_2022_1_2_Sun();
                Trip jeju2023_1_1_Sun = jeju_2023_1_1_Sun();
                Trip seoul2023_1_1_Sun = seoul_2023_1_1_Sun();
                jeju_2023_2_1_Wed();

                TripSearchConditions tripSearchConditions = monthsTripSearchConditions(Set.of(1));

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(
                        tripSearchConditions,
                        new TripPaging(null, 10)
                );

                // then
                assertThat(trips).containsExactly(seoul2023_1_1_Sun, jeju2023_1_1_Sun, seoul2022_1_2_Sun);
            }

            @Test
            void 여러_개_설정하면_해당_월들의_여행을_반환한다() {
                // given
                Trip yangyang2021_3_2_Tue = yangyang_2021_3_2_Tue();
                Trip seoul2022_1_2_Sun = seoul_2022_1_2_Sun();
                Trip jeju2023_1_1_Sun = jeju_2023_1_1_Sun();
                Trip seoul2023_1_1_Sun = seoul_2023_1_1_Sun();
                jeju_2023_2_1_Wed();

                TripSearchConditions tripSearchConditions = monthsTripSearchConditions(Set.of(1, 3));

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(
                        tripSearchConditions,
                        new TripPaging(null, 10)
                );

                // then
                assertThat(trips).containsExactly(
                        seoul2023_1_1_Sun,
                        jeju2023_1_1_Sun,
                        seoul2022_1_2_Sun,
                        yangyang2021_3_2_Tue
                );
            }
        }

        @Nested
        class 조건으로_요일을 {

            @Test
            void 한_개_설정하면_해당_요일의_여행을_반환한다() {
                // given
                Trip seoul2022_1_2_Sun = seoul_2022_1_2_Sun();
                Trip jeju2023_1_1_Sun = jeju_2023_1_1_Sun();
                Trip seoul2023_1_1_Sun = seoul_2023_1_1_Sun();
                jeju_2023_2_1_Wed();

                TripSearchConditions tripSearchConditions = daysOfWeekTripSearchConditions(Set.of(1));

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(
                        tripSearchConditions,
                        new TripPaging(null, 10)
                );

                // then
                assertThat(trips).containsExactly(seoul2023_1_1_Sun, jeju2023_1_1_Sun, seoul2022_1_2_Sun);
            }

            @Test
            void 여러_개_설정하면_해당_요일들의_여행을_반환한다() {
                // given
                Trip yangyang2021_3_2_Tue = yangyang_2021_3_2_Tue();
                Trip seoul2022_1_2_Sun = seoul_2022_1_2_Sun();
                Trip jeju2023_1_1_Sun = jeju_2023_1_1_Sun();
                Trip seoul2023_1_1_Sun = seoul_2023_1_1_Sun();
                jeju_2023_2_1_Wed();

                TripSearchConditions tripSearchConditions = daysOfWeekTripSearchConditions(Set.of(1, 3));

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(
                        tripSearchConditions,
                        new TripPaging(null, 10)
                );

                // then
                assertThat(trips).containsExactly(
                        seoul2023_1_1_Sun,
                        jeju2023_1_1_Sun,
                        seoul2022_1_2_Sun,
                        yangyang2021_3_2_Tue
                );
            }
        }

        @Nested
        class 조건으로_주소를 {

            @Test
            void 시도_시군구_읍면동_형식으로_입력하면_해당하는_여행을_반환한다() {
                // given
                Trip seoul_songpa_Bangi = seoul_2022_1_2_Sun();
                Trip jejuIsland_jeju_aewol = jeju_2023_1_1_Sun();
                Trip seoul_songpa_sincheon = seoul_2023_1_1_Sun();

                TripSearchConditions tripSearchConditions = addressTripSearchConditions("서울특별시 송파구 신천동");

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(
                        tripSearchConditions,
                        new TripPaging(null, 10)
                );

                // then
                assertThat(trips).containsExactly(seoul_songpa_sincheon);
            }

            @Test
            void 시도_시군구_형식으로_입력하면_해당하는_여행을_반환한다() {
                // given
                Trip seoul_songpa_Bangi = seoul_2022_1_2_Sun();
                Trip jejuIsland_jeju_aewol = jeju_2023_1_1_Sun();
                Trip seoul_songpa_sincheon = seoul_2023_1_1_Sun();

                TripSearchConditions tripSearchConditions = addressTripSearchConditions("서울특별시 송파구");

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(
                        tripSearchConditions,
                        new TripPaging(null, 10)
                );

                // then
                assertThat(trips).containsExactly(seoul_songpa_sincheon, seoul_songpa_Bangi);
            }

            @Test
            void 시도_형식으로_입력하면_해당하는_여행을_반환한다() {
                // given
                Trip seoul_songpa_Bangi = seoul_2022_1_2_Sun();
                Trip jejuIsland_jeju_aewol = jeju_2023_1_1_Sun();
                Trip seoul_songpa_sincheon = seoul_2023_1_1_Sun();

                TripSearchConditions tripSearchConditions = addressTripSearchConditions("서울특별시");

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(
                        tripSearchConditions,
                        new TripPaging(null, 10)
                );

                // then
                assertThat(trips).containsExactly(seoul_songpa_sincheon, seoul_songpa_Bangi);
            }
        }

        @Nested
        class 조건을 {

            @Test
            void 여러_개_설정하면_해당하는_여행을_반환한다() {
                // given
                yangyang_2021_3_2_Tue();
                seoul_2022_1_2_Sun();
                jeju_2023_1_1_Sun();
                Trip seoul2023_1_1_Sun = seoul_2023_1_1_Sun();
                jeju_2023_2_1_Wed();

                TripSearchConditions tripSearchConditions = TripSearchConditions.builder()
                        .years(Set.of(2023))
                        .months(Set.of(1))
                        .address("서울특별시")
                        .build();

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(
                        tripSearchConditions,
                        new TripPaging(null, 10)
                );

                // then
                assertThat(trips).containsExactly(seoul2023_1_1_Sun);
            }

            @Test
            void 설정하지_않으면_모든_여행을_반환한다() {
                // given
                Trip yangyang2021_3_2_Tue = yangyang_2021_3_2_Tue();
                Trip seoul2022_1_2_Sun = seoul_2022_1_2_Sun();
                Trip jeju2023_1_1_Sun = jeju_2023_1_1_Sun();
                Trip seoul2023_1_1_Sun = seoul_2023_1_1_Sun();
                Trip jeju_2023_2_1_Wed = jeju_2023_2_1_Wed();

                TripSearchConditions tripSearchConditions = emptyTripSearchConditions();

                // when
                List<Trip> trips = tripCustomRepository.findAllByConditions(
                        tripSearchConditions,
                        new TripPaging(null, 10)
                );

                // then
                assertThat(trips).containsExactly(
                        jeju_2023_2_1_Wed,
                        seoul2023_1_1_Sun,
                        jeju2023_1_1_Sun,
                        seoul2022_1_2_Sun,
                        yangyang2021_3_2_Tue
                );
            }
        }

        @Test
        void 감상이_없는_여행은_조회되지_않는다() {
            // given
            emptyPostTrip();

            TripSearchConditions tripSearchConditions = emptyTripSearchConditions();
//            TripSearchConditions tripSearchConditions = addressTripSearchConditions("서울특별시");
//            TripSearchConditions tripSearchConditions = daysOfWeekTripSearchConditions(Set.of(2));

            // when
            List<Trip> trips = tripCustomRepository.findAllByConditions(
                    tripSearchConditions,
                    new TripPaging(null, 10)
            );

            // then
            assertThat(trips).isEmpty();
        }

        private Trip jeju_2023_2_1_Wed() {
            Trip trip = Trip.from(member);
            Point point = 위치정보(2023, 2, 1, 1, 1);
            trip.add(point);
            tripRepository.save(trip);
            postRepository.save(new Post("", point, "제주특별자치도 제주시 애월읍", "", member, trip.id()));
            return trip;
        }

        private Trip seoul_2023_1_1_Sun() {
            Trip trip = Trip.from(member);
            Point point = 위치정보(2023, 1, 1, 10, 1);
            trip.add(point);
            tripRepository.save(trip);
            postRepository.save(new Post("", point, "서울특별시 송파구 신천동", "", member, trip.id()));
            return trip;
        }

        private Trip jeju_2023_1_1_Sun() {
            Trip trip = Trip.from(member);
            Point point = 위치정보(2023, 1, 1, 1, 1);
            trip.add(point);
            tripRepository.save(trip);
            postRepository.save(new Post("", point, "제주특별자치도 제주시 애월읍", "", member, trip.id()));
            return trip;
        }

        private Trip seoul_2022_1_2_Sun() {
            Trip trip = Trip.from(member);
            Point point = 위치정보(2022, 1, 2, 1, 1);
            trip.add(point);
            tripRepository.save(trip);
            postRepository.save(new Post("", point, "서울특별시 송파구 방이동", "", member, trip.id()));
            return trip;
        }

        private Trip yangyang_2021_3_2_Tue() {
            Trip trip = Trip.from(member);
            Point point = 위치정보(2021, 3, 2, 1, 1);
            trip.add(point);
            tripRepository.save(trip);
            postRepository.save(new Post("", point, "강원도 양양군", "", member, trip.id()));
            return trip;
        }

        private Trip emptyPostTrip() {
            Trip trip = Trip.from(member);
            Point point = 위치정보(2021, 3, 2, 1, 1);
            trip.add(point);
            tripRepository.save(trip);
            return trip;
        }
    }

}
