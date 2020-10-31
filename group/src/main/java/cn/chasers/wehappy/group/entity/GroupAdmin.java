package cn.chasers.wehappy.group.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import cn.chasers.wehappy.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 群聊用户表
 * </p>
 *
 * @author lollipop
 * @since 2020-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("GroupAdmin")
@ApiModel(value="GroupAdmin对象", description="群聊用户表")
public class GroupAdmin extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "群聊id")
    @TableField("group_id")
    private Long groupId;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;


}
