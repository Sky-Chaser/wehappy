package cn.chasers.wehappy.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import cn.chasers.wehappy.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author lollipop
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="User对象", description="用户信息表")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "性别：0表示男性，1表示女性，2表示未知")
    private Integer sex;

    @ApiModelProperty(value = "头像链接")
    private String avatar;

    @ApiModelProperty(value = "状态：0表示注册未激活，1表示正常，2表示被冻结")
    private Integer status;

    @ApiModelProperty(value = "获赞个数")
    private Integer numberLike;


}
