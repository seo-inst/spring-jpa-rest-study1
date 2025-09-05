package org.kosa.myproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kosa.myproject.dto.CarDto;
import org.kosa.myproject.entity.Car;
import org.kosa.myproject.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/*
        REST : REpresentational State Transfer  대표 상태 전송
                    분산 환경에서 시스템 간의 통신을 위한 아키텍쳐
                    웹 기본 프로토콜인 HTTP 를 활용한 아키텍트 스타일
                    책(자원)   /books/123
                    대출(동작)  POST  /books/123/borrow
                    반납(동작) DELETE /books/123/borrow
         REST :  프론트 엔드와 백엔드의 명확한 분리 가능
                      MSA 환경에서의 통신 방식
                      Open API 제공 ( 외부 개발자들이 우리 서비스를 활용 )
          REST 원칙
          1. 자원 식별 : URL 로 자원을 고유하게 식별
          2. HTTP 메서드 : Get 조회 , Post 생성 , Put 수정 , Delete 삭제 , Patch 부분 수정
 */
@RestController // REST API Controller     @Controller + @ResponseBody
@RequestMapping("/api/cars")
@RequiredArgsConstructor // lombok 에서  final 필드에 대한 생성자를 정의
@Slf4j
public class CarController {
    private final CarService carService;
    /*
            Get   /api/cars      ->  모든 자동차 조회  Collection
            Get   /api/cars/1   -> 특정 자동차 조회   Resource
            POST  /api/cars   -> 새 자동차 생성
            PUT    /api/cars/1   -> 자동차 수정
            DELETE /api/cars/1 -> 자동차 삭제
            PATCH /api/cars/1 -> 자동차 부분 수정
     */
    @GetMapping
    public ResponseEntity<List<CarDto>> getAllCars(){
            log.info("getAllCars 모든 자동차 조회");
            // db에서 자동차 리스트를 조회
            List<Car> cars = carService.findAllCars();
            // Entity 요소들이 저장된 List를 이용해  Dto 요소가 저장된 새로운  리스트를 생성
            List<CarDto> carDtos = cars.stream()
                    .map(car -> CarDto.from(car))
                    .collect(Collectors.toUnmodifiableList());
            // HTTP 200 OK + 데이터 응답
            return ResponseEntity.ok(carDtos);
    }
    /*
            자동차 id 로 자동차 정보 조회
            Http 요청 예시 :
            GET    http://localhost:8080/api/cars/1
     */
    @GetMapping("/{carId}")
    public ResponseEntity<CarDto> findCarById(@PathVariable  Long carId){
        try{
            Car car = carService.findCarById(carId);
            CarDto carDto = CarDto.from(car);
            return ResponseEntity.ok(carDto);
        } catch (Exception e) {
            log.warn("자동차 조회 실패 ID {} 에러 {}",carId,e.getMessage());
            return ResponseEntity.notFound().build();// HTTP Response status 404로 응답
        }
    }
    /**
     *  자동차 정보 등록
     *  POST    http://localhost:8080/api/cars
     *  Content-Type   application/json
     {
     "modelName": "프리우스",
     "price": 45000000
     }

     @RequestBody : Http 요청 body 의 JSON을 Java 객체로 자동 변환 , Spring 의 Jackson 라이브러리가 처리
     */
    @PostMapping
    public ResponseEntity<CarDto> createCar(@RequestBody CarDto carDto){
        try{
            Car createdCar = carService.createCar(carDto.getModelName(),carDto.getPrice());
            CarDto responseDto = CarDto.from(createdCar); // Entity 를 Dto 로 변환
            // Created  201  생성 작업 완료
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }
    /*
            PUT    http://localhost:8080/api/cars/2
            Content-Type  application/json
            {
                "modelName":"니로",
                "price":27000000
            }
     */
    // 전체 정보 수정할때 Put 을 이용한다
    @PutMapping("/{carId}")
    public ResponseEntity<CarDto> updateCar(@PathVariable Long carId,@RequestBody CarDto carDto){
        try {
            Car updatedCar = carService.updateCar(carId, carDto.getModelName(), carDto.getPrice());
            CarDto responseDto = CarDto.from(updatedCar);// Entity를 Dto로 변환해서 응답
            return ResponseEntity.ok(responseDto);
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }
    /*
            삭제
            DELETE  http://localhost:8080/api/cars/2
            삭제 응답
            성공 : HTTP Response Status code 204 No Content ( 응답 없음 )
            실패 : HTTP  404 Not found
     */
    @DeleteMapping("/{carId}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long carId){
        try{
            carService.deleteCar(carId);
            return ResponseEntity.noContent().build();//204  정상 처리하고 응답 본문 없음을 알림
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // 404
        }
    }
    /**
     *  모델명으로 자동차 검색
     *
     *  GET  http://localhost:8080/api/cars/search?modelName=소나타
     */
    @GetMapping("/search")
    public ResponseEntity<List<CarDto>> searchCarsByModelName(@RequestParam String modelName){
        List<Car> cars = carService.findCarsByModelName(modelName);
        // Entity 요소들로 구성된 List 를  Dto 요소들로 구성된 List로 변환해 생성
        List<CarDto> carDtos = cars.stream()
                .map(CarDto::from)
                .collect(Collectors.toUnmodifiableList());
        return ResponseEntity.ok(carDtos);
    }
    /**
     *  자동차 가격 할인 적용
     * @param carId  자동차 ID
     * @param rate  할인율
     * @return  할인 적용된 자동차 정보
     *
     * HTTP 요청 예시 :
     * PATCH    http://localhost:8080/api/cars/5/discount-rate?rate=0.1
     *
     * PUT :  전체 리소스 교체
     * PATCH : 부분 수정
     *
     * 할인 적용은 가격만 변경하므로 PATCH 가 적합
     */
    @PatchMapping("/{carId}/discount-rate")
    public ResponseEntity<CarDto> applyDiscount(@PathVariable Long carId,@RequestParam double rate){
        try {
            Car discountedCar = carService.applyDiscount(carId, rate);
            CarDto responseDto = CarDto.from(discountedCar);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}


































