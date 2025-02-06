package com.umc7.ZIC.user.service;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.handler.InstrumentHandler;
import com.umc7.ZIC.apiPayload.exception.handler.RegionHandler;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.common.domain.Instrument;
import com.umc7.ZIC.common.domain.Region;
import com.umc7.ZIC.common.domain.enums.InstrumentType;
import com.umc7.ZIC.common.repository.InstrumentRepository;
import com.umc7.ZIC.common.repository.RegionRepository;
import com.umc7.ZIC.common.util.InstrumentUtil;
import com.umc7.ZIC.common.util.RegionUtil;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomRequestDto;
import com.umc7.ZIC.practiceRoom.service.PracticeRoomService;
import com.umc7.ZIC.practiceRoom.service.PracticeRoomServiceImpl;
import com.umc7.ZIC.security.JwtTokenProvider;
import com.umc7.ZIC.user.converter.UserConverter;
import com.umc7.ZIC.user.converter.UserInstrumentConverter;
import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.domain.UserInstrument;
import com.umc7.ZIC.user.domain.enums.RoleType;
import com.umc7.ZIC.user.dto.UserRequestDto;
import com.umc7.ZIC.user.dto.UserResponseDto;
import com.umc7.ZIC.user.repository.UserInstrumentRepository;
import com.umc7.ZIC.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final InstrumentRepository instrumentRepository;
    private final RegionRepository regionRepository;
    private final UserInstrumentRepository userInstrumentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PracticeRoomService practiceRoomService;


    @Override
    public UserResponseDto.userDetailsDto updateUserDetails(Long userId, UserRequestDto.userDetailsDto userDetailsDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        checkPendingStatus(user);

        user.setRole(RoleType.USER);

        String userRequestRegion = userDetailsDto.region();

        Region userRegion = getRegion(userRequestRegion);
        user.setRegion(userRegion);

        //update
        User savedUser = userRepository.save(user);
        saveUserInstruments(savedUser, userDetailsDto.instrumentList());


        String jwtToken = jwtTokenProvider.createAccessToken(userId, RoleType.USER.name());

        return UserConverter.toRegisterUserDetails(user, jwtToken);
    }

    @Override
    public UserResponseDto.userDetailsDto updateOwnerDetails(Long userId, UserRequestDto.ownerDetailsDto ownerDetailsDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        checkPendingStatus(user);

        //ex [0]: 울산, [1]:남구 무거동~
        String[] userRequestRegion = ownerDetailsDto.region1().split(" ");
        Region userRegion = getRegion(userRequestRegion[0]);//ex 울산
        user.setRegion(userRegion);

        user.setRole(RoleType.OWNER);

        //남구 무거동 ~~~~
        StringBuilder address = new StringBuilder(); //메모리 효율을 위해 사용
        for (int i = 1; i < userRequestRegion.length; i++) {
            address.append(userRequestRegion[i]);
            if (i < userRequestRegion.length - 1) {
                address.append(" "); // 띄어쓰기
            }
        }
        //울산대학교 ~~
        address.append(" ").append(ownerDetailsDto.region2());

        user.setAddress(address.toString());
        user.setBusinessName(ownerDetailsDto.businessName());
        user.setBusinessNumber(ownerDetailsDto.businessNumber());

        //update
        User savedUser = userRepository.save(user);
        saveUserInstruments(savedUser, ownerDetailsDto.instrumentList());
        updateAuthorities(user);
        PracticeRoomRequestDto.CreateRequestDto createPracticeReqDto = new PracticeRoomRequestDto.CreateRequestDto
                (savedUser.getName(), savedUser.getRegion().getName().getKoreanName()+" "+savedUser.getAddress(), null, null,null);
        practiceRoomService.createPracticeRoom(createPracticeReqDto, savedUser.getId());
        String jwtToken = jwtTokenProvider.createAccessToken(userId, RoleType.OWNER.name());
        return UserConverter.toRegisterUserDetails(user, jwtToken);
    }


    @Override
    public List<UserResponseDto.OwnerEarning> getOwnerEarnings(Long userId, LocalDate targetMonth) {
        return userRepository.findOwnerEarningByUserIdAndMonth(userId, targetMonth);
    }

    @Override
    public List<UserResponseDto.OwnerMonthlyEarning> getOwnerMonthlyEarnings(Long userId) {
        return userRepository.findOwnerMonthlyEarningByUserId(userId);
    }

    Region getRegion(String regionName) {
        return regionRepository.findByName(RegionUtil.fromKoreanName(regionName))
                .orElseThrow(() -> new RegionHandler(ErrorStatus.REGION_NOT_FOUND));
    }


    private void saveUserInstruments(User user, List<String> instrumentKorNames) {
        for (String instrumentKorName : instrumentKorNames) {
            InstrumentType instrumentType = InstrumentUtil.fromKoreanName(instrumentKorName);
            Instrument instrument = instrumentRepository.findByName(instrumentType).orElseThrow(() -> new InstrumentHandler(ErrorStatus.INSTRUMENT_NOT_FOUND));
            ;
            if (instrument != null) {
                UserInstrument userInstrument = UserInstrumentConverter.toUserInstrument(user, instrument);
                userInstrumentRepository.save(userInstrument);
            }
        }
    }

    private void checkPendingStatus(User user) {
        //유저가 아직 가입 전인지 판단
        if (!user.getRole().equals(RoleType.PENDING)) {
            throw new UserHandler(ErrorStatus.USER_ROLE_NOT_PENDING);
        }
    }

    private void updateAuthorities(User user) {
        Collection<GrantedAuthority> updatedAuthorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        // 유저 권한 업데이트 로직 추가
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, updatedAuthorities)
        );
    }
}
