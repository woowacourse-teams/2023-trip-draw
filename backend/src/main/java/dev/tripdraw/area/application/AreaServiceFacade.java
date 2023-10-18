package dev.tripdraw.area.application;

import dev.tripdraw.area.domain.Area;
import dev.tripdraw.area.dto.AreaReqeust;
import dev.tripdraw.area.dto.AreaResponse;
import dev.tripdraw.auth.application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AreaServiceFacade {

    private final AreaService areaService;
    private final OpenAPIAreaDownloader openAPIAreaDownloader;

    public AreaResponse read(AreaReqeust areaReqeust) {
        return areaService.read(areaReqeust);
    }

    @Scheduled(cron = "0 0 0 1 * ?") // 매달 1일에 업데이트 한다는 CRON 표현식
    public void create() {
        List<Area> areas = openAPIAreaDownloader.download();
        areaService.create(areas);
    }
}
