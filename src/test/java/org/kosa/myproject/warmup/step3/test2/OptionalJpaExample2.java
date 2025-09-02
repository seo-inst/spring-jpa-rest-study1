package org.kosa.myproject.warmup.step3.test2;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
@Slf4j
public class OptionalJpaExample2 {
    // 실제 JPA Repository 메서드 역할
    private static Optional<Member> findMemberByUsername(String username){
        if("admin".equals(username)){
            return Optional.of(new Member(2L,"admin"));
        }
        return Optional.empty();
    }

    public static void main(String[] args) {
        // ifPresent() : 값이 존재할 때만 특정 동작을 수행
//        Optional<Member> duplicateMember = findMemberByUsername("admin");
//        duplicateMember.ifPresent(existMember -> {
//            throw new IllegalArgumentException("이미 존재하는 사용자명입니다 "+existMember.getName());
//        });
        // ifPresentOrElse()   값이 있을 때와 없을 때를 동시에 처리
        Optional<Member> testMember = findMemberByUsername("admin2");
        testMember.ifPresentOrElse(member2 -> {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다"+member2.getName());
        }, ()->{
            log.info("사용할 수 있는 사용자명입니다");
        });
    }
}
