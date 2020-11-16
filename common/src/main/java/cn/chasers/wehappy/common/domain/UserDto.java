package cn.chasers.wehappy.common.domain;

import lombok.*;

import java.util.List;

/**
 * 登录用户信息
 * @author lollipop
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Integer status;
    private String clientId;
    private List<String> roles;
}
