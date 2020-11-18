package cn.chasers.wehappy.message.entity;

import cn.chasers.wehappy.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户好友未读数表
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ConversationUnread对象", description = "会话未读数表")
public class ConversationUnread extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会话Id")
    private Long conversationId;

    @ApiModelProperty(value = "最后一条已读消息Id")
    private Long lastReadMessageId;

    @ApiModelProperty(value = "未读消息数")
    private Integer count;

}
