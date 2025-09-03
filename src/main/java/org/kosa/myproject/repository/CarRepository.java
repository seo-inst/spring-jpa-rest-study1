package org.kosa.myproject.repository;

import org.kosa.myproject.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 영속성 계층 컴포넌트 애너테이션
public interface CarRepository extends JpaRepository<Car,Long> { // Car : Entity 타입, Long : 기본키 타입
        // JpaRepository 로부터 상속받는 메서드를 테스트
        /*
                자동 제공 주요 메서드
                save(Car car) : 저장 또는 수정
                findById(Long id) : ID 로 조회
                findAll() : 모든 데이터 조회
                deleteById() : ID로 삭제
                count() : 전체 개수 조회
                등의 다양한 메서드가 자동 지원됨
         */
    /*
            쿼리 메서드 네이밍 규칙:
            findBy+필드명 : 해당 필드로 조회
            SELECT * FROM cars WHERE model_name=?
     */
    List<Car> findByModelName(String modelName);

    List<Car> findByPriceBetween(Long lowPrice, Long highPrice);
}






