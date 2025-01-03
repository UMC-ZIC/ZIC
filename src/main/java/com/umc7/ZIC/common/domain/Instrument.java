package com.umc7.ZIC.common.domain;

import com.umc7.ZIC.common.domain.enums.InstrumentType;
import com.umc7.ZIC.user.domain.UserInstrument;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Instrument extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private InstrumentType name;

    @OneToMany(mappedBy = "instrument", cascade = CascadeType.ALL)
    private List<UserInstrument> userInstrumentList = new ArrayList<>();
}
