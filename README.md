# CRUD 웹 게시판 제작

웹 게시판입니다.

Enum클래스로 접근권한을 명시하고, 타임리프의 sec:authorize의 익명사용자 ANONYMOUS도 함께 SecurityConfig클래스에 설정해두어 회원권한에 따라

혹은 특정회원인 경우에만 사용할 수 있는 기능이 간단히 구현되어 있습니다.

새로운 기능이 추가되고 기존의 기능을 개선할 때 확장에 용이하기 위해 패키지 정리에 신경을 써보고, 최대한 특정 구현체에 의존되지 않게 생각을 해봤습니다.

하지만 로그와 테스트케이스에 대해서 많은 부족함을 느꼈습니다. 로그를 묶어서 어떻게 정리할 것인지, 테스트케이스는 순서에 구애받지 않고 어떻게 실행해볼 것인지 고민하고 개선해나갈 예정입니다.



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
