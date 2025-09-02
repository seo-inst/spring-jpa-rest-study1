package org.kosa.myproject.warmup.step3.test3;

import lombok.extern.slf4j.Slf4j;
import org.kosa.myproject.warmup.step3.test2.Member;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class StreamBasicExample {
    public static void main(String[] args) {
        List<Member> members = List.of(
                new Member(1L,"조규성"),
                new Member(2L,"황희찬"),
                new Member(3L,"손흥민")
        );
        // 기존 회원 리스트를 스트림으로 만들어 요소별  회원명만 추출하여 새로운 불변성 리스트를 만듭니다
        List<String> memberNames = members.stream().map(member -> member.getName()).collect(Collectors.toUnmodifiableList());
        // 람다식 방식으로 출력해봅니다
        memberNames.forEach(name -> log.info("회원명 {}",name));
        log.info("=====람다 메서드 레퍼런스 활용해 코드를 축약=======");
        List<String> memberNames2 = members.stream().map(Member::getName).collect(Collectors.toUnmodifiableList());
        memberNames2.forEach(name -> log.info("회원명 2 {}",name));
    }
}

