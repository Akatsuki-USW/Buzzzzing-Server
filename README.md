# 복쟉복쟉
실시간 혼잡도 기반 장소 추천 서비스 🐑 ✈️ 

![Group 677(3)](https://github.com/Akatsuki-USW/Buzzzzing-Server/assets/72124326/3dcdbed3-b290-4639-b35d-e1c2c31ab0b2)

# 복쟉복쟉

주요 공간별 인구 혼잡 현황을 쉽게 알 수 있다면 어떨까?

: 복쟉복쟉 서비스는 서울시, SK의 혼잡도 API를 기반으로 장소별로 현재 인구 혼잡도 현황과 미래 인구 혼잡도 예측치를 제공하고, 유저의 세부 장소 추천 및 커뮤니티 공간을 마련합니다. 

### 💡 Features

- 소셜 로그인 (카카오)
- 취미 생활, 소모임 생성, 모집, 관리
- 모임 내 자유로운 커뮤니티인 게시판, 댓글
- 모임 활동을 기록할 수 있는 아카이브
- 모임 내 작은 챌린지를 할 수 있는 투두리스트
- 모임 내 일정을 관리 할 수 있는 캘린더, 공지
- 앱 활동 푸시 알림

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
