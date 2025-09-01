package org.kosa.myproject.warmup.step2.lombok.test2;

public class UserWithBuilderLombokTest {
    public static void main(String[] args) {
        // lombok 으로 생성한 Builder Pattern 적용된 User 객체를 생성해본다
        UserWithBuilderLombok user = UserWithBuilderLombok.builder()
                .id(1L)
                .username("이강인")
                .email("lee@gmail.com")
                .build();
        System.out.println(user);
    }
}
