package org.kosa.myproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kosa.myproject.entity.Car;

/*
        Dto : Data Transfer Object   계층 간 데이터 전송을 위한 객체 
        응답 : Entity 를 Dto 로 변환 ,  요청 : Dto 를 Entity 로 변환

 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDto {
    private Long carId;
    private String modelName;
    private Long price;

    /*
            Entity -> Dto 변환 ( 응답용 )
            언제 사용하는가? 자동차 정보 조회시 사용 , 조회한 Car 엔티티를 클라이언트에게 전송할때
     */
    public static CarDto from(Car car) {
        return CarDto.builder().carId(car.getCarId())
                .modelName(car.getModelName())
                .price(car.getPrice())
                .build();
    }

    /*
        DTO -> Entity 변환 ( 요청용 )
        언제 사용하는가 ? REST 기반  클라이언트가 요청시 보낸 정보를 이용해
        새로운 Car 엔티티를 생성할 때 사용
     */
    public Car toEntity() {
        return Car.builder().modelName(this.modelName).price(this.price).build();
    }
}





