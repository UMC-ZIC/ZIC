![image](https://github.com/user-attachments/assets/1620d892-6caa-4bff-8c98-7e019f6f6996)


## 💡 서비스 소개  
'**ZIC**' 은 음악 연습실을 쉽고 빠르게 예약하고 효율적으로 관리할 수 있게 돕는 서비스입니다.
> music의 sic을 어감을 더 찰지게 하기 위해 첫 글자를 Z로 바꾸면서 탄생했습니다.
<details> 
<summary>ZIC 기획안 </summary>
 
![image](https://github.com/user-attachments/assets/284900cb-c480-4c6d-9f20-a27ed26f7bae)




</details>



## 기술 스택

### Backend <br/>

![Java 17](https://img.shields.io/badge/Java%2017-007396?style=flat-square&logo=java&logoColor=white) 
![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203.4.1-6DB33F?style=flat-square&logo=Spring%20Boot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA%203.4.1-6DB33F?style=flat-square&logo=&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=Spring%20Security&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=flat-square&logo=JUnit5&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=JSON-Web-Tokens&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-black?style=flat-square&badgeColor=010101)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=flat-square&logo=Thymeleaf&logoColor=white)

### DB / Infra
![MySQL](https://img.shields.io/badge/MySQL%208-4479A1?style=flat-square&logo=MySQL&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=flat-square&logo=amazon-aws&logoColor=white)


### 문서/협업툴
![Notion](https://img.shields.io/badge/Notion-000000?style=flat-square&logo=notion&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-4A154B?style=flat-square&logo=intellijidea&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=Swagger&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-5865F2?style=flat-square&logo=discord&logoColor=white)

## 구조

전체 시스템 구조
<p align="center">

![image](https://github.com/user-attachments/assets/239c8c11-80e9-4000-b87c-af45f0f06346)

</p>






<details>
  <summary> 패키지 구조 </summary>
  
```
📦 
├─ .gitattributes
├─ .github
│  ├─ ISSUE_TEMPLATE
│  │  └─ 이슈-생성-템플릿.md
│  ├─ PULL_REQUEST_TEMPLATE.md
│  └─ workflows
│     └─ deploy.yml
├─ .gitignore
├─ build.gradle
├─ gradle
│  └─ wrapper
│     ├─ gradle-wrapper.jar
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
├─ settings.gradle
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ umc7
   │  │        └─ ZIC
   │  │           ├─ ZicApplication.java
   │  │           ├─ apiPayload
   │  │           │  ├─ code
   │  │           │  │  ├─ BaseCode.java
   │  │           │  │  ├─ BaseErrorCode.java
   │  │           │  │  ├─ ErrorReasonDTO.java
   │  │           │  │  ├─ ReasonDTO.java
   │  │           │  │  └─ status
   │  │           │  │     ├─ ErrorStatus.java
   │  │           │  │     └─ SuccessStatus.java
   │  │           │  └─ exception
   │  │           │     ├─ ApiResponse.java
   │  │           │     └─ handler
   │  │           │        ├─ ExceptionAdvice.java
   │  │           │        ├─ GeneralException.java
   │  │           │        ├─ InstrumentHandler.java
   │  │           │        ├─ PracticeRoomDetailHandler.java
   │  │           │        ├─ PracticeRoomHandler.java
   │  │           │        ├─ RegionHandler.java
   │  │           │        ├─ ReservationHandler.java
   │  │           │        └─ UserHandler.java
   │  │           ├─ common
   │  │           │  ├─ controller
   │  │           │  │  ├─ ReservationPageController.java
   │  │           │  │  └─ TestLoginController.java
   │  │           │  ├─ domain
   │  │           │  │  ├─ BaseEntity.java
   │  │           │  │  ├─ Instrument.java
   │  │           │  │  ├─ Region.java
   │  │           │  │  └─ enums
   │  │           │  │     ├─ InstrumentType.java
   │  │           │  │     └─ RegionType.java
   │  │           │  ├─ repository
   │  │           │  │  ├─ InstrumentRepository.java
   │  │           │  │  └─ RegionRepository.java
   │  │           │  ├─ util
   │  │           │  │  ├─ InstrumentUtil.java
   │  │           │  │  ├─ RedisUtil.java
   │  │           │  │  └─ RegionUtil.java
   │  │           │  └─ validation
   │  │           │     ├─ annotation
   │  │           │     │  └─ CheckPage.java
   │  │           │     └─ validator
   │  │           │        └─ PageCheckValidator.java
   │  │           ├─ config
   │  │           │  ├─ QueryDSLConfig.java
   │  │           │  ├─ RedisConfig.java
   │  │           │  └─ SwaggerConfig.java
   │  │           ├─ practiceRoom
   │  │           │  ├─ controller
   │  │           │  │  ├─ PracticeRoomController.java
   │  │           │  │  ├─ PracticeRoomDetailController.java
   │  │           │  │  └─ PracticeRoomLikeController.java
   │  │           │  ├─ converter
   │  │           │  │  └─ PracticeRoomInstrumentConverter.java
   │  │           │  ├─ domain
   │  │           │  │  ├─ PracticeRoom.java
   │  │           │  │  ├─ PracticeRoomDetail.java
   │  │           │  │  ├─ PracticeRoomInstrument.java
   │  │           │  │  ├─ PracticeRoomLike.java
   │  │           │  │  └─ enums
   │  │           │  │     └─ RoomStatus.java
   │  │           │  ├─ dto
   │  │           │  │  ├─ AvailableTimeSlot.java
   │  │           │  │  ├─ PageRequestDto.java
   │  │           │  │  ├─ PageResponseDto.java
   │  │           │  │  ├─ PracticeRoomDetailRequestDto.java
   │  │           │  │  ├─ PracticeRoomDetailResponseDto.java
   │  │           │  │  ├─ PracticeRoomLikeRequestDto.java
   │  │           │  │  ├─ PracticeRoomLikeResponseDto.java
   │  │           │  │  ├─ PracticeRoomRequestDto.java
   │  │           │  │  └─ PracticeRoomResponseDto.java
   │  │           │  ├─ repository
   │  │           │  │  ├─ PracticeRoomDetailRepository.java
   │  │           │  │  ├─ PracticeRoomInstrumentRepository.java
   │  │           │  │  ├─ PracticeRoomLikeRepository.java
   │  │           │  │  └─ PracticeRoomRepository.java
   │  │           │  ├─ service
   │  │           │  │  ├─ PracticeRoomDetailService.java
   │  │           │  │  ├─ PracticeRoomDetailServiceImpl.java
   │  │           │  │  ├─ PracticeRoomLikeService.java
   │  │           │  │  ├─ PracticeRoomLikeServiceImpl.java
   │  │           │  │  ├─ PracticeRoomService.java
   │  │           │  │  └─ PracticeRoomServiceImpl.java
   │  │           │  └─ validation
   │  │           │     ├─ annotation
   │  │           │     │  ├─ CheckPracticeRoomDetailStatus.java
   │  │           │     │  └─ ExistPracticeRoomDetail.java
   │  │           │     └─ validator
   │  │           │        ├─ PracticeRoomDetailExistValidator.java
   │  │           │        └─ PracticeRoomDetailStatusCheckValidator.java
   │  │           ├─ reservation
   │  │           │  ├─ ReservationScheduler.java
   │  │           │  ├─ controller
   │  │           │  │  └─ ReservationRestController.java
   │  │           │  ├─ converter
   │  │           │  │  ├─ KakaoPayConverter.java
   │  │           │  │  └─ ReservationConverter.java
   │  │           │  ├─ domain
   │  │           │  │  ├─ Reservation.java
   │  │           │  │  ├─ ReservationDetail.java
   │  │           │  │  └─ enums
   │  │           │  │     └─ ReservationStatus.java
   │  │           │  ├─ dto
   │  │           │  │  ├─ PaymentRequestDTO.java
   │  │           │  │  ├─ PaymentResponseDTO.java
   │  │           │  │  ├─ ReservationRequestDTO.java
   │  │           │  │  └─ ReservationResponseDTO.java
   │  │           │  ├─ repository
   │  │           │  │  ├─ ReservationDetailRepository.java
   │  │           │  │  ├─ ReservationRepository.java
   │  │           │  │  ├─ ReservationRepositoryCustom.java
   │  │           │  │  └─ ReservationRepositoryImpl.java
   │  │           │  ├─ service
   │  │           │  │  ├─ KakaoPayService.java
   │  │           │  │  ├─ KakaoPayServiceImpl.java
   │  │           │  │  ├─ ReservationCommandService.java
   │  │           │  │  ├─ ReservationCommandServiceImpl.java
   │  │           │  │  ├─ ReservationQueryService.java
   │  │           │  │  └─ ReservationQueryServiceImpl.java
   │  │           │  └─ validation
   │  │           │     ├─ annotation
   │  │           │     │  ├─ CheckReservationData.java
   │  │           │     │  ├─ CheckReservationStatus.java
   │  │           │     │  ├─ CheckReservationTime.java
   │  │           │     │  ├─ CheckReservationTimeOverlap.java
   │  │           │     │  └─ ExistReservation.java
   │  │           │     ├─ validationSequence
   │  │           │     │  ├─ ValidationOrder.java
   │  │           │     │  └─ ValidationStep.java
   │  │           │     └─ validator
   │  │           │        ├─ ReservationDataCheckValidator.java
   │  │           │        ├─ ReservationExistValidator.java
   │  │           │        ├─ ReservationStatusCheckValidator.java
   │  │           │        ├─ ReservationTimeCheckValidator.java
   │  │           │        └─ ReservationTimeOverlapCheckValidator.java
   │  │           ├─ security
   │  │           │  ├─ CustomUserDetail.java
   │  │           │  ├─ CustomUserDetailsService.java
   │  │           │  ├─ JwtFilter.java
   │  │           │  ├─ JwtTokenProvider.java
   │  │           │  ├─ OAuth
   │  │           │  │  ├─ CustomOAuth2User.java
   │  │           │  │  ├─ CustomOAuth2UserService.java
   │  │           │  │  ├─ OAuith2AuthenticationFailureHandler.java
   │  │           │  │  └─ OAuth2AuthenticationSuccessHandler.java
   │  │           │  ├─ SecurityConfig.java
   │  │           │  └─ handler
   │  │           │     ├─ CustomAccessDeniedHandler.java
   │  │           │     ├─ CustomAuthenticationEntryPoint.java
   │  │           │     └─ ExceptionFilter.java
   │  │           └─ user
   │  │              ├─ controller
   │  │              │  ├─ KakaoLoginPageController.java
   │  │              │  ├─ OwnerController.java
   │  │              │  └─ UserController.java
   │  │              ├─ converter
   │  │              │  ├─ UserConverter.java
   │  │              │  └─ UserInstrumentConverter.java
   │  │              ├─ domain
   │  │              │  ├─ User.java
   │  │              │  ├─ UserInstrument.java
   │  │              │  └─ enums
   │  │              │     └─ RoleType.java
   │  │              ├─ dto
   │  │              │  ├─ KakaoTokenResponseDto.java
   │  │              │  ├─ KakaoUserInfoResponseDto.java
   │  │              │  ├─ UserRequestDto.java
   │  │              │  └─ UserResponseDto.java
   │  │              ├─ repository
   │  │              │  ├─ UserInstrumentRepository.java
   │  │              │  ├─ UserRepository.java
   │  │              │  ├─ UserRepositoryCustom.java
   │  │              │  └─ UserRepositoryImpl.java
   │  │              └─ service
   │  │                 ├─ KakaoService.java
   │  │                 ├─ UserService.java
   │  │                 └─ UserServiceImpl.java
   │  └─ resources
   │     ├─ application.yml
   │     └─ templates
   │        ├─ KakaoReady.html
   │        ├─ home.html
   │        ├─ login.html
   │        ├─ static
   │        │  └─ kakao_login_medium_narrow.png
   │        └─ success.html
   └─ test
      └─ java
         └─ com
            └─ umc7
               └─ ZIC
                  ├─ ZicApplicationTests.java
                  └─ practiceRoom
                     └─ repository
                        ├─ PracticeRoomDetailRepositoryTest.java
                        ├─ PracticeRoomLikeRepositoryTest.java
                        └─ PracticeRoomRepositoryTest.java
```




</details>


  
</details>

## 설계

<details>
<summary> 아키텍쳐 </summary>
<br/>
 
### 백엔드 아키텍쳐
![image](https://github.com/user-attachments/assets/27384d3e-ff79-4730-8c1e-8a88ad232808)

<br/>

### 프론트 아키텍쳐
![image](https://github.com/user-attachments/assets/bdf238d5-5327-42b4-b8fd-ea6121443c75)

  
</details>


<details>
<summary>ERD</summary>

![image](https://github.com/user-attachments/assets/22072c82-95bd-43fc-88a7-575500e96d09)

</details>




## 프로젝트 협업 규칙

<details>
<summary>API 문서화 프로세스</summary>
<br>
 
 **API 설계 단계**
  - Notion을 활용한 초기 API 명세서 작성
  - Endpoint 정의
  - Request/Response 스키마 작성
    
<br>

 **배포 및 연동 단계**
- Swagger/OpenAPI를 통한 자동화된 문서 추가
  - API 변경사항 실시간 반영
  - Request/Response 스키마 자동화/동기화
  - Status Code 자동화/동기화
  - 필수 파라미터 및 제약조건 자동화 <br>

- API 연동 방식
  - AWS 프리티어 계정 활용
  - CI/CD를 통해 자동 배포 및 동기화 API 서버 배포

   </details>
   
<details>
<summary> 깃 전략 및 PR 규칙 </summary>

<br/>

  **Feature-Branch 전략(GitHub Flow)**

 ![image](https://github.com/user-attachments/assets/b96e30b5-7471-4cd1-9421-d12f6f1bb1cc)

    
  ### Main Branch
    - 배포 브랜치, 운영서버 
    - 직접적인 PUSH 불가 
    - develop → main 으로만 Pull Request 가능 
    
  ### Develop Branch
    - 개발 통합 브랜치: 
        - 다음 배포 버전을 위한 개발 코드 통합 
        - 기능 개발이 완료된 feature 브랜치들의 병합 지점 
        - 테스트 진행 시 베이스 브랜치 
    
  ### Feature Branch (branch명 : ex - feature#2-security) <br>
    - 기능 개발을 위한 작업 브랜치 
    - Issue 생성 및 할당 → develop에서 feature 브랜치 생성 → 개발 작업 수행 → develop으로 PR 요청 → 코드 리뷰 후 Merge 
    - Merge된 Remote Feature 브랜치는 자동 삭제 설정 
    - develop 기준으로 분기 
    
  ### Merge 방식
    - 마지막 approve 사람이 merge 하기 
    - feature 브랜치 
    - develop 브랜치 (본인이 스스로 approve 가능) 
    - main  브랜치 (1명 이상 approve시 가능) 
    
   </details>

    

    
<details>
<summary> 컨벤션 </summary> 

### 1. 커밋 유형
- 커밋 유형은 영어 소문자로 작성하기
- 급한 버그 수정은 대문자로 작성하기
    
    | 커밋 유형 | 의미 |
    | --- | --- |
    | `feature` | 새로운 기능 추가 |
    | `fix` | 버그 수정 |
    | `docs` | 문서 수정 |
    | `style` | 코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우 |
    | `refactor` | 코드 리팩토링 |
    | `test` | 테스트 코드, 리팩토링 테스트 코드 추가 |
    | `chore` | 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore |
    | `comment` | 필요한 주석 추가 및 변경 |
    | `rename` | 파일 또는 폴더 명을 수정하거나 옮기는 작업만인 경우 |
    | `remove` | 파일을 삭제하는 작업만 수행한 경우 |
    | `merge` | 병합 과정에서 충돌을 해결한 경우 |
    | `BREAKING CHANGE` | 커다란 API 변경의 경우 |
    | `HOTFIX` | 급하게 치명적인 버그를 고쳐야 하는 경우 |


### 2. 코드 유형
**명명 규칙**

- 상수는 영문 대문자, 스네이크 표기법을 사용 (예: RESPONSE_TIMEOUT)
- 변수 및 함수는 카멜 케이스를 사용 (예: UserId)
- 폴더 네이밍은 카멜 케이스를 기본 (예: PracticeRoom)
- DB는 스네이크- jpa에서 카멜케이스 사용시 자동 변환 해 줌
- URL, 파일명 등은 kebab-case를 사용 (예: /user-email-page )



### **블록 구문**

- 한 줄짜리 블록일 경우라도 {}를 생략하지 않고, 명확히 줄 바꿈 하여 사용한다

### **기타**

- 명시되지 않은 사항은 Google Java Style Guide와 같은 표준 자바 스타일 가이드를 준수한다.

---

</details>

  

## 팀원 소개

<table>
  <tr>
    <td>
        <a href="https://github.com/selloriwoo">
            <img src="https://avatars.githubusercontent.com/u/39435633?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/mono0801">
            <img src="https://avatars.githubusercontent.com/u/59917628?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/DongWooKim4343">
            <img src="https://avatars.githubusercontent.com/u/106728608?v=4" width="100px" />
        </a>
    </td>
  </tr>
  <tr style="text-align: center;">
    <td><b>박재락</b></td>
    <td><b>김현호</b></td>
    <td><b>김동우</b></td>
  </tr>
<!--   <tr>
    <td><b>로그인, 회원가입 <br /></b></td>
    <td><b>예약, 결제 </b></td>
    <td><b>연습실, 연습방 <br /></td>
  </tr> -->
</table>
