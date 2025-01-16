package com.umc7.ZIC.reservation.domain;

import com.umc7.ZIC.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReservationDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    // 예약 결제 고유 번호
    @Column(nullable = false)
    private String tid;

    // 예약 금액
    @Column(nullable = false)
    private int amount;

    // 비과세 금액
    @Column(nullable = false)
    private int tax_free_amount;

    // 부과세 금액
    @Column(nullable = false)
    private int vat_amount;
}
