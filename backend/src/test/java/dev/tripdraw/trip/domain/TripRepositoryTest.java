package dev.tripdraw.trip.domain;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.test.SearchConditionsTestFixture.addressSearchConditions;
import static dev.tripdraw.test.SearchConditionsTestFixture.daysOfWeekSearchConditions;
import static dev.tripdraw.test.SearchConditionsTestFixture.emptySearchConditions;
import static dev.tripdraw.test.SearchConditionsTestFixture.monthsSearchConditions;
import static dev.tripdraw.test.SearchConditionsTestFixture.yearsSearchConditions;
import static dev.tripdraw.test.TestFixture.위치정보;
import static dev.tripdraw.trip.exception.TripExceptionType.TRIP_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.common.config.JpaConfig;
import dev.tripdraw.common.config.QueryDslConfig;
import dev.tripdraw.common.domain.Paging;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.trip.exception.TripException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({JpaConfig.class, QueryDslConfig.class})
@DataJpaTest
class TripRepositoryTest {

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

    @Test
    void 회원_ID로_여행_목록을_조회한다() {
        // given
        Trip trip = new Trip(TripName.from("제주도 여행"), member);
        tripRepository.save(trip);

        // when
        List<Trip> trips = tripRepository.findAllByMemberId(member.id());

        // then
        assertSoftly(softly -> {
            softly.assertThat(trips).hasSize(1);
            softly.assertThat(trips.get(0)).isEqualTo(trip);
        });
    }

    @Test
    void 회원_ID로_여행_목록을_조회할_때_해당_회원의_여행이_없다면_빈_여행_목록을_반환한다() {
        // given & when
        List<Trip> trips = tripRepository.findAllByMemberId(member.id());

        // then
        assertThat(trips).isEmpty();
    }

