package cn.chasers.wehappy.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author liamcoder
 * @date 2020/11/19 11:25 下午
 */
@Data
@Builder
public class HandlerParam {
    @ApiModelProperty(value = "群组id")
    private Long groupId;
    @ApiModelProperty(value = "用户id")
    private Long groupUserId;
    @ApiModelProperty(value = "是否同意")
    private Boolean agree;
}
