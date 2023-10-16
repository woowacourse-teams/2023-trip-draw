package dev.tripdraw.area.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import dev.tripdraw.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Area extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "area_id")
    private Long id;

    @Column(name = "sido")
    private String sido;

    @Column(name = "sigungu")
    private String sigungu;

    @Column(name = "umd") // 읍,면,동
    private String umd;

    public Area(Long id, String sido, String sigungu, String umd) {
        this.id = id;
        this.sido = sido;
        this.sigungu = sigungu;
        this.umd = umd;
    }

    public Area(String sido, String sigungu, String umd) {
        this(null, sido, sigungu, umd);
    }
}
