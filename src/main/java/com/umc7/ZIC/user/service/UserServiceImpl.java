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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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


    @Override
    public UserResponseDto.userDetailsDto updateUserDetails(Long userId, UserRequestDto.userDetailsDto userDetailsDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        String userRequestRegion = userDetailsDto.region();

        Region userRegion = getRegion(userRequestRegion);
        user.setRegion(userRegion);
        user.setRole(RoleType.USER);
        //update
        User savedUser = userRepository.save(user);
        saveUserInstruments(savedUser, userDetailsDto.instrumentList());

        String jwtToken = jwtTokenProvider.createAccessToken(userId, RoleType.USER.name());

        return UserConverter.toRegisterUserDetails(user, jwtToken);
    }

    @Override
    public UserResponseDto.userDetailsDto updateOwnerDetails(Long userId, UserRequestDto.ownerDetailsDto ownerDetailsDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //todo 전처리 해야함 ex) 울산시 남구 무거동 1~~~ -> [0]울산
        String userRequestRegion = ownerDetailsDto.region1();
        Region userRegion = getRegion(userRequestRegion);
        user.setRegion(userRegion);
        user.setRole(RoleType.OWNER);
        //todo[1] 남구 무거동 111~ 울산대학교 ~~호 ~동
        String address = ownerDetailsDto.region2();
        user.setAddress(address);
        user.setBusinessName(ownerDetailsDto.businessName());
        user.setBusinessNumber(ownerDetailsDto.businessNumber());

        //update
        User savedUser = userRepository.save(user);
        saveUserInstruments(savedUser, ownerDetailsDto.instrumentList());

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
            Instrument instrument = instrumentRepository.findByName(instrumentType).orElseThrow(() -> new InstrumentHandler(ErrorStatus.INSTRUMENT_NOT_FOUND));;
            if (instrument != null) {
                UserInstrument userInstrument = UserInstrumentConverter.toUserInstrument(user, instrument);
                userInstrumentRepository.save(userInstrument);
            }
        }
    }

}
