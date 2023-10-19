package dev.tripdraw.area.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Long> {

    List<Area> findBySido(String sido);

    List<Area> findBySidoAndSigungu(String sido, String sigungu);
}
