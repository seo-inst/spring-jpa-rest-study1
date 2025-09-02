package org.kosa.myproject.warmup.step3.test1;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
@Slf4j
public class OptionalBasicExample {
    public static void main(String[] args) {
        /*
                Optional class : NPE(NullPointerException) 을 방지
                코드 안전성과 가독성을 높임,  JpaRepository 에서 제공하는
                메서드 반환형으로 사용됨
         */
        Optional<String> presentValue = Optional.of("안녕 옵셔널");
        log.info("presentValue 존재여부:{}",presentValue.isPresent());
        Optional<String> emptyValue = Optional.empty();
        log.info("emptyValue 존재여부:{}",emptyValue.isPresent());
        //값을 반환받기
        log.info("값 반환 받기 {}",presentValue.get());
        //값을 반환받기
        String result1 = presentValue.orElse("기본값");
        log.info("값 반환 받기 2 {}",result1);
        String result2 = emptyValue.orElse("기본값");
        log.info("값 반환 받기 3 {}",result1);
    }
}







