package dev.tripdraw.domain.post;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import dev.tripdraw.domain.common.BaseEntity;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.trip.Point;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private Long tripId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String address;
    
    @Column(columnDefinition = "TEXT")
    private String writing;

    @OneToOne
    @JoinColumn(name = "point_id")
    private Point point;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String imageUrl;

    protected Post() {
    }

    public Post(String title, Point point, String address, String writing, Member member, Long tripId) {
        this(null, title, point, address, writing, member, tripId);
    }

    public Post(Long id, String title, Point point, String address, String writing, Member member, Long tripId) {
        this.id = id;
        this.title = title;
        this.point = point;
        this.address = address;
        this.writing = writing;
        this.member = member;
        this.tripId = tripId;
    }

    public LocalDateTime pointRecordedAt() {
        return point.recordedAt();
    }

    public Long id() {
        return id;
    }

    public String title() {
        return title;
    }

    public Point point() {
        return point;
    }

    public String address() {
        return address;
    }

    public String writing() {
        return writing;
    }

    public Member member() {
        return member;
    }

    public Long tripId() {
        return tripId;
    }

    public String imageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
