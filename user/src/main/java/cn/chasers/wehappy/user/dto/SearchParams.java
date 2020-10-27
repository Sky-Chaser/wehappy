package cn.chasers.wehappy.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * 根据用户名或邮箱地址模糊查询用户信息的参数
 *
 * @author lollipop
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SearchParams {

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "当前页数")
    private Long currentPage;

    @ApiModelProperty(value = "每页显示条数")
    private Long size;
}