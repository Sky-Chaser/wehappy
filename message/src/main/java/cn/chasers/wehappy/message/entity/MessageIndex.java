package cn.chasers.wehappy.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import cn.chasers.wehappy.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 消息索引表
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MessageIndex对象", description="消息索引表")
public class MessageIndex extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "发送者id")
    private Long fromId;

    @ApiModelProperty(value = "接收者id")
    private Long toId;

    @ApiModelProperty(value = "发送者id_接收者id")
    private Long fromTo;

    @ApiModelProperty(value = "消息id")
    private Long messageId;

    @ApiModelProperty(value = "是否删除：0表示未删除，1表示已删除")
    @TableLogic
    private Integer isDeleted;


}
