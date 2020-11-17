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
 * 群聊信息索引表
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="GroupMessageIndex对象", description="群聊信息索引表")
public class GroupMessageIndex extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "群聊id")
    private Long groupId;

    @ApiModelProperty(value = "消息发送用户id")
    private Long fromId;

    @ApiModelProperty(value = "消息id")
    private Long messageId;


}
