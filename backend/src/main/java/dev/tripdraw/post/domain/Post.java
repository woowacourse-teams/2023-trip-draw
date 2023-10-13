package dev.tripdraw.post.domain;

import static dev.tripdraw.post.exception.PostExceptionType.NOT_AUTHORIZED_TO_POST;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import dev.tripdraw.common.entity.BaseEntity;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.post.exception.PostException;
import dev.tripdraw.trip.domain.Point;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@NoArgsConstructor(access = PROTECTED)
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

    private String postImageUrl;

    private String routeImageUrl;

    public Post(String title, Point point, String address, String writing, Member member, Long tripId) {
        this(null, title, point, address, writing, member, tripId);
    }

    public Post(Long id, String title, Point point, String address, String writing, Member member, Long tripId) {
        point.registerPost();
        this.id = id;
        this.title = title;
        this.point = point;
        this.address = address;
        this.writing = writing;
        this.member = member;
        this.tripId = tripId;
    }

    public void validateAuthorization(Long memberId) {
        if (!this.member.id().equals(memberId)) {
            throw new PostException(NOT_AUTHORIZED_TO_POST);
        }
    }

    public void changePostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public void removePostImageUrl() {
        this.postImageUrl = null;
    }

    public void changeRouteImageUrl(String routeImageUrl) {
        this.routeImageUrl = routeImageUrl;
    }

    public LocalDateTime pointRecordedAt() {
        return point.recordedAt();
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeWriting(String writing) {
        this.writing = writing;
    }
}
