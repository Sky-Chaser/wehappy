package cn.chasers.wehappy.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * 用户注册前检查用户名或邮箱地址是否已经被注册过的参数
 *
 * @author lollipop
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CheckExistParams {
    @NotEmpty
    @ApiModelProperty(value = "类型：\"email\" 或 \"username\"", required = true)
    private String type;

    @NotEmpty
    @ApiModelProperty(value = "用户输入的数据", required = true)
    private String data;
}