## 0. Getting Started (시작하기)

![image](https://github.com/user-attachments/assets/34516cec-087b-4da3-812b-eb469e1c2e52)


# 1. Project Overview (프로젝트 개요) 
- 프로젝트 이름: 댕댕 플레이스
- 개발 기간 : 2024.11.12 ~ 2024.12.20
- 배포 운영 기간 : 2024.11.20 ~ 2024.12.26 - [댕댕 플레이스](https://daengplace.vercel.app/)

# 2. Team Members (팀원 및 팀 소개)
| 오유찬 | 김동규 | 박종혁 | 정재민 |
|:------:|:------:|:------:|:------:|
| <img src="https://i.namu.wiki/i/4qLKxb0WiFRbid5wm4Dt5dNFlkZjGDNTvXrEo19EWE59o-GDUWyyHYRlpM8IRwkEdFd4xpochkypCsrgA35COA.webp" alt="오유찬" width="150"> | <img src="https://github.com/user-attachments/assets/beea8c64-19de-4d91-955f-ed24b813a638" alt="김동규" width="150"> | <img src="https://fcseoulite.me/files/attach/images/192/288/604/010/36a3040967c668dc209899d2daa1969f.jpg" alt="박종혁" width="150"> | <img src="https://i.namu.wiki/i/5Veq9acZq3uqIUMsQbKyf4wjHiuk500_e7LUTtdWvG_2m7Wax-Anb5bFATOMsQReegqabE05_P6Swl9h9vUl3g.webp" alt="정재민" width="150"> |
| PL | BE | BE | BE |
| [GitHub](https://github.com/ohyuchan123) | [GitHub](https://github.com/Estar0622) | [GitHub](https://github.com/pardessuccess) | [GitHub](https://github.com/sai06266) |


# 3. Key Features (주요 기능 및 ERD & 아키텍쳐)
- **회원가입**
  - 이메일 인증을 통한 사용자 회원가입
  - 사용자의 정보 등록
  - 사용자의 반려견 정보 등록

- **로그인**
  - 소셜 로그인을 통한 간편 로그인

- **사용자 및 반려견 회원 관리**
  - 사용자 등록, 조회, 수정, 삭제(물리적 삭제)
  - 반려견 등록, 조회, 수정, 삭제
  - 나의 반려견 견종 검색
  - 사용자 즐겨찾기

- **동반 가능 시설 조회**
  - 장소 필터링

- **리뷰 관리**
  - 리뷰 등록, 조회, 수정, 삭제
  - OCR(영수증 리뷰를 통한 리뷰 인증 시스템)
  - 인기 리뷰 리스트 조회
  - 리뷰 좋아요
 
- **사용자 및 반려견 성향 진단**
  - 사용자 성향 진단
  - 반려견 성향 진단
  - 성향 진단 기반의 장소 추천

## 3.1 프로젝트 ERD
![image](https://github.com/user-attachments/assets/48eeb8b9-4cc5-4642-b67c-1574275ccf77)

## 3.2 프로젝트 아키텍쳐

![image](https://github.com/user-attachments/assets/4632b666-9c65-41e9-b402-0d9df312bf05)

## 3.3 API 명세서
> API 명세서의 경우 현재 노션에서 관리하고 있습니다.

![image](https://github.com/user-attachments/assets/2110705a-54f0-4d27-8de4-47c2b57ae44b)

# 4. Tasks & Responsibilities (작업 및 역할 분담)
|  |  |  |
|-----------------|-----------------|-----------------|
| 오유찬    |  <img src="https://i.namu.wiki/i/4qLKxb0WiFRbid5wm4Dt5dNFlkZjGDNTvXrEo19EWE59o-GDUWyyHYRlpM8IRwkEdFd4xpochkypCsrgA35COA.webp" alt="오유찬" width="100"> | <ul><li>프로젝트 계획 및 관리</li><li>백엔드 배포 관리(무중단 배포)</li><li>리뷰 CRUD</li><li>장소 필터링</li><li>추천 시스템 로직 구현</li><li>이미지 업로드</li></ul>     |
| 김동규   |  <img src="https://github.com/user-attachments/assets/beea8c64-19de-4d91-955f-ed24b813a638" alt="김동규" width="100">| <ul><li>소셜 로그인</li><li>**QUICK SERVICE - OCR(영수증 인증 리뷰)**</li></ul> |
| 박종혁   |  <img src="https://fcseoulite.me/files/attach/images/192/288/604/010/36a3040967c668dc209899d2daa1969f.jpg" alt="박종혁" width="100">    |<ul><li>즐겨찾기</li><li>회원가입</li><li>펫 CRUD</li></ul>  |
| 정재민    |  <img src="https://i.namu.wiki/i/5Veq9acZq3uqIUMsQbKyf4wjHiuk500_e7LUTtdWvG_2m7Wax-Anb5bFATOMsQReegqabE05_P6Swl9h9vUl3g.webp" alt="정재민" width="100">    | <ul><li>장소 필터링</li><li>리뷰 CRUD</li><li>성향 검사</li><li>추천 시스템</li></ul>    |


# 5. Technology Stack (기술 스택)
## 5.1 Language
|  |  |  |
|-----------------|-----------------|-----------------|
| Java    |<img src="https://cdn.iconscout.com/icon/free/png-256/free-java-60-1174953.png?f=webp" alt="JAVA" width="100">| 17.0.11 |

<br/>

## 5.2 Backend
|  |  |  |  |  |  |
|-----------------|-----------------|-----------------|-----------------|-----------------|-----------------|
| Spring    |  <img src="https://blog.kakaocdn.net/dn/diVmCB/btqOcQWrLh9/K1AW5ftq5ih97pkt2rK9nk/img.png" alt="spring" width="100">    | 3.3.5   |JWT   | <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS8LJrZbMTtW1Hv4-ZIGC8UghRJUl8e6rzS9g&s" alt="Jwt" width="100">  | |
| MySQL       |  <img src="https://rastalion.dev/wp-content/uploads/2019/04/mysql_PNG19.png" alt="mysql" width="100">    | 8.4.3 |Oauth2   | <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRmrtF1vDXPOPYq-Hu1IYMf9DZReMpMte2fnw&s" alt="Oauth2" width="100">  | |
| REDIS       |  <img src="https://rastalion.dev/wp-content/uploads/2019/09/redis.png" alt="redis" width="100">    | 7.2.6 |
| AWS CLOUD |  <img src="https://static-00.iconduck.com/assets.00/aws-icon-2048x2048-ptyrjxdo.png" alt="aws" width="100">    | | Docker   | <img src="https://img1.daumcdn.net/thumb/R800x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F9981E6375B8CF0802A" alt="Docker" width="100">  | |

<br/>

## 5.3 Cooperation
|  |  |  |  |
|-----------------|-----------------|-----------------|-----------------|
|  <img src="https://github.com/user-attachments/assets/483abc38-ed4d-487c-b43a-3963b33430e6" alt="git" width="100">  |  <img src="https://github.com/user-attachments/assets/34141eb9-deca-416a-a83f-ff9543cc2f9a" alt="Notion" width="100"> |  <img src="https://static-00.iconduck.com/assets.00/jira-icon-2048x2048-nmec2job.png" alt="Notion" width="100">    | <img src="https://blog.greggant.com/images/posts/2019-04-25-figma/Figma.png" alt="Notion" width="100">  


<br/>

# 6. Project Structure (프로젝트 구조)
```plaintext
project/
├── Dockerfile              # Docker 컨테이너 설정
├── HELP.md                 # 도움말 문서
├── README.md               # 프로젝트 설명 문서
├── build.gradle            # Gradle 빌드 설정
├── settings.gradle         # Gradle 프로젝트 설정
├── dev.env                 # 개발 환경 변수
├── imagedefinitions.json   # Docker 이미지 정의
├── gradle/                 # Gradle 래퍼 설정
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── mycom/
│   │   │           └── backenddaengplace/
│   │   │               ├── auth/                # 인증/인가 관련
│   │   │               │   ├── config/         # 보안, 웹 설정
│   │   │               │   ├── controller/     # 인증 관련 컨트롤러
│   │   │               │   ├── domain/         # 인증 관련 엔티티
│   │   │               │   ├── dto/            # 데이터 전송 객체
│   │   │               │   ├── handler/        # 인증 핸들러
│   │   │               │   ├── interceptor/    # 인터셉터
│   │   │               │   ├── jwt/            # JWT 관련
│   │   │               │   ├── repository/     # 데이터 접근 계층
│   │   │               │   └── service/        # 비즈니스 로직
│   │   │               ├── common/             # 공통 기능
│   │   │               ├── favorite/           # 즐겨찾기 기능
│   │   │               ├── member/             # 회원 관리
│   │   │               ├── ocr/                # 이미지 인식
│   │   │               ├── pet/                # 반려동물 관리
│   │   │               ├── place/              # 장소 관리
│   │   │               ├── review/             # 리뷰 기능
│   │   │               └── trait/              # 특성 관리
│   │   └── resources/
│   │       ├── application.yml                 # 기본 설정
│   │       ├── application-dev.yml             # 개발 환경 설정
│   │       ├── application-local.yml           # 로컬 환경 설정
│   │       ├── application-email.yml           # 이메일 설정
│   │       ├── application-security.yml        # 보안 설정
│   │       └── templates/                      # 템플릿 파일
│   └── test/
│       └── java/                               # 테스트 코드
└── .gitignore             # Git 제외 파일 목록
```

# 7. Development Workflow (개발 워크플로우)
## github ruleset 전략
- github에 rule을 적용해서 PR에 한 사람 이상이 approve를 해주지 않으면 develop에 머지가 불가능 하도록
- develop에서 바로 커밋 할 수 없도록 변경(api 통신 이전까지는 유지)
  
## 브랜치 전략 (Branch Strategy)
- 영문 소문자 사용
- 단어 구분은 하이픈(-) 사용
- 간결하고 명확하게 작성
- 이슈번호 포함 가능 (#123)

### Branch Type

- `feat/`: 새로운 기능 개발
- `fix/`: 버그 수정
- `refactor/`: 코드 리팩토링
- `chore/`: 빌드, 설정 변경
- `docs/`: 문서 수정
- `test/`: 테스트 코드 추가/수정
- `style/`: 코드 포맷팅, 세미콜론 누락 등

### Ex .)

- `feat/add-social-login`

# 8. Coding Convention
## Gitmoji 사용

- 🎉 `:tada:` : 프로젝트 시작
- ✨ `:sparkles:` : 새 기능
- 🐛 `:bug:` : 버그 수정
- ♻️ `:recycle:` : 코드 리팩토링
- 📝 `:memo:` : 문서 추가/수정
- 🔧 `:wrench:` : 설정 파일 추가/수정 (gradle, yml…등등)
- ✅ `:white_check_mark:` : 테스트 추가/수정
- 🚀 `:rocket:` : 배포
- 📦 `:package:` : 패키지 추가/수정
- 🔥 `:fire:` : 코드 제거

<br/>

## 커밋 예시
```
== ex1
✨Feat: "회원 가입 기능 구현"

SMS, 이메일 중복확인 API 개발
```

# 10. 컨벤션 수행 결과
![image](https://github.com/user-attachments/assets/46f5fdb9-79b6-4381-bbdf-5bab895fab72)
![image](https://github.com/user-attachments/assets/0236ea3c-8ddb-4529-aae2-8920f59f840f)
