# post 경로 수정
포스트 게시 후 서버를 껐다가 켜야, 이미지가 출력되는 문제가 발생했다.<br>
문제의 원인은 이미지 저장 경로가 내부 ```/src/resources/static/uploads```로 설정된 것이었다.<br>
내부 디렉토리의 경우, spring boot 애플리케이션 빌드할 때에 패키지에 포함되기 때문이다.<br>
그래서, 프로젝트 파일의 루트로 경로를 변경했고, 외부 경로 파일에 접근하기 위한 **addResourceHandlers** 메서드를 구성했다.
```java
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // /uploads/** 경로를 루트 경로의 /uploads 폴더와 매핑
    registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/");
}
```
