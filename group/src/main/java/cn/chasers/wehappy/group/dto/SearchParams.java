package cn.chasers.wehappy.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author liamcoder
 * @date 2020/11/19 11:09 下午
 */
@Data
@Builder
public class SearchParams {
    @ApiModelProperty(value = "群组名称")
    private String groupName;

    @ApiModelProperty(value = "当前页数")
    private Long currentPage;

    @ApiModelProperty(value = "每页显示条数")
    private Long size;
}
