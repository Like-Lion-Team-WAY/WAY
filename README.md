# 🗯️ WAY : 익명 질문 커뮤니티 사이트
## Period
<mark>2024.07.23 ~ 2024.09.06</mark>
## Description

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
