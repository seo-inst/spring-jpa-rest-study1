package org.kosa.myproject.warmup.step2.lombok.test2;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserWithBuilderLombok {
    private Long id;
    private String username;
    private String email;
}
