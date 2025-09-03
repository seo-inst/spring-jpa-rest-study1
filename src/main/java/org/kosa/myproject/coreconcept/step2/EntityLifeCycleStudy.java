package org.kosa.myproject.coreconcept.step2;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kosa.myproject.entity.Car;
import org.springframework.boot.CommandLineRunner;

/**
 Entity LifeCycle(엔티티 라이프사이클)

 JPA의 EntityManager는  **영속성 컨텍스트(Persistence Context)**를 통해
 엔티티 객체의 상태를 관리하고 데이터베이스와의 동기화를 자동으로 처리하는 데 있습니다.
 라이프사이클을 이해하면 persist(), find(), merge(), remove() 같은 메서드들이
 어떤 원리로 작동하는지 명확하게 알 수 있습니다.

 Entity LifeCycle(엔티티 라이프사이클) 의 중요성

 성능 최적화: 라이프사이클을 이해하면 불필요한 데이터베이스 접근을 막을 수 있습니다.
 예를 들어, 동일한 트랜잭션 내에서 같은 엔티티를 여러 번 조회해도
 첫 번째 조회 시에만 쿼리가 발생하고(1차 캐시),
 객체 수정 시 별도의 update() 메서드 호출 없이도
 자동으로 변경 내용을 반영하는 변경 감지(Dirty Checking) 기능도
 이 라이프사이클 개념에서 비롯됩니다.

 버그 방지: detach 상태의 객체를 영속 상태인 것처럼 오해하여 데이터를 수정하려다
 버그가 발생하는 경우를 막을 수 있습니다.
 객체의 상태를 정확히 파악해야 예상치 못한 동작을 방지할 수 있습니다.
 사례 ) @Transactional 어노테이션이 붙은 메서드가 종료되면,
 그 메서드 안에서 JPA가 관리하던 영속성 컨텍스트도 함께 종료됩니다.
 이 시점에 영속성 컨텍스트가 관리하던 모든 엔티티 객체는 자동으로 준영속 상태가 됩니다.
 예를 들면 UserController에서 user.setName("New Name"); 코드가 실행되지만,
 user는 이미 준영속 상태이므로 JPA의 변경 감지 기능이 동작하지 않아
 데이터베이스에 UPDATE 쿼리가 발생하지 않습니다.


 엔티티 라이프 사이클 주요 상태

 **비영속(New) 상태**
 - 단순히 new 키워드로 생성한 상태
 - JPA가 전혀 관리하지 않는 순수 Java 객체


 **영속(Managed) 상태**
 - persist(), find(), merge() 등으로 영속성 컨텍스트에 관리되는 상태
 - 변경 감지, 1차 캐시, 쓰기 지연 등 모든 JPA 기능 활용 가능


 **준영속(Detached) 상태**
 - 영속 상태였다가 영속성 컨텍스트에서 분리된 상태
 - ID값은 있지만 JPA가 관리하지 않음


 **삭제(Removed) 상태**
 - 삭제하기로 예정된 상태
 - 실제 DELETE는 flush 시점에 실행
 */
//@Component // 엔티티 라이프 사이클 공부를 위한 클래스, 테스트 후에는 주석처리
@RequiredArgsConstructor // lombok : final 필드에서 대한 생성자를 자동으로 만들어줌
@Slf4j
public class EntityLifeCycleStudy  implements CommandLineRunner {
    private final EntityManagerFactory entityManagerFactory;
    // lombok @RequiredArgsConstructor으로 아래 생성자를 간단하게 표현
//    public EntityLifeCycleStudy(EntityManagerFactory entityManagerFactory){
//        this.entityManagerFactory=entityManagerFactory;
//    }
    @Override
    public void run(String... args) throws Exception {
        log.info("***JPA Entity LifeCycle Study***");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try{
                transaction.begin();
                Car car = Car.builder().modelName("SM5").price(500L).build();
                log.info("1. 비영속 상태 - JPA 가 관리하지 않음");
                // 영속 상태(managed)로 전환
                entityManager.persist(car);
                //  EntityManager 가 관리하는 Persistence Context 에 Entity 저장되고 관리
                log.info("2. 영속 상태 - JPA 가 관리 시작... 변경 감지 dirty checking 활성화, 1차 캐시 저장됨");
               Car foundCar =  entityManager.find(Car.class,car.getCarId());
                log.info("동일한 객체인가? 동일하면 1차 캐쉬에서 반환된 것임 {}",(car == foundCar));
                // 3. 준영속 (Detached) 상태로 전환 ->  @Transactional 서비스 메서드가 종료되면 준영속 상태로 됨
            //  EntityManager 의 Persistence Context 도 함께 종료
                entityManager.detach(car);// 준영속 상태로 전환
            // 객체 정보를 업데이트 -> 준영속 상태이므로 DB 반영 안됨 : 변경 감지(Dirty Checking 안됨 )
                car.updateModelName("SM1");
                log.info("3. 준영속 상태에서 car 객체 업데이트 했으므로 db에 반영안됨");
                Car foundCar2 = entityManager.find(Car.class,car.getCarId());// 준영속 상태에서 find 조회를 한다
                // false 가 나옴 :  준영속 상태이므로 1차 캐시에서 가지고 오는 것이 아니라 db 에서 직접 select 를 하므로
                log.info("준영속 상태에서 조회 후 car 객체와 비교 {}",(car == foundCar2));
                // 4.  다시 영속 상태로 전환
                Car  managedCar =  entityManager.merge(car);
                // managedCar 엔티티 객체의 모델명을 업데이트
            // merge 를 통해 다시 영속 상태로 되었으므로 변경 감지 Dirty Checking 이 되어
            // Entity 객체 정보 업데이트 되면 db 에 반영됨
               managedCar.updateModelName("SM7");//변경감지, 쓰기 지연
               //5. 삭제 Removed 상태
               entityManager.remove(managedCar);//변경감지 , 쓰기 지연
                transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            transaction.rollback();
        } finally {
            entityManager.close();
        }
    }
}








