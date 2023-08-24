# CRUD 웹 게시판 제작

웹 게시판입니다.

회원권한에 따라 접근가능한 권한에 다른 간단한 기능이 포함되어있습니다.

Enum클래스로 접근권한을 명시하고, 타임리프의 sec:authorize의 익명사용자 ANONYMOUS도 함께 SecurityConfig클래스에 설정해두어 회원권한에 따라

혹은 특정회원인 경우에만 사용할 수 있는 기능이 간단히 구현되어 있습니다.


### 개발 환경

* Spring Boot 3.1.0
* Java 17
* gradle 7.6.1
* Intellij IDEA
* MariaDB
* GitHub (https://github.com/25shshsh/Sample)

### 사용 방법


./gradlew :test --tests "Integration"

./gradlew bootRun

(모든 회원의 비밀번호는 1111 입니다.)
