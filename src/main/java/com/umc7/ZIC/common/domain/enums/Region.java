package com.umc7.ZIC.common.domain.enums;

import com.umc7.ZIC.common.domain.BaseEntity;
import com.umc7.ZIC.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RegionType name;

    @OneToOne(mappedBy = "region")
    private User user;
}
