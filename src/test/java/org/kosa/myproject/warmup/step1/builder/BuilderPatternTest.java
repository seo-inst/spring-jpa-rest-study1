package org.kosa.myproject.warmup.step1.builder;

public class BuilderPatternTest {
    public static void main(String[] args) {
        User user1 = User.builder().id(1L).username("조규성").email("cho@gmail.com").build();
        System.out.println(user1);
        User user2 = User.builder()
                .username("손흥민")
                .email("son@gmail.com")
                .id(2L)
                .build();
        System.out.println(user2);
    }
}
