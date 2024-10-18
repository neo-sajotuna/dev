# NewNeo-BoardBuddy


<br>

# 👀 welcome to NewNeo-BoardBuddy👀

### [ Trello API 구현 프로젝트 ]

<br><br>

## 👨‍👩‍👧‍👦 Our Team

|최선|박현국|김건우|오현택|
|:---:|:---:|:---:|:---:|
|[@Seon](https://github.com/sunchoiii)|[@HyunKook](https://hyunkook.tistory.com/)|[@KeunWoo](https://choni.tistory.com)|[@HyunTaek](https://oht2050.tistory.com/)|
|WorkSpace, Board, CI/CD|Card, Concurrency Control|List, Alram, Indexing|Comment, AWS S3, Redis|

<br>

### [👊 프로젝트 노션 바로가기](https://teamsparta.notion.site/0d265337d61a4ddc81b3333a31f4f122)

<br><br>

## 프로젝트 기능

### 🛡 Trello API 구현을 통해 WorkSpace, Board, List, Card 를 사용자들이 편리하게 사용할 수 있도록 구현하였습니다.

> * User: Trello 애플리케이션에 회원 가입한 모두, Member: 특정 워크스페이스에 초대된 유저
> * WorkSpace 는 ADMIN 권한을 가진 유저만 생성할 수 있습니다.
> * Board 는 WorkSpace 내에서 새로운 Board 를 생성할 수 있습니다.
> * List 는 Board 내에서 생성하고 수정할 수 있습니다.
> * Card 는 List 내에서 Card 를 생성하고 수정할 수 있습니다.

 <br>


### 📊 첨부파일 기능 (AWS S3)
 
> * 카드에 파일을 첨부할 수 있는 기능을 추가하여, 파일을 조회 또는 삭제가 가능하게 합니다.
> * FILE 테이블을 생성하여, Target 에 대한 DB Column 을 두어 특정 테이블의 기본키로 이미지 경로를 조회할 수 있습니다. 
> * 특정 Table 에서 파일을 저장 시 table 이름으로 Bucket 에 폴더를 생성 후 Image 를 저장하여 DB 에 업데이트 해줍니다.

<details>
<summary> S3 미리보기</summary>
<div markdown="1">

![데이터 보여주기](https://imgproxy.gamma.app/resize/quality:80/resizing_type:fit/width:1200/https://cdn.gamma.app/a01hohwam5u4kqu/6327b9e1627341efba3a3bc1f548a607/original/111.png)

 <br>
</div>
</details>

### 🗨 동시성 제어 (Concurrency Control)
 
> * 하나의 카드에 많은 사람들이 수정할 일은 드물다고 판단.
> * 낙관적 락을 통해 불필요한 락 방지
> * 테스트 코드를 통해 1000 건의 요청을 받아 동시성 제어
> * 낙관적 락을 사용했을때와 미사용 했을때의 차이 비교

<details>
<summary>성능 비교 보기</summary>
<div markdown="1">

![글쓰기1](https://cdn.gamma.app/a01hohwam5u4kqu/17b3331b16214399b8c54c84b1e26e0d/original/image.png)

 <br>
</div>
</details>

### 🔍 캐싱 (Redis)

> * 조회수를 캐시에 저장하여 응답 속도를 높이는 방법으로 분산 캐쉬 (Redis) 를 사용한다.
> * 사용자의 고유값을 저장하고 조회 후 어뷰징 방지한다.
> * 가장 인기 있는 카드를 조회할 수 있도록 데이터 타입을 ZSetOperations 로 설정하여 정렬 후 캐시에 저장

<details>
<summary> 캐싱 미리보기</summary>
<div markdown="1">

![캐싱](https://imgproxy.gamma.app/resize/quality:80/resizing_type:fit/width:1200/https://cdn.gamma.app/a01hohwam5u4kqu/00ebcd6b4a3e4fe58e894719e25d75b4/original/222.png)

 <br>
</div>
</details>

### 👨‍💻 최적화 (Indexing)
 
> * 데이터가 대량으로 축적될 경우, 카드 검색 속도가 느려질 수 있습니다. 따라서 인덱싱을 통해 최적화 작업을 하였습니다.
> * 별도의 더미데이터 Class 를 생성하였고, DB 에 75만개의 Card를 생성하여 Card를 모두 조회하는 메서드를 각 5회씩 실행 후 소요 평균 시간 계산

<details>
<summary>미리보기</summary>
<div markdown="1">

![Index 사용시](https://cdn.gamma.app/a01hohwam5u4kqu/ca7d6e402b8c4fbdb387a0a3e82f2cfc/original/image.png)

 <br>
</div>
</details>

### 📢 배포 (CI/CD)
 
> * Continuous Integration(CI): 코드 변경 시마다 자동 빌드와 테스트를 수행합니다. 
> * Continuous Deployment/Delivery(CD): 테스트를 통과한 코드를 자동으로 배포하거나, 배포 준비 상태로 둡니다.

<details>
<summary> CD 자동 배포 미리보기</summary>
<div markdown="1">

![배포](https://cdn.gamma.app/a01hohwam5u4kqu/fe39fbceafe0413db4094d31db76c344/original/seukeurinsyas-2024-10-18-ojeon-8.24.30.png)

 <br>
</div>
</details>


## 적용 기술
### ◻ JPA

### ◻ QueryDSL

> 정렬, 검색어 등에 따른 동적 쿼리 작성을 위하여 QueryDSL 도입하여 활용했습니다.

### ◻ AWS S3

### ◻ 캐싱 Redis

### ◻ Github Actions & Code Deploy (CI/CD)

### ◻ 동시성 제어

### ◻ 최적화 Indexing


<br><br>

## 🚨 Trouble Shooting

#### 낙관적 락 테스트 과정 중 OptimisticLockingFailureException 캐치를 제대로 해주지 못하는 상황 발생 
> * @Trasactional 어노테이션이 테스트 코드에서 제외
> * Mock 대신 Autowired 사용
> * 올바른 ThreadPool 지정
> * Card 를 생성할때마다, 카운트 시켜주는 메서드를 반영 

#### List가 WorkSpace 에 있는지 확인할때, Workspace → Board → List를 거쳐 확인해야 한다는 문제가 발생(N+1) 
> * 원인 : OneToMany를 사용하는 Entity에 join fetch를 여러 번 사용할 경우 발생
> * List → … → Workspace까지 fetch join을 진행하는 쿼리 작성
[N+1 해결](https://cdn.gamma.app/a01hohwam5u4kqu/6f4b798ba5fe4aa5bc86f6fb200d37b3/original/image.png)

#### GitHub Action -> 계속 이어지는 Action CI 동작 에러
> * 원인 : 처음엔 테스트코드에서 에러가 났고, 그걸 해결하면 데이터베이스 연결 문제,  EC2 도커 연동 문제, 도커허브 로그인 문제, 도커 이미지 생성 문제 등 쭉 이어지는 에러 행진
> * 해결 : pull request를 해야 확인을 할 수 있기 때문에 수십번의 푸쉬를 했고, 에러를 하나씩 해결하면서 처음 구축할 때 놓쳤던 설정들을 하나씩 추가하니까 더 확실하고 안정적인 설계를 하게 됐습니다.
[CI 해결](https://cdn.gamma.app/a01hohwam5u4kqu/2013737b36ff44ac9de4354d5965bd60/original/seukeurinsyas-2024-10-18-ojeon-8.24.30.png)

<br>

<br>

## 📝 Technologies & Tools (BE) 📝

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/> <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white"/> <img src="https://img.shields.io/badge/JSONWebToken-000000?style=for-the-badge&logo=JSONWebTokens&logoColor=white"/>

<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"/>  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"/> <img src="https://img.shields.io/badge/LINUX-FCC624?style=for-the-badge&logo=linux&logoColor=black"/>  <img src="https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=Ubuntu&logoColor=white"/>

<img src="https://img.shields.io/badge/AmazonEC2-FF9900?style=for-the-badge&logo=AmazonEC2&logoColor=white"/> <img src="https://img.shields.io/badge/AmazonS3-569A31?style=for-the-badge&logo=AmazonS3&logoColor=white"/>  <img src="https://img.shields.io/badge/AmazonRDS-527FFF?style=for-the-badge&logo=AmazonRDS&logoColor=white"/> <img src="https://img.shields.io/badge/CODEDEPLOY-181717?style=for-the-badge"/>  

<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"/> <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"/> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"/>  <img src="https://img.shields.io/badge/GithubActions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"/>  

<img src="https://img.shields.io/badge/IntelliJIDEA-000000?style=for-the-badge&logo=IntelliJIDEA&logoColor=white"/>  <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"/>  <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white"/> <img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"/> <img src="https://img.shields.io/badge/JiraSoftware-0052CC?style=for-the-badge&logo=jirasoftware&logoColor=white"/>   <img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white"/>

<br><br><br><br>

<div align=center>

![DKDKDKDKDKDK](https://github.com/user-attachments/assets/b4a6ba71-9251-40ca-8017-02a069f4a734)


</div>
