## 📗 EC2 인스턴스 설정

### EC2 기본 정보

- 인스턴스 타입: `t2.micro`
- 운영체제: `Ubuntu 24.04 LTS`
- 탄력적 IP 할당

### EC2 인스턴스 
![EC2 인스턴스 요약](images/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-07-03%20200205.png)

### 보안 그룹 설정  
![EC2 보안 그룹](images/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-07-03%20182639.png)
- ➕ 이후 인텔리제이 연동을 위한 8080 포트 추가

### 탄력적 IP 설정  
![EC2 탄력적 IP](images/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-07-03%20190153.png)

---

## 📘 RDS 설정 및 애플리케이션 연결

### RDS 인스턴스 정보

- DB 엔진: MySQL 8.0.41
- 인스턴스 클래스: db.t3.micro
- 포트: 3306
- RDS 보안 그룹 인바운드 규칙에 EC2 인스턴스의 보안 그룹을 허용하여 접근 가능하도록 설정

### RDS 인스턴스
![RDS 인스턴스](images/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-07-03%20200225.png)

### RDS 연결 정보 및 엔드포인트
- AWS RDS 콘솔에서 확인한 엔드포인트: `database-1.c726msaus0bb.ap-northeast-2.rds.amazonaws.com:3306`
- Spring Boot의 `application.properties`에서는 `${MYSQL_URL}`, `${MYSQL_USER}`, `${MYSQL_PASSWORD}`, `${JWT_SECRET_KEY}`와 같은 환경 변수 기반 설정을 사용하였음
- 애플리케이션 실행 전, git bash에서 아래와 같이 환경 변수를 직접 설정하여 연결 정보를 주입
```bash
export MYSQL_URL=jdbc:mysql://database-1.c726msaus0bb.ap-northeast-2.rds.amazonaws.com:3306/todo?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true
export MYSQL_USER='username'
export MYSQL_PASSWORD='password'
```
- 이후 `java -jar expert-0.0.1-SNAPSHOT.jar` 명령어로 애플리케이션을 실행하여 RDS에 정상적으로 연결되었고, 연결 성공 로그가 출력

### RDS 보안 그룹 설정 (EC2 접근 허용)
- RDS 보안 그룹 인바운드 규칙에서 EC2 인스턴스가 속한 보안 그룹을 허용함으로써 EC2에서 접근 가능하도록 설정

### 애플리케이션 DB 연결 로그  
![DB 연결 성공 로그](images/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-07-03%20202350.png)

### application.properties 설정

```properties
spring.datasource.url=${MYSQL_URL}
spring.datasource.username=${MYSQL_USER}
spring.datasource.password=${MYSQL_PASSWORD}

jwt.secret.key=${JWT_SECRET_KEY}
```

---

## 🔍 Health Check API

- 서버 상태를 확인할 수 있는 API
- 인증 없이 누구나 접근할 수 있다.

### 엔드포인트

- GET localhost:8080/actuator/health

### application.properties 설정

```properties
management.endpoints.web.exposure.include=health
```

### 응답 예시
```json
{
  "status": "UP"
}
```
