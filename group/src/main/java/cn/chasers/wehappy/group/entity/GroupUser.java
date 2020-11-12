package cn.chasers.wehappy.group.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.chasers.wehappy.common.entity.BaseEntity;
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
@TableName("GroupUser")
@ApiModel(value="GroupUser对象", description="群聊用户表")
public class GroupUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "群聊id")
    @TableField("group_id")
    private Long groupId;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "邀请用户id")
    @TableField("invited_user_id")
    private Long invitedUserId;

    @ApiModelProperty(value = "状态：0表示正常，1表示管理员邀请还未同意进群，2表示用户申请加群还未通过")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "用户类型：0表示普通群员，1表示管理员，2表示群主")
    @TableField("type")
    private Integer type;
}
