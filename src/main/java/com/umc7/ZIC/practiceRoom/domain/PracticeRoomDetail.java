package com.umc7.ZIC.practiceRoom.domain;

import com.umc7.ZIC.common.domain.BaseEntity;
import com.umc7.ZIC.practiceRoom.domain.enums.RoomStatus;
import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PracticeRoomDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="practice_room_id")
    private PracticeRoom practiceRoom;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 1000)
    private String image;

    @Column(length = 20, nullable = false)
    private Integer fee;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "practiceRoomDetail", cascade = CascadeType.ALL)
    private List<Reservation> reservationList = new ArrayList<>();

    public void update(String name, String image, Integer fee, RoomStatus status) {
        this.name = name;
        this.image = image;
        this.fee = fee;
        this.status = status;
    }
    public List<Reservation> getSuccessReservationsByDate(LocalDate date) {
        List<Reservation> successReservations = new ArrayList<>();
        for (Reservation reservation : reservationList) {
            if (reservation.getDate().isEqual(date) && reservation.getStatus() == ReservationStatus.SUCCESS) {
                successReservations.add(reservation);
            }
        }
        return successReservations;
    }

    public void updateStatus(RoomStatus status) {
        this.status = status;
    }
}
