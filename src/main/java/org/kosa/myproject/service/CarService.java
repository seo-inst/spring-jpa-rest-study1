package org.kosa.myproject.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kosa.myproject.entity.Car;
import org.kosa.myproject.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
        Service (Business 계층 ) :  비즈니스 로직을 처리하는 계층
                                                 Controller 와 Repository 의 중간 매개 역할
                                                 트랜잭션 관리 : 데이터 무결성과 일관성 보장 ( ACID )
                                                 복잡한 업무 규칙과 검증 절차, 예외 처리
 */
@Service
@Transactional(readOnly = true) // 읽기 전용 트랜잭션
@RequiredArgsConstructor // lombok : final 필드에 대한 생성자 생성 -> 스프링 컨테이너가 DI
@Slf4j // lombok : Logging 을 위한 선언부 자동 생성
public class CarService {
    private final CarRepository carRepository;
    public List<Car> findAllCars(){
        List<Car> cars = carRepository.findAll();
        log.info("조회된 자동차 수: {} 대",cars.size());
        return cars;
    }

    public Car findCarById(long carId) {
        return carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("Car Id "+carId+ "에 해당하는 자동차를 찾을 수 없습니다"));
    }
    @Transactional
    public Car createCar(String modelName, Long price) {
        Car car = Car.builder().modelName(modelName).price(price).build();
        Car savedCar = carRepository.save(car);
        return  savedCar;
    }
    @Transactional
    public void deleteCar(Long carId) {
        findCarById(carId); // carId 에 해당하는 자동차 없으면 예외 발생
        carRepository.deleteById(carId);
    }
    @Transactional
    public Car updateCar(Long carId, String modelName, Long price) {
        Car existingCar = findCarById(carId);
        // Entity 의 비즈니스 메서드 호출
        // carRepository 의 메서드를 호출하지 않고  Entity 의 인스턴스 변수들의 정보를 변경
        // Dirty Checking  변경 감지 :    트랜잭션 별로 생성되는 EntityManager 가 Persistence Context 에 저장된 Entity 의 정보가
        // 변경되었는 지 확인해 변경되었으면 데이터베이스에 update 시킨다
        existingCar.updateCar(modelName,price);
        return existingCar;
    }
}







