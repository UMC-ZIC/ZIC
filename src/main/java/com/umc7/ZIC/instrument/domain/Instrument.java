package com.umc7.ZIC.instrument.domain;

import com.umc7.ZIC.common.domain.BaseEntity;
import com.umc7.ZIC.common.domain.enums.InstrumentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Instrument extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InstrumentType name;

    private String image;
}
