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

    @ApiModelProperty(value = "消息id")
    private Long messageId;

    @ApiModelProperty(value = "用户id")
    private Long userId;


}
