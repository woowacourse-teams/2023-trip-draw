package dev.tripdraw.area.application;

import dev.tripdraw.area.domain.Area;
import dev.tripdraw.area.domain.AreaRepository;
import dev.tripdraw.area.dto.AreaReqeust;
import dev.tripdraw.area.dto.AreaResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AreaService {

    private final AreaRepository areaRepository;
    private final OpenAPIAreaService openAPIAreaService;

    @Transactional(readOnly = true)
    public AreaResponse read(AreaReqeust areaReqeust) {
        String sido = areaReqeust.sido();
        String sigungu = areaReqeust.sigungu();

        if (sido.isBlank()) {
            return readAllSidos();
        }

        if (sigungu.isBlank()) {
            return readAllSigungusOf(sido);
        }

        return readAllUmdsOf(sido, sigungu);
    }

    private AreaResponse readAllSidos() {
        List<Area> areas = areaRepository.findAll();
        List<String> sidos = areas.stream()
                .map(Area::sido)
                .distinct()
                .sorted()
                .toList();

        return AreaResponse.from(sidos);
    }

    private AreaResponse readAllSigungusOf(String sido) {
        List<Area> areasOfSido = areaRepository.findBySido(sido);
        List<String> sigungus = areasOfSido.stream()
                .map(Area::sigungu)
                .distinct()
                .sorted()
                .toList();

        return AreaResponse.from(sigungus);
    }

    private AreaResponse readAllUmdsOf(String sido, String sigungu) {
        List<Area> areasOfSigunguAndSido = areaRepository.findBySidoAndSigungu(sido, sigungu);
        List<String> umds = areasOfSigunguAndSido.stream()
                .map(Area::umd)
                .distinct()
                .sorted()
                .toList();

        return AreaResponse.from(umds);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 1 * ?") // 매달 1일에 업데이트 한다는 CRON 표현식
    public void create() {
        long count = areaRepository.count();
        if (count != 0) {
            AreaResponse.from(List.of());
            return;
        }

        areaRepository.deleteAllInBatch();
        List<Area> areas = openAPIAreaService.download();

        List<Area> allAreas = areaRepository.saveAll(areas);
        List<String> allAddresses = allAreas.stream()
                .map(Area::toFullAddress)
                .toList();

        AreaResponse.from(allAddresses);
    }
}
