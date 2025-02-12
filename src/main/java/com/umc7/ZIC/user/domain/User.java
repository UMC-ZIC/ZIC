package com.umc7.ZIC.user.domain;

import com.umc7.ZIC.common.domain.BaseEntity;
import com.umc7.ZIC.common.domain.Region;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.user.domain.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private Long kakaoId;

    @Column(length = 40, nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 30, nullable = true)
    private String businessNumber;

    @Column(length = 20, nullable = true)
    private String businessName;

    @Column(length = 50, nullable = true)
    private String address;

    @Column(length = 300, nullable = true)
    private String profilePic;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'PENDING'", length = 15)
    private RoleType role;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserInstrument> userInstrumentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Reservation> reservationList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PracticeRoom> practiceRoomList = new ArrayList<>();

}
