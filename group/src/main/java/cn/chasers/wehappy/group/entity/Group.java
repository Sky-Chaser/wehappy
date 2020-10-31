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
 * 群聊信息表
 * </p>
 *
 * @author lollipop
 * @since 2020-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("group")
@ApiModel(value="Group对象", description="群聊信息表")
public class Group extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "群名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "管理员人数")
    @TableField("admin_count")
    private Integer adminCount;

    @ApiModelProperty(value = "群人数")
    @TableField("member_count")
    private Integer memberCount;

    @ApiModelProperty(value = "群主Id")
    @TableField("owner_id")
    private Long ownerId;

    @ApiModelProperty(value = "头像链接")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty(value = "状态：0表示正常，1表示被冻结")
    @TableField("status")
    private Integer status;


}
