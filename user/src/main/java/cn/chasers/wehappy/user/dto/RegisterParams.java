package cn.chasers.wehappy.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * 用户注册参数
 *
 * @author lollipop
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RegisterParams {
    @NotEmpty
    @ApiModelProperty(value = "邮箱地址", required = true)
    private String email;

    @NotEmpty
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @NotEmpty
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @NotEmpty
    @ApiModelProperty(value = "验证码", required = true)
    private String code;
}