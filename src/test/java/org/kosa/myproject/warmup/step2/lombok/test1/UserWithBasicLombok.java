package org.kosa.myproject.warmup.step2.lombok.test1;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserWithBasicLombok {
    private Long id;
    private String username;
    private String email;
}
