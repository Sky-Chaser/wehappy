package cn.chasers.wehappy.message.entity;

import cn.chasers.wehappy.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 消息表
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Message对象", description="消息表")
public class Message extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "时间戳")
    private Long time;

    @ApiModelProperty(value = "消息类型：0表示文本消息，1表示系统消息，2表示图片，3表示语音，4表示视频，5表示语音通话，6表示视频通话，7表示私聊红包，8表示群聊普通红包，9表示群聊运气红包")
    private Integer type;

    @ApiModelProperty(value = "消息内容")
    private String content;

}
