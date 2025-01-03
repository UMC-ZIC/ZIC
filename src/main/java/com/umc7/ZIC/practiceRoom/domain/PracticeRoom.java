package com.umc7.ZIC.practiceRoom.domain;

import com.umc7.ZIC.common.domain.BaseEntity;
import com.umc7.ZIC.common.domain.Region;
import com.umc7.ZIC.common.domain.enums.RegionType;
import com.umc7.ZIC.owner.domain.Owner;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PracticeRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Column(length = 40, nullable = false)
    private String name;

    @Column(length = 40, nullable = false)
    private String address;

    @Min(value = -90)
    @Max(value = 90)
    private Double latitude;

    @Min(value = -180)
    @Max(value = 180)
    private Double longitude;


}
