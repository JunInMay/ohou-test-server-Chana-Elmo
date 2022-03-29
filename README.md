# 오늘의집\_B 개발 일지

---

## 2022-03-19 진행상황

### 공통

- IOS 파트와 개발 범위 설정 및 기획
- 회원가입/로그인에 필요한 ERD 설계
- 개발스택 선정

### 엘모

- 기획서 작성

### 차나

- 기획서 작성

---

## 2022-03-20 진행상황

### 엘모

- 유저 + SNS 영역 ERD 검토 및 수정

### 차나

- 유저 + SNS 영역 ERD 설계

---

## 2022-03-21 진행상황

### 엘모

- 로컬 회원가입/로그인 API 구현
- 기획서 수정
- EC2 서버 구축
- EC2 서버 SSL 설정
- EC2 서버 리버스 프록시 설정

### 차나

- 카카오 로그인 API 구현
- 기획서 수정
- EC2 서버 구축
- EC2 서버 SSL 설정
- EC2 서버 리버스 프록시 설정

---

## 2022-03-22 진행상황

### 엘모

- 스토어 ERD 설계
- 로컬 로그인 유효 이메일 검증 로직 추가
- 로컬 회원가입/로그인 API 리스트업

### 차나

- 유저 + SNS ERD 피드백 반영 / 일부 수정
- 유저 + SNS API 리스트업

---

## 2022-03-23 진행상황

### 엘모

- 스토어 ERD 개선 및 항목 추가
- 스토어 API 리스트업

### 차나

- 댓글 ERD 수정
- 회원가입 API 수정
- 카카오 로그인 API 구동을 위한 세팅
- 미디어 피드 상세 조회 API 구현
- 미디어 피드 댓글 달기 API 구현
- user 더미 데이터 추가 작업
- feed 더미 데이터 추가 작업
- media 더미 데이터 추가 작업
- media_feed 더미 데이터 추가 작업
- comment 더미 데이터 추가 작업

## 2022-03-24 진행상황

### 엘모

- JPA 적용 테스트
- JPA 이슈 발견 및 해결

### 차나

- 미디어 피드 상세 조회 API 수정 (미디어 피드 상세 조회 API를 통해 조회되는 사진 피드 아래에 키워드 조회 기능 추가)
- 미디어 피드 리스트 조회 API 작성 - 정렬, 필터 기능은 추후 보완
- 내 기본 정보 조회 API 작성
- 미디어 피드 댓글 달기 API 구현
- 키워드 더미 데이터 추가 작업
- 피드, 키워드 포함 관계 더미 데이터 추가 작업
- 스크랩북 더미 데이터 추가 작업
- 스크랩북, 피드 포함 관계 더미 데이터 추가 작업
- 좋아요 더미 데이터 추가 작업

## 2022-03-25 진행상황

### 엘모

- 상품/오늘의딜/인기상품 테이블 생성 및 더미 데이터 추가
- DB 테스팅

### 차나

- 파이어베이스 스토리지 구축 및 데이터 저장
- 사진 더미 데이터 추가 50개
- 사진 묶음 더미 데이터 추가 10개
- 집들이 더미 데이터 추가 10개
- 노하우 더미 데이터 추가 10개
- 홈 - 인기 섹션 1번 조회 API 구현
- 홈 - 인기 섹션 2번 조회 API 구현
- 홈 - 인기 섹션 3번 조회 API 구현
- 홈 - 인기 키워드별 사진 피드 조회 API 구현
- 사진 더미 데이터 특정 유저의 보유 관계에 맞게 수정
- 홈-인기 인기 사진 묶음 TOP 10 API 구현
- 9개 공간별 유저가 가장 최근에 업로드한 미디어 조회 API 구현
- 특정 유저가 올린 미디어 개수 조회 API 구현
- 동영상 / 사진을 firebase에 업로드하는 프로세스에 대한 이해 필요(어떻게 클라이언트가 파일을 보내주고, 서버는 어떻게 받고, 어떻게 서버가 firebase에 업로드하는지)

