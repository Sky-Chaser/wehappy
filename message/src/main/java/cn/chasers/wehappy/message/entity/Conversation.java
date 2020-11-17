package cn.chasers.wehappy.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import cn.chasers.wehappy.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 最近会话表
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Conversation对象", description="最近会话表")
public class Conversation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "发送者id")
    private Long fromId;

    @ApiModelProperty(value = "接收者id")
    private Long toId;

    @ApiModelProperty(value = "消息id")
    private Long messageId;


}
