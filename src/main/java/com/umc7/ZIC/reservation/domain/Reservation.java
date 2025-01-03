package com.umc7.ZIC.reservation.domain;

import com.umc7.ZIC.common.domain.BaseEntity;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "practiceRoomDetail_id", nullable = false)
    private PracticeRoomDetail practiceRoomDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 예약 날짜
    @Column(nullable = false)
    private LocalDate date;

    // 예약 시작 시간
    @Column(nullable = false)
    private LocalTime startTime;

    // 예약 끝나는 시간
    @Column(nullable = false)
    private LocalTime endTime;
}
