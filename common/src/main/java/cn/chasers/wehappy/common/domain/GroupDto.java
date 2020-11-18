package cn.chasers.wehappy.common.domain;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author zhangyuanhang
 * @date 2020/11/18: 11:53 下午
 */
@Data
public class GroupDto {
    @ApiParam(value = "Id")
    private Long id;

    @ApiParam(value = "群名称")
    private String name;

    @ApiParam(value = "状态：0表示正常，1表示被冻结")
    private Integer status;
}
