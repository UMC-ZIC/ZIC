package com.umc7.ZIC.user.service;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.handler.InstrumentHandler;
import com.umc7.ZIC.apiPayload.exception.handler.PracticeRoomHandler;
import com.umc7.ZIC.apiPayload.exception.handler.RegionHandler;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.common.domain.Instrument;
import com.umc7.ZIC.common.domain.Region;
import com.umc7.ZIC.common.domain.enums.InstrumentType;
import com.umc7.ZIC.common.repository.InstrumentRepository;
import com.umc7.ZIC.common.repository.RegionRepository;
import com.umc7.ZIC.common.util.InstrumentUtil;
import com.umc7.ZIC.common.util.RegionUtil;
import com.umc7.ZIC.practiceRoom.converter.PracticeRoomInstrumentConverter;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomInstrument;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomRequestDto;
import com.umc7.ZIC.practiceRoom.repository.PracticeRoomInstrumentRepository;
import com.umc7.ZIC.practiceRoom.repository.PracticeRoomRepository;
import com.umc7.ZIC.practiceRoom.service.PracticeRoomService;
import com.umc7.ZIC.reservation.repository.ReservationRepository;
import com.umc7.ZIC.security.JwtTokenProvider;
import com.umc7.ZIC.user.converter.UserConverter;
import com.umc7.ZIC.user.converter.UserInstrumentConverter;
import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.domain.UserInstrument;
import com.umc7.ZIC.user.domain.enums.RoleType;
import com.umc7.ZIC.user.dto.KakaoUserInfoResponseDto;
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
import java.util.*;

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
    private final PracticeRoomInstrumentRepository practiceRoomInstrumentRepository;
    private final PracticeRoomRepository practiceRoomRepository;
    private final ReservationRepository reservationRepository;
    private final String defaultImg= "https://firebasestorage.googleapis.com/v0/b/muzic-6fce5.firebasestorage.app/o/zic%2Fdefault%2FZIC_default.jpg?alt=media&token=564dd5fe-c924-448e-98ff-d1b2eec7e10b";


    @Override
    public UserResponseDto.User.UserDetailsDto updateUserDetails(Long userId, UserRequestDto.userDetailsDto userDetailsDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        checkPendingStatus(user);

        user.setRole(RoleType.USER);

        String userRequestRegion = userDetailsDto.region();

        Region userRegion = getRegion(userRequestRegion);
        user.setRegion(userRegion);

        //update
        User savedUser = userRepository.save(user);
        Optional<List<String>> instrumentList = Optional.ofNullable(userDetailsDto.instrumentList());
        saveUserInstruments(savedUser, instrumentList);


        String jwtToken = jwtTokenProvider.createAccessToken(userId, savedUser.getRole().toString(), savedUser.getName());

        return UserConverter.toRegisterUserDetails(user, jwtToken);
    }

    @Override
    public UserResponseDto.User.OwnerDetailsDto updateOwnerDetails(Long userId, UserRequestDto.ownerDetailsDto ownerDetailsDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Optional<List<String>> instrumentList = Optional.ofNullable(ownerDetailsDto.instrumentList());

        //연습실 등록을 위한 악기가 있는지
        if(!instrumentList.isPresent() || instrumentList.get().isEmpty()){
            throw new InstrumentHandler(ErrorStatus.INSTRUMENT_OWNER_NOT_FOUND);
        }
        
        //악기 한글 이름 잘못 되어 있는지 save 전에 선 검색
        List<Instrument> instruments = new ArrayList<>();
        for (String roomInstrument : instrumentList.get()) {
            instruments.add(getInstrument(roomInstrument));
        }

        checkPendingStatus(user);

        //ex [0]: 울산, [1]:남구 무거동~
        String[] userRequestRegion = ownerDetailsDto.region1().trim().split(" ");
        Region userRegion = getRegion(userRequestRegion[0]);//ex 울산
        user.setRegion(userRegion);

        user.setRole(RoleType.OWNER);
        //남구 무거동 ~~~~
        StringBuilder address = new StringBuilder(); //메모리 효율을 위해 사용
        for (int i = 1; i < userRequestRegion.length; i++) {
            address.append(userRequestRegion[i].trim());
            if (i < userRequestRegion.length - 1) {
                address.append(" "); // 띄어쓰기
            }
        }
        //울산대학교 ~~
        //region이 '울산'으로만 끝나면 띄어쓰기 안 주기
        if(userRequestRegion.length==1){
            address.append(ownerDetailsDto.region2());
        } else {
            address.append(" ").append(ownerDetailsDto.region2());
        }

        user.setAddress(address.toString());
        user.setBusinessName(ownerDetailsDto.businessName());
        user.setBusinessNumber(ownerDetailsDto.businessNumber());

        //update

        User savedUser = userRepository.save(user);
        saveUserInstruments(savedUser, instrumentList);
        updateAuthorities(user);

        PracticeRoomRequestDto.CreateRequestDto createPracticeReqDto = new PracticeRoomRequestDto.CreateRequestDto
                (savedUser.getBusinessName(), savedUser.getRegion().getName().getKoreanName()+" "+savedUser.getAddress(), defaultImg, null,null);

        //연습실 등록
        PracticeRoom savedPracticeRoom;
        try {
            PracticeRoom practiceRoom = createPracticeReqDto.toEntity(user, user.getRegion());

            savedPracticeRoom = practiceRoomRepository.save(practiceRoom);

        }catch (Exception e) {
            log.error(e.getMessage());
            throw new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_OWNER_ROLE);
        }

        if (instrumentList.isPresent() && !instrumentList.get().isEmpty()){
            for (Instrument roomInstrument : instruments) {
                PracticeRoomInstrument practiceRoomInstrument = PracticeRoomInstrumentConverter.toPracticeRoomInstrument(savedPracticeRoom, roomInstrument);
                practiceRoomInstrumentRepository.save(practiceRoomInstrument);
            }
        }

        String jwtToken = jwtTokenProvider.createAccessToken(userId, savedUser.getRole().toString(), savedUser.getName());
        return UserConverter.toRegisterOwnerDetails(user, jwtToken, savedPracticeRoom.getId());
    }

    @Override
    public UserResponseDto.User.UserDetailsDto getUser(Long UserId, String jwtToken) {
        User user = userRepository.findById(UserId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        UserResponseDto.User.UserDetailsDto userDetailsDto= UserConverter.toResponseUser(user, jwtToken);

        return userDetailsDto;
    }

    @Override
    public UserResponseDto.User.UserDetailsDto kaKaoGetUser(KakaoUserInfoResponseDto userInfo) {
        User user = userRepository.findByKakaoId(userInfo.id()).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        String jwtAccessToken = jwtTokenProvider.createAccessToken(user.getId(),user.getRole().name(), user.getName());

        return UserConverter.toResponseUser(user, jwtAccessToken);
    }


    @Override
    public List<UserResponseDto.OwnerEarning> getOwnerEarnings(Long userId, LocalDate targetMonth) {
        return userRepository.findOwnerEarningByUserIdAndMonth(userId, targetMonth);
    }

    @Override
    public List<UserResponseDto.OwnerMonthlyEarning> getOwnerMonthlyEarnings(Long userId) {
        return userRepository.findOwnerMonthlyEarningByUserId(userId);
    }

    @Override
    public UserResponseDto.UserMyPageDto getUserMypage(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        List<UserResponseDto.UserMyPageDto.UserThisMonthPractice.UserThisMonthPracticeDetail> userThisMonthPracticeDetailList = reservationRepository.findUserThisMonthPracticeDetailsByUserId(userId);
        List<UserResponseDto.UserMyPageDto.FrequentPracticeRooms.FrequentPracticeRoomDetail> findTop3PracticeRoomsByUserId = reservationRepository.findTop3PracticeRoomsByUserId(userId);
        UserResponseDto.UserMyPageDto userMyPageDto = UserConverter.UserMyPageDto(findTop3PracticeRoomsByUserId, userThisMonthPracticeDetailList, user);


        return userMyPageDto;
    }


    Region getRegion(String regionName) {
        return regionRepository.findByName(RegionUtil.fromKoreanName(regionName))
                .orElseThrow(() -> new RegionHandler(ErrorStatus.REGION_NOT_FOUND));
    }

    Instrument getInstrument(String instrumentName) {
        return instrumentRepository.findByName(InstrumentUtil.fromKoreanName(instrumentName))
                .orElseThrow(() -> new InstrumentHandler(ErrorStatus.INSTRUMENT_NOT_FOUND));
    }


    private void saveUserInstruments(User user, Optional<List<String>> instrumentKorNames) {
        if (!instrumentKorNames.isPresent() || instrumentKorNames.get().isEmpty()) {
            return;
        }
        for (String instrumentKorName : instrumentKorNames.get()) {
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
