package cn.chasers.wehappy.group.dto;

import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;

/**
 * @author liamcoder
 * @date 2020/11/12 11:00 下午
 */
@Data
@Builder
public class UpdateGroupParams {
    @ApiParam(value = "id")
    private Long id;
    @ApiParam(value = "群名称")
    private String name;
    @ApiParam(value = "头像URL")
    private String avatar;
}
