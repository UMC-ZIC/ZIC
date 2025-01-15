package com.umc7.ZIC.user.repository;

import com.umc7.ZIC.user.domain.UserInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInstrumentRepository extends JpaRepository<UserInstrument, Long> {
}
