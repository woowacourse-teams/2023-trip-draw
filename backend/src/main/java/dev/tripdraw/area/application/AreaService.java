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
    private final AreaInitializer areaInitializer;

    @Transactional(readOnly = true)
    public AreaResponse read(AreaReqeust areaReqeust) {
        String sido = areaReqeust.sido();
        String sigungu = areaReqeust.sigungu();

        if (sido.isBlank()) {
            List<Area> areas = areaRepository.findAll();
            List<String> sidos = areas.stream()
                    .map(Area::sido)
                    .distinct()
                    .sorted()
                    .toList();

            return AreaResponse.from(sidos);
        }

        if (sigungu.isBlank()) {
            List<Area> areasOfSido = areaRepository.findBySido(sido);
            List<String> sigungus = areasOfSido.stream()
                    .map(Area::sigungu)
                    .distinct()
                    .sorted()
                    .toList();

            return AreaResponse.from(sigungus);
        }

        List<Area> areasOfSigunguAndSido = areaRepository.findBySidoAndSigungu(sido, sigungu);
        List<String> umds = areasOfSigunguAndSido.stream()
                .map(Area::umd)
                .distinct()
                .sorted()
                .toList();

        return AreaResponse.from(umds);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 1 * ?")
    public AreaResponse create() {
        long count = areaRepository.count();
        if (count != 0) {
            return AreaResponse.from(List.of());
        }

        areaRepository.deleteAllInBatch();

        List<Area> areas = areaInitializer.init();

        List<Area> allAreas = areaRepository.saveAll(areas);
        List<String> allAddresses = allAreas.stream()
                .map(Area::toFullAddress)
                .toList();

        return AreaResponse.from(allAddresses);
    }
}
