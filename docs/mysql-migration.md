# MariaDB → MySQL 전환 기록

## 개요
- 목표: Spring Boot 백엔드의 DB를 MariaDB에서 MySQL로 전환하고, EC2에서 정상 기동
- 범위: 데이터소스 드라이버/디알렉트/URL 변경, 의존성 교체, EC2 MySQL 설정 및 접속 문제 해결 (Elasticsearch는 제외)

## 주요 변경 파일
- build.gradle
  - runtimeOnly: `org.mariadb.jdbc:mariadb-java-client` → `com.mysql:mysql-connector-j:8.1.0`
- src/main/resources/application.yml
  - spring.datasource.driver-class-name: `org.mariadb.jdbc.Driver` → `com.mysql.cj.jdbc.Driver`
  - spring.jpa.properties.hibernate.dialect: `org.hibernate.dialect.MariaDBDialect` → `org.hibernate.dialect.MySQLDialect`
  - spring.datasource.url: `jdbc:mysql://localhost:3306/lucky_b_key?serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true&useSSL=false&connectExpiredPassword=true`
  - spring.datasource.username/password: `luckybuser` / `hackathon123!`
- (.env)
  - DB_USERNAME/DB_PASSWORD를 애플리케이션 계정으로 동기화

## EC2(MySQL) 작업 이력 요약
- MySQL 서비스 비정상 모드 원인: systemd 환경변수 `MYSQLD_OPTS`에 `--skip-grant-tables --skip-networking` 잔존
  - 조치: `sudo systemctl unset-environment MYSQLD_OPTS && sudo systemctl restart mysqld`
- 최초 접속/인증 이슈들
  - 드라이버 미존재: `ClassNotFoundException: com.mysql.cj.jdbc.Driver` → Gradle 의존성 교체로 해결
  - 암호 만료: `Your password has expired` → JDBC URL에 `connectExpiredPassword=true` 추가 후 계정 암호 갱신
  - 인증 플러그인: `sha256_password` 관련 → MySQL 8 기본(`caching_sha2_password`) 사용, `allowPublicKeyRetrieval=true` 설정
  - URL 파라미터 손상: 잘못된 `useSSL=falseconnectExpiredPassword=true` → `&`로 구분하여 정상화
- 계정/권한
  - DB: `lucky_b_key`
  - 계정: `luckybuser`@`localhost` / 비밀번호 `hackathon123!`
  - 권한: `GRANT ALL PRIVILEGES ON lucky_b_key.* TO 'luckybuser'@'localhost'`

## 재현/기동 방법
```bash
# EC2에서
sudo systemctl status mysqld | cat
mysql -u root -p
# 필요 시
ALTER USER 'root'@'localhost' IDENTIFIED BY '새암호';
CREATE USER IF NOT EXISTS 'luckybuser'@'localhost' IDENTIFIED BY '새암호';
GRANT ALL PRIVILEGES ON lucky_b_key.* TO 'luckybuser'@'localhost';
FLUSH PRIVILEGES;

# 앱 기동 (프로젝트 루트)
./gradlew bootRun
```

## 남은 이슈(보류)
- Elasticsearch: `spring.data.elasticsearch.url` 플레이스홀더 미해결 → 별도 작업에서 처리 예정

## 보안 메모
- 현재 비밀번호는 임시용이며 배포 전 교체 권장
- 보안 그룹/SSH 키 권한 점검 권장