## 2022-03-26 진행상황

### 엘모

- 오늘의딜/인기상품 리스트 출력 API 추가 및 테스팅

### 차나

- 동영상 더미 데이터 추가 20개
- 동영상 썸네일 더미 데이터 추가 20개
- 홈-인기 인기 동영상 피드 TOP 10 API 구현
- 미디어 피드 리스트 조회 API 정렬, 필터 기능 추가
- 다양한 필터 기능이 있었지만 해당 기능 관련해서 정확한 스펙을 파악하기가 힘들고, 우선순위와 난이도 등을 따진 정황상 그 외 필터링은 구현하기 힘들 것이라 판단하여 일부 필터만 구현
- 특정 피드의 상단에 들어가는 작성자 정보 + 피드 업로드 타임 조회 API 구현
- 미디어 피드 리스트 조회 API 가로 뷰에서도 보완되도록 API 수정
- 팔로우탭 - 유저가 팔로우한 키워드 + 유저들의 피드 리스트 조회 API 구현

## 2022-03-27 진행상황

### 엘모

- 인기상품 리스트 조회 API 구현
- 오늘의 딜 상품 리스트 조회 API 구현
- 현재 로그인 된 유저의 최근 본 상품 리스트 조회 API 구현
- 필요 테이블 더미데이터 추가

### 차나

- 유저 팔로우하기 API 구현
- 집들이 리스트 조회 및 정렬, 필터 API 구현
- 집들이 더미 데이터 추가 20개
- 유저 더미 데이터 추가 13개
- 노하우 더미 데이터 추가 20개
- 전문가 집들이 더미 데이터 생성 20개
- 노하우 리스트 조회 및 정렬, 필터 API 구현
- 전문가 집들이 리스트 조회 및 정렬, 필터 API 구현
- 미디어 피드 메타 데이터 조회 API 구현
- 미디어 피드 하단 정보 조회 API 구현

## 2022-03-28 진행상황

### 엘모

### 차나
- 해당 미디어 피드 작성자가 올린 다른 미디어 피드 조회 API 구현
- 미디어 피드에 달린 댓글 일부 + 댓글 개수 정보 조회 API 구현
- 미디어 피드 댓글 리스트 조회 API 구현
- 피드 북마크하기 API 구현
- 스크랩북 만들기 API 구현
- 특정 유저의 메인 스크랩북 최상단 정보 조회 API 구현

## 2022-03-29 진행상황

### 엘모

### 차나
- 서드 파티 네이버 API 활용 - 텍스트 중 불건전한 워드 판별하기 API 구현
- 특정 스크랩북의 최상단 정보 조회 API 구현
- 특정 스크랩북의 모든 피드 조회 API 구현
- 특정 유저가 만든 모든 서브 스크랩북 조회 API 구현
- 2차 피드백 API 명세서 수정사항 수정(쿼리스트링에 예시 값 넣어서 혼동 막기)
- 2차 피드백 ERD 수정사항 수정(status 넣기)
- 특정 스크랩북의 모든 미디어 관련 피드 조회 API 구현
- 특정 스크랩북의 서브 스크랩북 조회 API 구현
- 특정 스크랩북의 집들이 피드 조회 API 구현
- 특정 스크랩북의 노하우 피드 조회 API 구현
- 피드 북마크 취소 API 구현
- 피드백 반영 이후 구조가 변경됨에 따라 발생한 API 오류 수정
- 스크랩북 삭제하기 API 구현
- 스크랩북 내용 수정하기 API 구현

## 2022-03-30 진행상황

### 엘모

### 차나
- 특정 유저의 메인 스크랩북의 모든 피드 조회 API 구현
- 특정 유저의 메인 스크랩북의 모든 미디어 관련 피드 조회 API 구현