package com.umc7.ZIC.reservation.repository;

import com.umc7.ZIC.reservation.domain.ReservationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationDetailRepository extends JpaRepository<ReservationDetail, Long> {
}
