package org.kosa.myproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kosa.myproject.dto.CarDto;
import org.kosa.myproject.entity.Car;
import org.kosa.myproject.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController // REST API Controller
@RequestMapping("/api/cars")
@RequiredArgsConstructor // lombok 에서  final 필드에 대한 생성자를 정의
@Slf4j
public class CarController {
    private final CarService carService;
    /*
            Get   /api/cars      ->  모든 자동차 조회  Collection
            Get   /api/cars/1   -> 특정 자동차 조회   Resource
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
}








