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

    @ApiModelProperty(value = "查询的消息 Id 都小于 messageId ")
    private Long messageId;

    @ApiModelProperty(value = "当前页数")
    private Long currentPage;

    @ApiModelProperty(value = "每页显示条数")
    private Long size;
}