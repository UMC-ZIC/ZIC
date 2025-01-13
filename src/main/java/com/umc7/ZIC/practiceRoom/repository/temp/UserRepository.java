package com.umc7.ZIC.practiceRoom.repository.temp;

import com.umc7.ZIC.common.domain.Region;
import com.umc7.ZIC.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
