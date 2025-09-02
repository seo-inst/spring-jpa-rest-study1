package org.kosa.myproject.warmup.step3.test2;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

// JPA 에서 자주 사용하는 Optional Pattern
@Slf4j
public class OptionalJpaExample1 {
    // JPA Repository 메서드를 대행
    private static Optional<Member> findMemberById(Long id){
        if(id == 1L){
            return Optional.of(new Member(1L,"조규성"));
        }
        return Optional.empty();
    }

    public static void main(String[] args) {
        Long memberId = 1L;
        // Optional  orElseThrow : 존재하면 회원 객체를 반환하고 존재하지 않으면 Exception throw
       Optional<Member> memberOptional =  findMemberById(memberId);
       Member member = memberOptional.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 아이디 "+memberId));
       log.info("찾은 회원: {}",member.getName());
    }
}
