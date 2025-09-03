package org.kosa.myproject.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.kosa.myproject.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@Transactional  // 테스트 시에 @Transactional 적용하면 테스트 후 자동 롤백
@Slf4j
public class CarRepositoryTest {
    @Autowired
    CarRepository carRepository;
    @Test
    void saveTest(){
        //Given
        Car car = Car.builder().modelName("코나").price(28000000L).build();
        //When
        Car savedCar = carRepository.save(car);
        //Then
        // AssertJ
        assertThat(savedCar.getCarId()).isNotNull();
    }
    @Test
    void saveAllAndFindAllTest(){
        Car car1 = Car.builder().modelName("아반떼").price(25000000L).build();
        Car car2 = Car.builder().modelName("소나타").price(35000000L).build();
        Car car3 = Car.builder().modelName("그랜저").price(45000000L).build();
        Car car4= Car.builder().modelName("제네시스").price(65000000L).build();
        //carRepository.saveAll(List.of(car1,car2,car3,car4));
        // JUnit 테스트 구조와 실행을 담당하는  TDD 프레임워크
        // AssertJ 테스트 결과 검증하는 컴포넌트
        List<Car> carList = carRepository.findAll(); // db 에서 전체 car list를 select
        assertThat(carList).hasSize(5);
    }
    @Test
    void deleteCarTest(){
        // Given - 삭제할 자동차 찾기
        List<Car> cars = carRepository.findAll();
        Car carToDelete =  cars.get(0);
        Long carId = carToDelete.getCarId();
        // When - 삭제
        carRepository.deleteById(carId);
        // Then  - 확인
        Optional<Car>  deleteCar = carRepository.findById(carId);
        assertThat(deleteCar).isEmpty();
    }
    @Test
    void updateCarTest(){
        //Given
        List<Car> cars = carRepository.findAll();
        Car car = cars.get(0);
        // 엔티티 객체의 자동차 모델명을 재할당
        car.updateModelName("테슬라");
        // save 메서드 1  식별자(ID , PK ) 가 없는 경우 :  persist -> sql insert
        //                     2  식별자(ID, PK) 가 있는 경우 :  merge -> sql update
        carRepository.save(car);// insert or update
        // then
        Optional<Car> updatedCar = carRepository.findById(car.getCarId());
        log.info("db update 확인:{}",updatedCar.get());
        assertThat(updatedCar.get().getModelName()).isEqualTo("테슬라");
    }
    @Test
    void findByModelNameTest(){
        //given
        // 위 테스트에서 데이터를 저장해서 생략
        //when - 소나타 모델 자동차 정보 조회
        List<Car> sonatas = carRepository.findByModelName("소나타");
        assertThat(sonatas).hasSize(1);
        log.info("sonatas {}",sonatas);
    }
    @Test
    void findByPriceBetweenTest(){
        Long lowPrice = 30000000L;
        Long highPrice = 50000000L;
        List<Car> cars = carRepository.findByPriceBetween(lowPrice,highPrice);
        assertThat(cars).hasSize(2);
        cars.forEach(car -> log.info(car.toString()));
    }
}

















