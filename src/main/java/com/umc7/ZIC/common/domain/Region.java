package com.umc7.ZIC.common.domain;

import com.umc7.ZIC.common.domain.enums.RegionType;
import com.umc7.ZIC.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RegionType name;

    @OneToMany(mappedBy = "region")
    private List<User> user;
}
