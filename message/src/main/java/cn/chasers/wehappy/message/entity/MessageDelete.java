package cn.chasers.wehappy.message.entity;

import cn.chasers.wehappy.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 消息删除记录表
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MessageDelete对象", description="消息删除记录表")
public class MessageDelete extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息类型：0表示私聊消息，1表示群聊消息，2表示推送消息")
    private Integer type;

    @ApiModelProperty(value = "发送者id")
    private Long from;

    @ApiModelProperty(value = "接收者id：type为1时表示群聊Id")
    private Long to;

    @ApiModelProperty(value = "消息索引id")
    private Long messageIndexId;

}
