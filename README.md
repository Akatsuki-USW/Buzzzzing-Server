# 복쟉복쟉
실시간 혼잡도 기반 장소 추천 서비스 🐑 ✈️ 

![Group 677(3)](https://github.com/Akatsuki-USW/Buzzzzing-Server/assets/72124326/3dcdbed3-b290-4639-b35d-e1c2c31ab0b2)

# 복쟉복쟉

주요 공간별 인구 혼잡 현황을 쉽게 알 수 있다면 어떨까?

: 복쟉복쟉 서비스는 서울시, SK의 혼잡도 API를 기반으로 장소별로 현재 인구 혼잡도 현황과 미래 인구 혼잡도 예측치를 제공하고, 유저의 세부 장소 추천 및 커뮤니티 공간을 마련합니다. 

### 💡 Features

- 소셜 로그인 (카카오)
- 서울시, SK API 혼잡도 데이터 적재
- 장소 목록, 장소별 혼잡도 현황, 과거 데이터 기반 미래 혼잡도 예측
- 장소별 스팟(세부 장소 추천) 및 댓글, 대댓글
- 휴면 유저 공지 메일 스케줄링
- 유저 신고 및 차단
- 앱 활동 푸시 알림(FCM)
- 관리자 페이지

## ⚒️ Tech Stack

- Spring Boot 3.0 (Java 17)
- Spring Security, OAuth2, JWT
- JPA, Spring Data JPA, QueryDSL
- MySQL
- AWS Hosting : EC2, RDS, S3, CloudWatch, SMS
- CI/CD : Github Actions, Docker
- Firebase Cloud Messaging
- Spring Scheduling. Spring Mail
- Test : JUnit, Mock

### ERD
![buzzing_erd](https://github.com/Akatsuki-USW/Buzzzzing-Server/assets/72124326/2248f961-ade7-4a59-a716-be7422c8edf7)

### System Architecture
![Web App Reference Architecture(1)](https://github.com/Akatsuki-USW/Buzzzzing-Server/assets/72124326/104981fc-29e0-48ee-b0d8-edd4965350d8)
