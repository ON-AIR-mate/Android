# OnAirMate Android
## Info

Android Studio : Koala

TargetSDK : 35

minSDK : 26

&nbsp; &nbsp; 
## Library  
- android-youtube-player : 유튜브 영상 재생 
- io.socket:socket.io-client : 실시간 채팅
- com.google.dagger:hilt-android : 의존성 주입
- com.squareup.retrofit2:retrofit : 네트워크 연결
- com.squareup.retrofit2:converter-gson : 데이터 파싱
- com.google.android.flexbox : 유연한 UI 구성 
&nbsp; &nbsp; 

## branch 전략 (**github-flow)**

```
├─main
    ├─feature/화면이름-기능이름   
```
  
- 모든 브랜치는 main에서 만든다
- 브랜치 이름은 자세하게 어떤 일을 하고 있는지에 대해서 작성
- merge 준비가 완료되었을 때는 pull request를 생성한다
- main은 어떤 때든 배포가 가능하도록 빌드가 가능함을 확인하고 merge

&nbsp; &nbsp; 
## Commit 컨벤션

**“타입: 작업내용” 의  형식으로 올려주세요**

ex) feat: 채팅 기능 구현 

**🏷️타입**

- **feat** : 새로운 기능 구현
- **fix** : 버그 및 오류 수정
- **style**: 스타일, 포멧 등에 관한 커밋
- **chore** : 자잘한 수정
- **delete** : 쓸모없는 코드나 파일 삭제
- **docs** : 문서 수정
- **refactor** : 리팩토링
- **test**: 테스트 코드 수정

&nbsp; &nbsp; 
## Code 컨벤션

- 클래스명 : 명사 & Pascal Case
- 함수명 : 동사 & camel Case
- 변수명 : 명사 & camel Case
- 상수명 :  UPPER_SNAKE_CASE (모두 대문자)
- 패키지명 : 명사 & 소문자 & '_'로 단어 연결
- id명 : camel Case
    - <iv_>
    - <tv_>
    - <rv_>
- name(style, string 등) : snake Case
- drawable 파일 : 키워드를 제일 앞으로ex) ic_name, ic_back
- textSize 단위는 sp
- 리사이클러뷰 아이템 레이아웃 이름 rv_item_{이름} 로 시작
- 리사이클러뷰 어댑터 클래스명 {이름}Adapter 로 사용
- 로그 남길 때
    - 태그는 클래스명 : private val TAG = this.javaClass.simpleName

- 나머지는 안드로이드 공식문서에 있는 코틀린 스타일로
    
    https://developer.android.com/kotlin/style-guide?hl=ko
    

##
