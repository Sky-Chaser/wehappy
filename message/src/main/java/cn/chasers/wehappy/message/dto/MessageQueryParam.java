package cn.chasers.wehappy.message.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询聊天记录的参数
 *
 * @author lollipop
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MessageQueryParam {

    @ApiModelProperty(value = "好友或群聊Id")
    private Long to;

    @ApiModelProperty(value = "用户名")
    private Long time;

    @ApiModelProperty(value = "当前页数")
    private Long currentPage;

    @ApiModelProperty(value = "每页显示条数")
    private Long size;
}