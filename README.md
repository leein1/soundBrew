# SoundBrew

## 📝 프로젝트 소개
<br>
회원/구독제 음원 판매 사이트 
<br>
<br>

### 프로젝트 배경
<br>
팀원 전부 실용음악과 출신으로, 음원 공유 사이트를 만들어 보고 싶다는 목표로 주제를 정하게 되었습니다.<br>
REST를 최대한 준수하는것이 목표였으며, '실제 회사에서 프로젝트를 한다면 어떻게 진행할까' 를 중점으로 개발하였습니다.
<br>
<br>

### 프로젝트 참여 인원
<br>
이인원 경동흔 각 주소<br>
사진도 좋을듯 번호라던가 메일이라던가 
<br>
<br>

## 상세 문서
<br>
아래는 Notion으로 작성한 프로젝트 전반에 대한 상세 문서 링크 입니다.<br>
<br>
https://leeinwon.notion.site/3-1aea7464bbfc80ec84f0d3b7c0a5a991
<br>
<br>

## 🛠 사용 기술

<span>
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white">
<img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white">
<img src="https://img.shields.io/badge/jsonwebtokens-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white">
</span>
<br>
<span>
<img src="https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">
<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=HTML&logoColor=white">
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white">
<img src="https://img.shields.io/badge/axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white">
</span>
<br>
<span>
<img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
<img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">
<img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
</span>
<br>
<span>
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
</span>
<br>

## 📊 시스템 구조
<br>

```mermaid
graph TD
    subgraph Web_Browser["브라우저 (SPA)"]
        Browser["Browser"]
    end

    subgraph AWS ["AWS"]
        subgraph EC2 ["EC2"]
            
            subgraph Server_Layer ["Server Layer"]

                subgraph Security_Layer ["Security Layer"]
                    SpringSecurity["Spring Security"]
                    JWT["JWT Token Authentication"]
                end

                subgraph View_Layer ["View Layer"]
                    ViewController["View Controller"]
                end

                Controller["API Controller"] 

                Service["Service Layer"]
            end
        
        end

        subgraph RDS ["Amazon RDS"]
            Database["RDS (MySQL/PostgreSQL)"]
        end

        subgraph S3 ["Amazon S3"]
            StaticFiles["Static Files"]
        end
    end

    %% 요청 흐름
    Browser -->|최초 뷰 요청| ViewController
    ViewController --> |SPA를 위한 HTML , JS 제공| Browser

    Browser -->|API 요청| SpringSecurity
    SpringSecurity -->|Token 검증| JWT
    JWT --> |Token 검증 결과| SpringSecurity
    SpringSecurity -->|인증 성공| Controller
    Controller -->|요청 처리| Service
    Service -->|데이터 요청| Database
    Service -->|파일 요청| StaticFiles

    %% 응답 흐름
    Database -->|데이터 응답| Service
    StaticFiles -->|파일 응답| Service
    Service -->|처리결과 응답| Controller
    Controller -->|Api 응답| Browser

```
<br>

## 데이터베이스 구조

사진 첨부

## 프로젝트 구조

```plaintext
src/
└── main/
    ├── java/
    │   └── com.soundbrew.soundbrew/
    │       ├── config/
    │       ├── controller/
    │       ├── domain/
    │       ├── dto/
    │       ├── handler/
    │       ├── repository/
    │       ├── security/
    │       ├── service/
    │       ├── util/
    │       └── SoundBrewApplication
    └── resources/
        ├── static/
        ├── templates/
        ├── application.yml
        ├── application-dev.yml
        └── application-prod.yml
```

## 🔍 주요 기능( 좀 더 자세히x -> 적절한 단어 찾아봐야 할듯)
1. 회원 관리
   - JWT 기반 인증
   - OAuth2.0 소셜 로그인

2. 음원 관리
   - 앨범 
   - 음원

3. 음원 재생
4. 파일 업/다운로드
5. 구독 결제

## 🎯 요약
사이트 사진이라던가 주요 기능들 스크린샷 너무 많이 x

