# 🗯️ WAY : 익명 질문 커뮤니티 사이트
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

## 🔠 컨벤션
- 커밋 메시지 : `type: commit title (#이슈번호)`
- 브랜치 이름 : `type/이슈번호-branch-name `
- 이슈 제목 : `[Category] issue title`
- PR  
  - 제목 : `[Category] issue title` (이슈 번호 제외)  
  - 본문 : `close #이슈번호`
  - 머지 스타일 : squash and merge

## 🗃️ ERD
![erd](https://github.com/user-attachments/assets/3a3b0484-b659-46ee-b894-f5a09358de11)

## 🛠️ 기능
- 질문하기
- 마이페이지
- 커뮤니티
- 채팅

## 👩‍💻 개발자
- [이나연](https://github.com/leenayeonnn)
- [변혜빈](https://github.com/hye2021)
- [이유준](https://github.com/L-U-Ready)
- [정호현](https://github.com/Firemanhyeon)
- [최혜진](https://github.com/chhyejin)

## 🌳 프로젝트 구조
```
  way
  ├───admin
  │   ├───controller
  │   ├───domain
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
  │       └───serviceImpl
  ├───board
  │   ├───api
  │   │   └───request
  │   ├───application
  │   │   ├───request
  │   │   └───response
  │   ├───domain
  │   └───repository
  ├───chat
  │   ├───controller
  │   │   └───rest
  │   ├───domain
  │   │   └───dto
  │   ├───repository
  │   └───service
  │       ├───impl
  │       └───kafka
  │           └───imple
  ├───common
  │   └───controller
  ├───config
  ├───els
  │   ├───controller
  │   ├───domain
  │   ├───repository
  │   └───service
  │       └───serviceimpl
  ├───feed
  │   ├───controller
  │   ├───domain
  │   │   └───dto
  │   ├───repository
  │   └───service
  │       └───imp
  ├───jwt
  │   ├───exception
  │   ├───filter
  │   ├───service
  │   ├───token
  │   └───util
  └───user
      ├───controller
      │   ├───restcontroller
      │   └───security
      ├───domain
      ├───dto
      ├───oauth2
      │   ├───dto
      │   ├───handler
      │   └───service
      ├───repository
      └───service
          └───serviceImpl
```

## 🔗 라이센스
이 프로젝트에서 아래 라이브러리를 사용했습니다:
- [Font Awesome](https://fontawesome.com) - Licensed under [CC BY 4.0](https://fontawesome.com/license/free)
- [Bootstrap](https://getbootstrap.com) - Licensed under [MIT License](https://github.com/twbs/bootstrap/blob/main/LICENSE)

## 😎 발표 자료
[1차 기획 발표](https://www.canva.com/design/DAGL6Azijjk/XFunBHtoLC4nzzufzPrcsw/edit?utm_content=DAGL6Azijjk&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton) <br>
[1차 중간 발표](https://www.canva.com/design/DAGNtVmtYeI/-sWADMXqv5v0ajQSte3aBg/view?utm_content=DAGNtVmtYeI&utm_campaign=designshare&utm_medium=link&utm_source=editor)
