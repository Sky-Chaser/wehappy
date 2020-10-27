package cn.chasers.wehappy.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 根据用户名或邮箱地址查询用户信息的参数
 *
 * @author lollipop
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QueryParams {
    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "用户名")
    private String username;
}