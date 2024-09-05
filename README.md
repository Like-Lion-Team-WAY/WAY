# 🗯️ WAY : 익명 질문 커뮤니티 사이트
[WAY 사이트](http://3.34.10.59/main)

## 📅 개발 기간

<mark>2024.07.23 ~ 2024.09.06</mark>

## 🖥️ 프로젝트 개요

### ❔기획 배경❔

현대 사회에서 익명성을 활용한 커뮤니티 사이트를 통해, 고백이나 친해지기 등의 용기 부족으로 표현하지 못한 감정을 솔직하게 전하고, 새로운 인간관계를 형성할 수 있는 안전한 소통 공간을 제공하고자 WAY 라는 익명 질문 커뮤니티 서비스를 만들게 되었습니다.

### ⭐기대 효과 및 차별성⭐

익명성을 통해 사용자는 민감한 주제나 개인적인 문제에 대해 자유롭게 질문하고 답변할 수 있으며, 다양한 사람들과의 소통을 통해 풍부한 의견을 얻을 수 있습니다. <br>
질문을 받은 사람은 1:1 채팅 요청을 통해 실명으로 전환할 기회를 가질 수 있으며, 이를 통해 커뮤니케이션 능력과 자신감을 회복할 수 있습니다.
또한, 익명 질문과 답변이 게시글로 공개되어 사용자의 영향력을 자연스럽게 드러낼 수 있는 기회를 제공합니다.

## 🍄 개발 환경

<div align=center><h1>📚 STACKS</h1></div>

<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <br>
  
  <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> 
  <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> 
  <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> 
  <img src="https://img.shields.io/badge/jquery-0769AD?style=for-the-badge&logo=jquery&logoColor=white">
  <br>


  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/elasticsearch-6DB33F?style=for-the-badge&logo=elasticsearch&logoColor=green">
  <img src="https://img.shields.io/badge/mongodb-6DB33F?style=for-the-badge&logo=mongodb&logoColor=green">
  <img src="https://img.shields.io/badge/apachekafka-231F20?style=for-the-badge&logo=apachekafka-231F20&logoColor=white">
  <br>

  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
  <img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> 
  <img src="https://img.shields.io/badge/jsonwebtokens-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"> 
  <img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">
  <br>

  <img src="https://img.shields.io/badge/apache tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=white">
  <img src="https://img.shields.io/badge/amazons3-4479A1?style=for-the-badge&logo=amazons3&logoColor=white"> 
  <img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> 
  <img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"> 
  <img src="https://img.shields.io/badge/awselasticloadbalancing-8C4FFF?style=for-the-badge&logo=awselasticloadbalancing&logoColor=white"> 
  <br>
  
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white"> 
  <br>
  
</div>

## 😉 설치 및 실행 방법

- spring boot + jdk 21
- **docker**
  [docker-compose.yml 모음](https://www.notion.so/docker-compose-yml-bde50cd80961486ca1d87f25716efa6d?pvs=21)
  - kafka + zookeeper
  - elasticsearch
  - mongo
  - mysql <br>
    [DB 쿼리문](https://www.notion.so/DB-da96f69e9519427f80c4c6c27f55c22f?pvs=21)
- 로컬의 git bash 에서 깃허브 링크를 복사하여 클론합니다.
  ```
  git clone https://github.com/Like-Lion-Team-WAY/WAY.git
  ```
- "/src/main/resources/application.yml” 경로로 yml 파일을 추가해줍니다.
  ```
  spring:
    profiles:
      active: prod
    servlet:
      multipart:
        max-file-size: 50MB
        max-request-size: 50MB
    elasticsearch:
      uris: ${ELASTIC_URIS}
      username: ${ELASTIC_USERNAME}
      password: ${ELASTIC_PASSWORD}

    application:
      name: WAY
    datasource:
      url: ${DB_URL}
      username: ${DB_USERNAME}
      password:  ${DB_PASSWORD}
      driver-class-name: com.mysql.cj.jdbc.Driver
      hikari:
        max-lifetime: 150000
        idle-timeout: 100000
    jpa:
      hibernate:
        ddl-auto: update
      properties:
        hibernate:
          format_sql: true
          enable_lazy_load_no_trans: true
      open-in-view: false

    security:
      oauth2:
        client:
          registration:
            google:
              client-id: ${GOOGLE_ID}
              client-secret: ${GOOGLE_SECRET}
              scope:
                - profile
                - email
            naver:
              client-id: ${NAVER_ID}
              client-secret: ${NAVER_SECRET}
              client-name: Naver
              redirect-uri: http://localhost:8080/login/oauth2/code/naver
              authorization-grant-type: authorization_code
              scope:
                - name
                - email
            kakao:
              client-id: ${KAKAO_ID}
              client-secret: ${KAKAO_SECRET}
              client-name: Kakao
              client-authentication-method: client_secret_post
              redirect-uri: http://localhost:8080/login/oauth2/code/kakao
              authorization-grant-type: authorization_code
              scope:
                - account_email
                - profile_nickname
          provider:
            kakao:
              authorizationUri: https://kauth.kakao.com/oauth/authorize
              tokenUri: https://kauth.kakao.com/oauth/token
              userInfoUri: https://kapi.kakao.com/v2/user/me
              user-name-attribute: id
            naver:
              authorization-uri: https://nid.naver.com/oauth2.0/authorize
              token-uri: https://nid.naver.com/oauth2.0/token
              user-info-uri: https://openapi.naver.com/v1/nid/me
              user-name-attribute: response
    data:
      mongodb:
        uri: ${MONGO_URI}

    kafka:
      bootstrap-servers: ${KAFKA_SERVER}
      consumer:
        group-id: chat-group
        auto-offset-reset: earliest
        key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
        value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      producer:
        key-serializer: org.apache.kafka.common.serialization.StringSerializer
        value-serializer: org.apache.kafka.common.serialization.StringSerializer

  server:
    port: 8080
    serverName: ${SERVER_NAME}
  jwt:
    secretKey: ${JWT_SECRET_KEY}
    refreshKey: ${JWT_REFRESH_KEY}

  cloud:
    aws:
      credentials:
        access-key: ${AWS_ACCESS_KEY}
        secret-key: ${AWS_SECRET_KEY}

      s3:
        bucketName: ${AWS_BUCKET_NAME}

      region:
        static: ${AWS_REGION}
        auto: false

  ```

## 🔠 컨벤션

[컨벤션](https://www.notion.so/0b6735e7d71649c1be607aef75eaf608?pvs=21)
<br>

- 커밋 메시지 : `type: commit title (#이슈번호)`
- 브랜치 이름 : `type/이슈번호-branch-name `
- 이슈 제목 : `[Category] issue title`
- PR
  - 제목 : `[Category] issue title` (이슈 번호 제외)
  - 본문 : `close #이슈번호`
  - 머지 스타일 : squash and merge

## 🗃️ ERD

![erd](https://github.com/user-attachments/assets/dc9392ee-23c5-4395-966a-653f2ee71cf3)

## 🛠️ 기능

- 로그인
  - oauth2를 이용한 kakao, google, naver 로그인
- 피드
  - 피드 작성
    (비로그인 사용자) 작성된 피드를 구경할 수 있는 권한 가짐
    (로그인 사용자) 피드 작성 & 수정 & 삭제 & 대글 (대댓글) & 좋아요 & 스크랩 등
  - 사용자 피드 조회
    - Elastic Search 사용하여 user 검색
    - 관심사 검색을 통해 사용자 찾을 수 있음
- 질문
  - 로그인 & 비로그인 사용자가 익명 & 실명으로 질문
- 커뮤니티
  - 게시판 조회 & 생성 & 수정 & 삭제
  - 게시글 조회 & 생성 & 수정 & 삭제
  - 게시글 좋아요 & 스크랩 & 댓글(대댓글)
  - 게시판 & 게시글 검색
- 알림
  - SSE - 이벤트 발행해서 알림
    - 새 질문, 내가 단 질문의 답변
    - 내 피드의 댓글, 내 댓글의 대댓글
    - 채팅
- 채팅
  - Mongo + kafka + websocket을 이용한 채팅
  - 질문을 통한 유저간의 1:1 채팅 가능
  - 신고, 실명 공개, 채팅방 명 변경, 나가기 기능 제공
- 관리자
  - 유저로 부터 받은 신고 (질문, 피드, 댓글, 채팅) 처리
  - 유저에게 블루체크 권한 제공

## 👩‍💻 개발자

| 팀원                                      | 역할 | 주 담당 기능 |
| ----------------------------------------- | ---- | ------------ |
| [이나연](https://github.com/leenayeonnn)  | 팀장 | 채팅         |
| [변혜빈](https://github.com/hye2021)      | 팀원 | 알림         |
| [이유준](https://github.com/L-U-Ready)    | 팀원 | 커뮤니티     |
| [정호현](https://github.com/Firemanhyeon) | 팀원 | 유저 + CI/CD |
| [최혜진](https://github.com/chhyejin)     | 팀원 | 질문 + 피드  |

## 🌳 프로젝트 구조

```
way
  ├───admin
  │   ├───controller
  │   ├───domain
  │   ├───dto
  │   ├───repository
  │   └───service
  │       └───Impl
  ├───alarm
  │   ├───controller
  │   ├───domain
  │   ├───dto
  │   ├───event
  │   ├───repository
  │   └───service
  │       ├───kafka
  │       └───serviceImpl
  ├───board
  │   ├───controller
  │   │   └───request
  │   ├───domain
  │   ├───dto
  │   │   ├───request
  │   │   └───response
  │   ├───repository
  │   └───service
  │       └───Impl
  ├───chat
  │   ├───constant
  │   ├───controller
  │   │   └───rest
  │   ├───domain
  │   ├───dto
  │   ├───repository
  │   └───service
  │       ├───impl
  │       └───kafka
  │           └───imple
  ├───config
  ├───els
  │   ├───controller
  │   ├───domain
  │   ├───repository
  │   └───service
  │       └───serviceimpl
  ├───exception
  ├───feed
  │   ├───controller
  │   ├───domain
  │   ├───dto
  │   ├───repository
  │   ├───service
  │   │   └───imp
  │   └───util
  ├───file
  │   ├───controller
  │   └───service
  │       └───impl
  ├───jwt
  │   ├───exception
  │   ├───filter
  │   ├───service
  │   ├───token
  │   └───util
  └───user
      ├───controller
      │   └───restcontroller
      ├───domain
      ├───dto
      ├───oauth2
      │   ├───dto
      │   ├───handler
      │   └───service
      ├───repository
      ├───security
      └───service
```

## 🔗 라이센스

이 프로젝트에서 아래 라이브러리를 사용했습니다:

- [Font Awesome](https://fontawesome.com) - Licensed under [CC BY 4.0](https://fontawesome.com/license/free)
- [Bootstrap](https://getbootstrap.com) - Licensed under [MIT License](https://github.com/twbs/bootstrap/blob/main/LICENSE)

## 👌 문제 해결 (Troubleshooting) 및 FAQ

[[트러블슈팅] SSE ](https://www.notion.so/SSE-b0d2c6321e3d422dabb392038ead9cfd?pvs=21)

[[트러블슈팅] header요소 클릭에 따라 다른 aside요소 보여주기](https://www.notion.so/header-aside-4723d1e52634434993a2c28df38c1e38?pvs=21)

[[트러블슈팅] AWS RDS 현재 활동](https://www.notion.so/AWS-RDS-6f84846a71194581927d6c636f3d6b5b?pvs=21)

[[트러블슈팅] nginx - 카카오 로그인](https://www.notion.so/nginx-OAuth-497f7a3321bd4867a2b7aed1c998c719)

## 😙 향후 계획

- 코드 리펙토링
- 나쁜 말 필터링

## 😎 발표 자료

[1차 기획 발표](https://www.canva.com/design/DAGL6Azijjk/XFunBHtoLC4nzzufzPrcsw/edit?utm_content=DAGL6Azijjk&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton) <br>
[1차 중간 발표](https://www.canva.com/design/DAGNtVmtYeI/-sWADMXqv5v0ajQSte3aBg/view?utm_content=DAGNtVmtYeI&utm_campaign=designshare&utm_medium=link&utm_source=editor) <br>
[2차 기획 발표](https://dog-lightning-b4b.notion.site/2-0e296198df364989ac8406fb1a5327c1?pvs=74) <br>
[2차 최종 발표](https://www.canva.com/design/DAGPlbu1KaY/_PhW511rIiq1eFw2Shn7UA/view?utm_content=DAGPlbu1KaY&utm_campaign=designshare&utm_medium=link&utm_source=editor)
[2차 최종 발표_요약](https://www.canva.com/design/DAGP2N8ESTw/yp88vJvMtQ6PCOWneZLJ7g/view?utm_content=DAGP2N8ESTw&utm_campaign=designshare&utm_medium=link&utm_source=editor)

## 🎥 데모
[![Video Label](http://img.youtube.com/vi/wQImY0ZIj74/0.jpg)](https://youtu.be/wQImY0ZIj74)
