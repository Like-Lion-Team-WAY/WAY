# 🗯️ WAY : 익명 질문 커뮤니티 사이트
## Period
<mark>2024.07.23 ~ 2024.09.06</mark>
## Description
### ❔기획 배경❔
현대 사회에서 익명성을 활용한 커뮤니티 사이트를 통해, 고백이나 친해지기 등의 용기 부족으로 표현하지 못한 감정을 솔직하게 전하고, 새로운 인간관계를 형성할 수 있는 안전한 소통 공간을 제공하고자 WAY 라는 익명 질문 커뮤니티 서비스를 만들게 되었습니다.

### ⭐기대 효과 및 차별성⭐
익명성을 통해 사용자는 민감한 주제나 개인적인 문제에 대해 자유롭게 질문하고 답변할 수 있으며, 다양한 사람들과의 소통을 통해 풍부한 의견을 얻을 수 있습니다. <br>
질문을 받은 사람은 1:1 채팅 요청을 통해 실명으로 전환할 기회를 가질 수 있으며, 이를 통해 커뮤니케이션 능력과 자신감을 회복할 수 있습니다. 
또한, 익명 질문과 답변이 게시글로 공개되어 사용자의 영향력을 자연스럽게 드러낼 수 있는 기회를 제공합니다.

## 개발 환경

## Feature
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
  <br>

  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
  <img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">
  <br>

  <img src="https://img.shields.io/badge/apache tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=white">
  <br>
  
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <br>
</div>

## Developers

- [이나연](https://github.com/leenayeonnn)
- [변혜빈](https://github.com/hye2021)
- [이유준](https://github.com/L-U-Ready)
- [정호현](https://github.com/Firemanhyeon)
- [최혜진](https://github.com/chhyejin)

## 프로젝트 구조

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
