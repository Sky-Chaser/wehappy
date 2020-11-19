package cn.chasers.wehappy.group.dto;

import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;

/**
 * @author liamcoder
 * @date 2020/11/19 10:49 下午
 */
@Data
@Builder
public class GroupUserParam {
    @ApiParam("群组id")
    private Long groupId;
    @ApiParam("用户id")
    private Long userId;

}
