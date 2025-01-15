package com.umc7.ZIC.user.repository;

import com.umc7.ZIC.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByKakaoId(Long kakaoId);
    Optional<User> findByEmail(String email);
}
