package org.kosa.myproject.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.kosa.myproject.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}














