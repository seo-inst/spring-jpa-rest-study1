package org.kosa.myproject.coreconcept.step1;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import org.kosa.myproject.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
/*
        EntityManager : JPA (Java Persistence API) 의 핵심 객체
        엔티티 객체를 관리하고 데이터베이스와 상호 작용하는 역할을 함
        구체적으로 말하면 데이터베이스와 애플리켕션 사이의  객체와 테이블 간의 매핑을 관리
        EntityManager 는 영속성 컨텍스트 ( Persistence Context) 를 통해 객체의 생명 주기를 관리하여
        데이터 베이스의 작업을 수행하는 역할을 한다

        영속성 컨텍스트 ( Persistence Context)는 엔티티를 관리하는 환경
        임시 저장되는 메모리 공간과 같음
        - 1차 캐시
        - 변경 감지 Dirty Checking
        - 쓰기 지연
 */
@Component // JPA EntityManager 공부를 위한 클래스, 테스트 후에 주석처리
@Slf4j
public class EntityManagerStudy implements CommandLineRunner{
    @Autowired
    EntityManagerFactory entityManagerFactory;
    @Override
    public void run(String... args) throws Exception {
            log.info("***EntityManager(Persistence Context) Study**");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            transaction.begin();
            // 저장  persist
            // 1. 새로운 엔티티 생성 ( 비영속 상태 )
            Car car = Car.builder().modelName("K5").price(300L).build();
            log.info("1. 비영속 상태: {} {}",car.getCarId(),car.getModelName());

            //2. 영속성 컨텍스트에 저장 ( 영속 상태 )
            /*
                    쓰기 지연 : SQL 즉시 실행시키지 않고 모아  뒀다가 flush() 시점에 일괄 실행
                    성능 최적화와 트랜잭션 관리에 유리 ->  장바구니에 담아뒀다가 한번에 주문하는 것과 같음
             */
            entityManager.persist(car);
            log.info("2. 영속 상태 ");
            // 3. 같은 아이디로 조회 - 1차 캐시에서 반환
            // 영속성 컨텍스트 내의 1차 캐시 공간에서 반환
            Car foundCar = entityManager.find(Car.class,car.getCarId());
           log.info("3. 동일 객체 확인 :"+(car == foundCar ));
//            // 4. 변경 감지 ( Dirty Checking ) 테스트
//            // JPA 가 엔티티 변경 사항을 자동으로 감지하여 UPDATE 쿼리 생성
//            // 개발자가 별도로 update() 메서드를 호출할 필요 없음을 확인
            car.updateModelName("K8");
            log.info("변경 감지 dirty checking 확인");
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
        }finally {
            entityManager.close();
        }
    }
}








