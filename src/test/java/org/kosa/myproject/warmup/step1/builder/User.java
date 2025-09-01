package org.kosa.myproject.warmup.step1.builder;
/*
     Builder Design Pattern 을 직접 구현해본다 ( 이후에는 lombok 라이브러리로 간편하게 만듬 )
     Builder Pattern 이란  객체 생성 과정을 캡슐화하여 객체 생성과 표현을 분리하는 디자인 패턴
     복잡한 객체를 단계별로 구성할 수 있어 가독성과 생산성을 높이는 디자인 패턴
 */
public class User {
    private Long id;
    private String username;
    private String email;
    // 생성자에 private 명시하여 외부에서 직접 생성자를 호출하지 못하도록 막는다
    private User(){}
    //Builder 인스턴스를 반환하는 static method
    public static UserBuilder builder(){
        return new UserBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    /*
         User 객체를 빌드하기 위한 Builder 내부 클래스
         이 클래스가 실제 빌더 로직을 담당합니다.
     */
    public static class UserBuilder{
        private Long id;
        private String username;
        private String email;
        // private 생성자로 외부에서 생성자 호출 못하게 하고 오직 User.builder() 를 통해서만 접근 가능하게 함
        private UserBuilder(){}
        /*
                id 값을 설정하는 메서드
         */
        public UserBuilder id(Long id){
            this.id=id;
            return this;
        }
        public UserBuilder username(String username){
            this.username=username;
            return this;// 메서드 체이닝
        }
        public UserBuilder email(String email){
            this.email=email;
            return this;
        }
        public User build(){
            //  필수 필드 유효성 검증 ( 예: username )
            if(username == null || username.trim().isEmpty()){
                throw new IllegalStateException("username 은 필수입니다");
            }
            User user = new User();
            user.setId(this.id);
            user.setUsername(this.username);
            user.setEmail(this.email);
            return user;
        }

    }
}









