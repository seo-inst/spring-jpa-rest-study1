package org.kosa.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 *  Car 엔티티 클래스
 *  JPA 엔티티 클래스 :  테이타베이스 테이블과 매핑되는 자바 객체 클래스
 *  객체 지향 프로그래밍과 관계형 데이터베이스를 연결하는 다리 역할
 */
@Entity // 이 클래스는 db 테이블과 매핑되는 엔티티임을 스프링 컨테이너너에가 알려줌
@Table(name = "cars") // 실제 데이터베이스 테이블 명을 지정 ( 생략시 클래스 명 소문자로 테이블 생성 )
@Getter // getter
@NoArgsConstructor  // 기본 생성자
@AllArgsConstructor // 매개변수 정의된 생성자
@Builder // 빌더 패턴
@ToString
public class Car {
    /*
            @Id :  이 필드가  테이블의 기본키임을 JPA에게 알려줌
             @GeneratedValue(strategy = GenerationType.IDENTITY) : DB의 auto_increment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Long carId;
    /*
        컬럼명과 제약조건을 기술
     */
    @Column(name="model_name", nullable = false, length = 100)
    private String modelName;

    @Column(nullable = false)
    private Long price;

    /*
            비즈니스 메서드 들
            엔티티는 도메인 로직을 포함 , 데이터와 데이터를 다루는 행위를 함께 관리
     */
    public void updateModelName(String newModelName){
        if(newModelName == null || newModelName.trim().isEmpty()){
            throw new IllegalArgumentException("모델명은 비어 있을 수 없습니다");
        }
        this.modelName = newModelName.trim();
    }
    public void updateCar(String newModelName, Long newPrice){
        // 검증 절차
        this.modelName = newModelName;
        this.price = newPrice;
    }

    /**
     *  가격 할인 적용 메서드
     * @param rate 할인율 (0.1 = 10% 할인율)
     */
    public void applyDiscount(double rate) {
        if(rate < 0 || rate > 1)
            throw new IllegalArgumentException("할인율은 0과 1 사이여야 합니다");
        this.price = Math.round(this.price * (1 - rate));
    }
}






































