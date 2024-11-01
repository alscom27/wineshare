### 목차

> 1. [프로젝트 소개](#프로젝트-소개)
> 2. [IA 정보구조도](#IA-정보구조도)
> 3. [ERD 엔티티 관계도](#ERD-엔티티-관계도)
> 4. [기술 스택](#기술-스택)
> 5. [주요 기능](#주요-기능)
> 6. [주요 화면](#주요-화면)
> 7. [API 명세](#API-명세)
> 8. [향후 개선 사항](#향후-개선-사항)

# 프로젝트 소개

> <img src="./wineshareReadmeImages/wineshare_logo2.png" alt="wineshare 로고" width="300"/></br>
> ### 와인을 공유하다, WINESHARE 와인 정보 제공 및 커뮤니티 플랫폼
>
> 전문가(소믈리에)들이 작성해주는 와인 정보와 공공 API를 연동하여 다양한 와인 정보를 수집, 가공하여 사용자 맞춤형 조회 서비스를 제공합니다.  
> 
> 사용자는 원하는 와인 정보를 검색, 필터링하고, 관심있는 와인을 셀러로 등록하여 해당 와인들만 정리해서 볼 수 있습니다.   
> 
> 등업 게시판을 통해 일반 사용자에서 전문가로 권한변경을 요청하고, 변경 할 수 있으며, 요청 게시판을 통해 사이트에는 없는 와인에 대한 정보를 요청할 수 있고, 다양한 질문이 가능한 질문 게시판이 있습니다.</br>
> 위 게시판들을 통해 사용자 간 의견을 공유하고, 와인에 대한 다양한 관점을 나눌 수 있는 플랫폼을 구축하고 이로써 사용자 친화적이고 기능적으로도 완성도 높은 와인 정보 서비스를 제공합니다.
> 
> 전문가들의 매장을 등록하고 프로모션을 등록할 수 있으며, 홍보를 해줌으로써 전문가들의 정보 제공에 동기를 부여했습니다.
> 
> 사용자가 와인을 친근하게 접근할 수 있도록 이벤트를 통해 와인 시음회나 행사들을 볼 수 있습니다.
> 
> SpringBoot 프레임워크를 사용하여 MVC 패턴의 기본적인 CRUD를 구현하고
> AJAX를 통해 REST API를 설계하여 사용자 중심의 HTTP 비동기 처리를 구현하였습니다.
>
> **REFERENCE** [vivino](https://www.vivino.com/?srsltid=AfmBOop0pTFXpypezAvVIzxOK1vsAX_5B4i0vicOy4SdaIEqw35XJ6xR)</br>
> **REFERENCE** [와인나라](https://www.winenara.com/)

> ### 개발 기간
> 2024.10.01 ~ 2024.10.28

> ### 개발 인원(4명)
> **이종진(FULLSTACK)**   
> 기획, 설계, 개발환경 구축, GIT 관리   
> 로그인, 회원가입, 사이트 소개, 공지·등업·질문·요청 게시판, 플레이스, 이벤트, 사용자 마이페이지(회원정보관리), 관리자 대시보드 구현
>
> **오은우(FULLSTACK)**   
> 기획, 설계, API 연동, GIT 관리   
> 메인, 와인 목록, 와인 상세, 사용자 마이페이지(셀러), 관리자 대시보드 구현
>
> **최수림(FRONTEND)**   
> ppt, 모든 뷰에 대한 마크업, css
> 
> **오재환(FRONTEND)**</br>
> 자료조사

# IA 정보구조도
<img src="src/main/resources/static/images/IA.png" width="100%" height="400px" alt="IA"></img><br/>

# ERD 엔티티 관계도
<img src="src/main/resources/static/images/ERD.png" width="100%" height="500px" alt="ERD"></img><br/>

# 기술 스택
<img src="src/main/resources/static/images/back.png" width="100%" height="400px" alt="back"></img><br/>

# 주요 기능
### 요약
|공통|사용자|관리자|
|------|---|---|
|-회원가입, 로그인<br>-공연목록 및 공연상세 조회<br>-게시글 조회|-즐겨찾기 추가<br>-공연후기 작성<br>-예매 결제<br>-게시글, 댓글 작성<br>-마이페이지|-공연 API 관리<br>-회원 관리<br>-게시글 관리|

## [공통 기능]
### 회원가입, 로그인
* 아이디, 비밀번호 유효성 검증과 비밀번호 암호화를 설정했다.
* 카카오 로그인 시 자동 회원가입 가능하다.
* 일반로그인과 카카오 로그인 회원의 권한이 분리되어 있다.
### 메인
* 금주의 공연, 장르별 공연목록 조회가 가능하다.
* 기본으로 8개가 보여지며 더보기를 누르면 8개씩 추가로 조회할 수 있다.
### 공연 목록
* 장르, 지역, 공연상태에 따른 조회가 가능하다.
* 공연이름이나 공연시설이름으로 검색 조회가 가능하다.
* 날짜순으로 정렬이 가능하며 한 페이지에 9개의 공연 목록이 나타난다.
### 공연 상세
* 공연의 세부정보와 즐겨찾기 갯수, 남은 티켓 갯수를 조회할 수 있다.
* 카카오 지도로 공연장 위치조회와 길찾기 서비스를 사용할 수 있다.
* 공연 후기와 별점 조회가 가능하다.
### 게시판
* 게시글과 댓글을 날짜순, 제목이나 작성자로 검색 조회가 가능하다.

## [사용자 기능]
### 츨겨찾기 관리
* 공연상세에서 즐겨찾기 추가, 취소가 가능하다.
* 즐겨찾기된 공연은 마이페이지에 추가되며 추가된 공연들의 일정을 달력으로 확인할 수 있다.
### 공연 후기 관리
* 공연상세에서 공연에 대한 별점과 후기를 남길 수 있다.
* 작성자와 로그인된 회원이 같다면 후기 수정, 삭제가 가능하다.
* 마이페이지에서도 조회, 수정, 삭제가 가능하다.
### 예매 결제 관리
* 공연상세에서 잔여티켓 내에서 최대 4장까지 예매 가능하다.
* 예매가 완료된 공연은 마이페이지에 추가되며 예매 취소가 가능하다.
### 게시글, 댓글 관리
* 게시판에 게시글과 게시글에 대한 댓글을 작성할 수 있다.
* 작성자와 로그인된 회원이 같다면 게시글 수정, 삭제가 가능하다.
* 마이페이지에서도 조회, 수정, 삭제가 가능하다.

## [관리자 기능]
### API 관리
* 1주 단위로 API 업데이트와 삭제를 할 수 있다.
* 모든 공연의 관리, 삭제가 가능하다.
### 회원 관리
* 모든 회원의 정보를 아이디나 이름으로 검색 조회가 가능하며 수정, 삭제도 가능하다.
### 게시글 관리
* 모든 게시글을 기간이나 제목, 작성자로 검색 조회가 가능하며 수정, 삭제도 가능하다.

# 주요 화면
|메인|회원가입|
|------|---|
|<img src="src/main/resources/static/images/main.png" width="400px" height="250px" alt="main"></img>|<img src="src/main/resources/static/images/sign.png" width="400px" height="250px" alt="sign"></img>|
|공연목록|공연상세|
|<img src="src/main/resources/static/images/showlist.png" width="400px" height="250px" alt="showlist"></img>|<img src="src/main/resources/static/images/showdetail.png" width="400px" height="250px" alt="showdetail"></img>|
|게시판|게시글|
|<img src="src/main/resources/static/images/board.png" width="400px" height="250px" alt="board"></img>|<img src="src/main/resources/static/images/repliy.png" width="400px" height="250px" alt="repliy"></img>|
|마이페이지|관리자페이지|
|<img src="src/main/resources/static/images/mypage.png" width="400px" height="250px" alt="mypage"></img>|<img src="src/main/resources/static/images/adminpage.png" width="400px" height="250px" alt="adminpage"></img>|

# API 명세
|Domain|URL|Method|Description|Role|
|------|---|---|---|---|
|Member|/admin/managementAPI|GET|관리자페이지 API 조회|Admin|
||/admin/managementAPI/update|GET|관리자페이지 API 수정 |Admin|
||/admin/managementAPI/delete|POST|관리자페이지 API 삭제|Admin|
||/admin/managementAPI/detail<br>/managementAPI/detail/{page}|GET|관리자페이지 공연 조회|Admin|
||/admin/managementAPI/deleteShow/{id}|DELETE|관리자페이지 공연 삭제|Admin|
||/admin/managementMember/list<br>/admin/managementMember/list/{page}|GET|관리자페이지 회원 조회|Admin|
||/admin/managementMember/delete|GET|관리자페이지 회원 삭제|Admin|
||/admin/managementMember/editMember|POST|관리자페이지 회원정보 수정|Admin|
||/admin/managementBoard/list<br>/admin/managementBoard/list/{page}|GET|관리자페이지 게시글 조회|Admin|
||/admin/managementBoard/delete|GET|관리자페이지 게시글 삭제|Admin|
||/members/agree|GET|약관동의||
||/members/new|GET|회원가입||
||/members/login|GET|로그인||
||/members/delete|GET|로그아웃|User|
||/members/info|GET|마이페이지 조회|User|
||/members/updateInfo|GET|마이페이지 프로필 이미지 수정|User|
||/members/update|POST|마이페이지 회원 정보 수정|User|
||/members/myBoards<br>/members/myBoards/{page}|GET|마이페이지 내가 쓴글 조회|User|
||/favorite/event|GET|마이페이지 즐겨찾기 달력 요청|User|
||/order/event|GET|마이페이지 예매내역 달력 요청|User|
|Order|/order|POST|공연상세 공연예매|User|
||/orders<br>/orders/{page}|GET|마이페이지 예매 내역 조회|User|
||/order/{orderNum}/cancel|POST|마이페이지 예매 취소|User|
|Favorite|/favorites<br>/favorites/{page}|GET|마이페이지 즐겨찾기 조회|User|
||/favorite|POST|공연상세 즐겨찾기 추가|User|
||/favorite/{mt20id}/cancel|POST|공연상세 즐겨찾기 취소|User|
|Review|/show/{mt20id}/review|POST|공연상세 공연후기 추가|User|
||/show/{mt20id}/review/{num}|POST<br>DELETE|공연상세 공연후기 수정, 삭제|User|
||//reviews<br>/reviews/{page}|GET|마이페이지 공연후기 조회|User|
|Show|/|GET|메인페이지||
||/loadMoreShows|GET|메인페이지 더보기 조회||
||/genreFilter|GET|메인페이지 장르별 조회||
||/shows<br>/shows/{page}|GET|공면목록 조회||
||/show/{mt20id}|GET|공면상세 조회||
|Board|/board<br>/board/{page}|GET|게시판 게시글 조회||
||/board/write|GET|게시판 게시글 작성 폼|User|
||/board/write|POST|게시판 게시글 작성|User|
||/board/showModify/{boardId}|POST|게시판 게시글 수정 폼|User|
||/board/modify/{boardId}|POST|게시판 게시글 수정|User|
||/board/delete/{boardId}|POST|게시판 게시글 삭제|User|
||/board/boardDtl/{boardId}|GET|게시판 게시글 상세조회||
|Reply|/addReply/{id}/reply|POST|게시판 댓글 추가|User|
||/board/{id}/reply/{replyId}/update|POST|게시판 댓글 수정|User|
||/board/{id}/reply/{replyId}/remove|POST|게시판 댓글 삭제|User|

# 향후 개선 사항
> * 회원가입 시 아이디, 이메일 중복 체크 및 휴대전화 인증 절차 구현
> * 회원가입 시 주소찾기 API 를 사용하여 우편번호와 상세 주소 구현
> * 휴대전화 인증으로 아이디, 비밀번호 찾기 서비스 구현
> * AJAX 비동기 통신으로 즐겨찾기 기능 수정
> * 공연 예매 시 시간, 날짜, 좌석 선택과 환불 정책에 따른 환불서비스 구현
> * 사용자들이 실시간 소통을 할 수 있는 커뮤니티 기능 구현현

