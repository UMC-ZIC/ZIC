package com.umc7.ZIC.reservation.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc7.ZIC.common.domain.BaseEntity;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import com.umc7.ZIC.reservation.domain.enums.Status;
import com.umc7.ZIC.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    // 클라이언트에서 생성한 무작위 문자열
    @Column(nullable = false)
    private String reservationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "practiceRoomDetail_id", nullable = false)
    private PracticeRoomDetail practiceRoomDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 결제 상태
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'PENDING'", length = 15)
    private Status status = Status.PENDING;

    // 예약 날짜
    @Column(nullable = false)
    private LocalDate date;

    // 예약 시작 시간
    @JsonFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime startTime;

    // 예약 끝나는 시간
    @JsonFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime endTime;

    // 양방향 연결
    @OneToOne(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ReservationDetail reservationDetail;

    // Status 변경
    public Reservation toggleStatus(Status status) {
        this.status = status;
        return this;
    }
}
