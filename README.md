## Githerb 백엔드

## version
- Spring Boot 2.5.1
- JAVA 11
- Github API 1.131
- Gradle 6.8.3

<br>

## 프로젝트 설명
<img src="https://user-images.githubusercontent.com/69130921/123449100-50e8f100-d616-11eb-8ec5-c5c0675e1621.png" width="400" height="400">
<img src="https://user-images.githubusercontent.com/69130921/123449094-4fb7c400-d616-11eb-9721-7b779e8c516a.png" width="400" height="400">
<br>
<img src="https://user-images.githubusercontent.com/69130921/123449085-4e869700-d616-11eb-9565-b9ef83ccfd51.png" width="400" height="400">
<img src="https://user-images.githubusercontent.com/69130921/123448933-2a2aba80-d616-11eb-9c2c-348cd6b21183.png" width="400" height="400">


<br>

## 구성도
<img src="https://user-images.githubusercontent.com/69130921/123305775-75cc5e00-d55b-11eb-9ca8-c3014bc18a92.png">

<br>

## sample 조회 API
### 식물 조회 요청
```json
GET /plants/2 HTTP/1.1
Content-Type: application/json;charset=UTF-8
Accept: application/json
Host: localhost:8080
```

<br>

### 식물 조회 응답 (데이터 처리 후 응답)
```json
HTTP/1.1 200 OK
Content-Type: application/json
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Length: 1024

{"id":2,"userId":1,"frontName":"푸릇한","backName":"새싹이","plantLevel":1,"plantStatus":"IN_PROGRESS","repoName":"BeomjunLee/TableOrder","repoDesc":"주문 시스템입니다","startDate":"2021-06-24T22:00:00.000545915","deadLine":"2021-07-23T18:00:00","decimalDay":29,"commitCycle":3,"commitCount":9,"totalCommit":5,"commit":[{"date":"2021-06-01T12:00:00","committer":"아이디1","message":"메시지1"},{"date":"2021-06-02T12:00:00","committer":"아이디2","message":"메시지2"},{"date":"2021-06-03T12:00:00","committer":"아이디3","message":"메시지3"},{"date":"2021-06-04T12:00:00","committer":"아이디4","message":"메시지4"},{"date":"2021-06-05T12:00:00","committer":"아이디5","message":"메시지5"}],"comLang":[{"language":"Kotlin","percentage":35.1,"usedLine":36741},{"language":"JavaScript","percentage":33.2,"usedLine":34811},{"language":"CSS","percentage":15.6,"usedLine":16307},{"language":"HTML","percentage":14.5,"usedLine":15227},{"language":"others","percentage":1.5,"usedLine":1649}]}
```

<br>

### 회원 조회 요청
```json
GET /users/1 HTTP/1.1
Content-Type: application/json;charset=UTF-8
Accept: application/json
Host: localhost:8080
```

<br>

### 회원 조회 응답 (데이터 처리 후 응답)
```json
HTTP/1.1 200 OK
Content-Type: application/json
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Length: 298

{"id":1,"username":"BeomjunLee","name":"이범준","picture":"https://avatars.githubusercontent.com/u/69130921?v=4","userJob":"서버 개발자","userDesc":"서버 개발을 좋아합니다","userTier":"GOLD","following":30,"follower":20,"plantAchieve":{"all":4,"inProgress":2,"done":1,"failed":1}}
```