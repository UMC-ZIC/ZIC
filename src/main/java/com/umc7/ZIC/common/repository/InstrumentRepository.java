package com.umc7.ZIC.common.repository;

import com.umc7.ZIC.common.domain.Instrument;
import com.umc7.ZIC.common.domain.enums.InstrumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    Optional<Instrument> findByName(InstrumentType instrumentType);
}
