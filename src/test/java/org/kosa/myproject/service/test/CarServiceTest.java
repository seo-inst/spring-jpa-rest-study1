package org.kosa.myproject.service.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.kosa.myproject.entity.Car;
import org.kosa.myproject.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional // junit test 환경에서는 테스트 후 자동 롤백
@Slf4j // lombok  로깅 선언
public class CarServiceTest {
    @Autowired
    CarService carService;
    @Test
    void findAllCarsTest(){
        List<Car> list =carService.findAllCars();
        assertThat(list).hasSize(4);
    }
    @Test
    void findCarByIdTest(){
        // Given
        long carId = 2L;
        // When
        Car car = carService.findCarById(carId);
        // Then
        assertThat(car.getModelName()).isEqualTo("테슬라");
    }
    @Test
    void createCarTest(){ // 실제 insert 되지만 JUnit Test 에서 Class level 에서 @Transactional 을 명시해 자동 롤백
        // Given
        String modelName = "K5";
        Long price = 36000000L;
        //When
        Car newCar = carService.createCar(modelName,price);
        //Then
        log.info("등록 차량 {}",newCar.toString());
        assertThat(newCar.getModelName()).isEqualTo(modelName);
    }
    @Test
    void deleteCarTest(){
        // Given
        Long carId = 2L; // 테슬라 자동차 아이디
        // When
        carService.deleteCar(carId);
        // Then : 삭제 되었으므로  Exception 이 발생되면 테스트 검증 통과
        assertThatThrownBy(() -> carService.findCarById(carId)).isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    void updateCarTest(){
        //Given
        Long carId = 2L;
        String modelName = "프리우스";
        Long price = 3000L;
        //When
        Car updatedCar = carService.updateCar(carId,modelName,price);
        //Then
        assertThat(updatedCar.getModelName()).isEqualTo("프리우스");
    }
}







