# 복쟉복쟉

![복쟉배경](https://github.com/Akatsuki-USW/Buzzzzing-Server/assets/72124326/42828c59-0ebe-421b-bd3d-cda88760fbce)

주요 장소별 인구 혼잡 현황을 쉽게 알 수 있다면 어떨까?

복쟉복쟉 서비스는 서울시, SK의 실시간 혼잡도 API를 기반으로 장소별로 현재 인구 혼잡도 현황과 미래 인구 혼잡도 예측치를 제공하고, 유저의 세부 장소 추천 및 커뮤니티 공간을 마련합니다. 

### 💡 Features

- 인증 서비스 : 카카오 소셜 로그인
- 혼잡도 서비스 : 장소 조회 및 혼잡도 현황, 과거 데이터 기반 미래 혼잡도 예측
- 커뮤니티 서비스 : 장소별 세부 장소 추천(스팟) 조회 및 댓글, 대댓글
- 유저 서비스 : 휴면 유저 공지 메일 스케줄링, 유저 신고 및 차단, 앱 활동 푸시 알림(FCM)

## ⚒️ Tech Stack

- Framework & Language : Spring Boot 3.0 & Java 17
- Auth : Spring Security, OAuth2, JWT
- ORM : JPA, Spring Data JPA, QueryDSL
- DB : MySQL, Redis
- Test : JUnit(+ AssertJ), Mockito
- AWS : EC2, RDS, S3, CloudWatch, SNS
- CI/CD : Github Actions, Docker
- Firebase Cloud Messaging
- Spring Scheduling. Spring Mail
- Swagger

## 🔍 Architecture
### ERD
![buzzing_erd](https://github.com/Akatsuki-USW/Buzzzzing-Server/assets/72124326/2248f961-ade7-4a59-a716-be7422c8edf7)

### System Architecture
![Web App Reference Architecture(1)](https://github.com/Akatsuki-USW/Buzzzzing-Server/assets/72124326/104981fc-29e0-48ee-b0d8-edd4965350d8)
