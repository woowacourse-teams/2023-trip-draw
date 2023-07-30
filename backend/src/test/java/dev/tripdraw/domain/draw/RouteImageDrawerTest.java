package dev.tripdraw.domain.draw;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RouteImageDrawerTest {

    @Test
    void 이미지_크기를_받아_RouteImageDrawer를_생성한다() {
        // given
        int imageSize = 800;
        Color transparent = new Color(0, 0, 0, 0);
        BasicStroke roundStroke = new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        // when
        RouteImageDrawer routeImageDrawer = RouteImageDrawer.from(imageSize);

        // then
        assertSoftly(softly -> {
            Graphics2D graphics2D = routeImageDrawer.graphics2D();
            softly.assertThat(graphics2D.getStroke()).isEqualTo(roundStroke);
            softly.assertThat(graphics2D.getBackground()).isEqualTo(transparent);
            softly.assertThat(graphics2D.getPaint()).isEqualTo(Color.WHITE);
        });
    }

    @Test
    void positions를_입력받아_이미지에_경로를_그린다() {
        // given
        Graphics2D graphics2D = Mockito.mock(Graphics2D.class);
        RouteImageDrawer routeImageDrawer = new RouteImageDrawer(null, graphics2D);
        Positions positions = new Positions(List.of(
                new Position(254, 100), new Position(302, 150), new Position(389, 138), new Position(482, 164),
                new Position(521, 276), new Position(546, 336), new Position(402, 426), new Position(317, 700)
        ));

        // when
        routeImageDrawer.drawLine(positions);

        // then
        Mockito.verify(graphics2D, times(positions.size() - 1)).draw(any(Line2D.Double.class));
    }

    @Test
    void positions를_입력받아_이미지에_위치_점을_그린다() {
        // given
        Graphics2D graphics2D = Mockito.mock(Graphics2D.class);
        RouteImageDrawer routeImageDrawer = new RouteImageDrawer(null, graphics2D);
        Positions positions = new Positions(List.of(
                new Position(254, 100), new Position(302, 150), new Position(389, 138)
        ));

        // when
        routeImageDrawer.drawPoint(positions);

        // then
        Mockito.verify(graphics2D, times(positions.size())).draw(any(Line2D.Double.class));
    }

    @Test
    void 자원할당을_해제한다() {
        // given
        Graphics2D graphics2D = Mockito.mock(Graphics2D.class);
        RouteImageDrawer routeImageDrawer = new RouteImageDrawer(null, graphics2D);
        
        // when
        routeImageDrawer.dispose();

        // then
        Mockito.verify(graphics2D, times(1)).dispose();
    }
}