    @Test
    void 여행_ID를_입력받아_여행_목록과_위치_정보까지_조회한다() {
        // given
        Trip trip = new Trip(TripName.from("제주도 여행"), member);
        Point point1 = new Point(1.1, 2.2, LocalDateTime.now());
        Point point2 = new Point(3.3, 4.4, LocalDateTime.now());
        trip.add(point1);
        trip.add(point2);
        tripRepository.save(trip);

        // when
        Trip savedTrips = tripRepository.getTripWithPoints(trip.id());

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedTrips.points()).containsExactly(point1, point2);
            softly.assertThat(savedTrips).isEqualTo(trip);
        });
    }

    @Test
    void 회원_ID로_여행을_삭제한다() {
        // given
        Trip trip = new Trip(TripName.from("제주도 여행"), member);
        tripRepository.save(trip);

        // when
        tripRepository.deleteByMemberId(member.id());

        // then
        assertThat(tripRepository.findById(trip.id())).isEmpty();
    }

    @Test
    void 여행_ID로_여행을_조회한다() {
        // given
        Trip trip = tripRepository.save(new Trip(TripName.from("제주도 여행"), member));

        // when
        Trip foundTrip = tripRepository.getById(trip.id());

        // then
        assertThat(foundTrip).isEqualTo(trip);
    }

    @Test
    void 여행_ID로_여행을_조회할_때_존재하지_않는_경우_예외를_발생시킨다() {
        // given
        Long wrongId = Long.MIN_VALUE;

        // expect
        assertThatThrownBy(() -> tripRepository.getById(wrongId))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.message());
    }

    @Nested
    class 조건에_따라_여행을_조회할_때 {

        @Nested
        class 개수_제한을_입력하면 {

            @ParameterizedTest
            @CsvSource({"0, 1", "1, 2", "2, 3"})
            void 개수_제한보다_하나_더_반환한다(int limit, int expectedSize) {
                // given
                jeju_2023_1_1_Sun();
                jeju_2023_1_1_Sun();
                jeju_2023_1_1_Sun();

                Paging paging = new Paging(null, limit);

                // when
                List<Trip> trips = tripRepository.findAllByConditions(emptySearchConditions(), paging);

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
                List<Trip> trips = tripRepository.findAllByConditions(emptySearchConditions(),
                        new Paging(lastViewedId, 10));

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
                List<Trip> trips = tripRepository.findAllByConditions(emptySearchConditions(),
                        new Paging(lastViewedId, 10));

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

                SearchConditions searchConditions = yearsSearchConditions(List.of(2023));

                // when
                List<Trip> trips = tripRepository.findAllByConditions(searchConditions, new Paging(null, 10));

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

                SearchConditions searchConditions = yearsSearchConditions(List.of(2023, 2021));

                // when
                List<Trip> trips = tripRepository.findAllByConditions(searchConditions, new Paging(null, 10));

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

                SearchConditions searchConditions = monthsSearchConditions(List.of(1));

                // when
                List<Trip> trips = tripRepository.findAllByConditions(searchConditions, new Paging(null, 10));

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

                SearchConditions searchConditions = monthsSearchConditions(List.of(1, 3));

                // when
                List<Trip> trips = tripRepository.findAllByConditions(searchConditions, new Paging(null, 10));

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

                SearchConditions searchConditions = daysOfWeekSearchConditions(List.of(1));

                // when
                List<Trip> trips = tripRepository.findAllByConditions(searchConditions, new Paging(null, 10));

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

                SearchConditions searchConditions = daysOfWeekSearchConditions(List.of(1, 3));

                // when
                List<Trip> trips = tripRepository.findAllByConditions(searchConditions, new Paging(null, 10));

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

                SearchConditions searchConditions = addressSearchConditions("서울특별시 송파구 신천동");

                // when
                List<Trip> trips = tripRepository.findAllByConditions(searchConditions, new Paging(null, 10));

                // then
                assertThat(trips).containsExactly(seoul_songpa_sincheon);
            }

            @Test
            void 시도_시군구_형식으로_입력하면_해당하는_여행을_반환한다() {
                // given
                Trip seoul_songpa_Bangi = seoul_2022_1_2_Sun();
                Trip jejuIsland_jeju_aewol = jeju_2023_1_1_Sun();
                Trip seoul_songpa_sincheon = seoul_2023_1_1_Sun();

                SearchConditions searchConditions = addressSearchConditions("서울특별시 송파구");

                // when
                List<Trip> trips = tripRepository.findAllByConditions(searchConditions, new Paging(null, 10));

                // then
                assertThat(trips).containsExactly(seoul_songpa_sincheon, seoul_songpa_Bangi);
            }

            @Test
            void 시도_형식으로_입력하면_해당하는_여행을_반환한다() {
                // given
                Trip seoul_songpa_Bangi = seoul_2022_1_2_Sun();
                Trip jejuIsland_jeju_aewol = jeju_2023_1_1_Sun();
                Trip seoul_songpa_sincheon = seoul_2023_1_1_Sun();

                SearchConditions searchConditions = addressSearchConditions("서울특별시");

                // when
                List<Trip> trips = tripRepository.findAllByConditions(searchConditions, new Paging(null, 10));

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

                SearchConditions searchConditions = new SearchConditions(
                        List.of(2023),
                        List.of(1),
                        List.of(),
                        List.of(),
                        List.of(),
                        "서울특별시"
                );

                // when
                List<Trip> trips = tripRepository.findAllByConditions(searchConditions, new Paging(null, 10));

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

                SearchConditions searchConditions = emptySearchConditions();

                // when
                List<Trip> trips = tripRepository.findAllByConditions(searchConditions, new Paging(null, 10));

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

            SearchConditions searchConditions = emptySearchConditions();

            // when
            List<Trip> trips = tripRepository.findAllByConditions(searchConditions, new Paging(null, 10));

            // then
            assertThat(trips).isEmpty();
        }

        private Trip jeju_2023_2_1_Wed() {
            Trip trip = new Trip(TripName.from(""), member);
            Point point = 위치정보(2023, 2, 1, 1, 1);
            trip.add(point);
            tripRepository.save(trip);
            postRepository.save(new Post("", point, "제주특별자치도 제주시 애월읍", "", member, trip.id()));
            return trip;
        }

        private Trip seoul_2023_1_1_Sun() {
            Trip trip = new Trip(TripName.from(""), member);
            Point point = 위치정보(2023, 1, 1, 10, 1);
            trip.add(point);
            tripRepository.save(trip);
            postRepository.save(new Post("", point, "서울특별시 송파구 신천동", "", member, trip.id()));
            return trip;
        }

        private Trip jeju_2023_1_1_Sun() {
            Trip trip = new Trip(TripName.from(""), member);
            Point point = 위치정보(2023, 1, 1, 1, 1);
            trip.add(point);
            tripRepository.save(trip);
            postRepository.save(new Post("", point, "제주특별자치도 제주시 애월읍", "", member, trip.id()));
            return trip;
        }

        private Trip seoul_2022_1_2_Sun() {
            Trip trip = new Trip(TripName.from(""), member);
            Point point = 위치정보(2022, 1, 2, 1, 1);
            trip.add(point);
            tripRepository.save(trip);
            postRepository.save(new Post("", point, "서울특별시 송파구 방이동", "", member, trip.id()));
            return trip;
        }

        private Trip yangyang_2021_3_2_Tue() {
            Trip trip = new Trip(TripName.from(""), member);
            Point point = 위치정보(2021, 3, 2, 1, 1);
            trip.add(point);
            tripRepository.save(trip);
            postRepository.save(new Post("", point, "강원도 양양군", "", member, trip.id()));
            return trip;
        }

        private Trip emptyPostTrip() {
            Trip trip = new Trip(TripName.from(""), member);
            Point point = 위치정보(2021, 3, 2, 1, 1);
            trip.add(point);
            tripRepository.save(trip);
            return trip;
        }
    }
}
