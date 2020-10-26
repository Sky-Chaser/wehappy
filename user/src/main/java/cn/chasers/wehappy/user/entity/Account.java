package cn.chasers.wehappy.user.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import cn.chasers.wehappy.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户账户表
 * </p>
 *
 * @author lollipop
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Account对象", description="用户账户表")
public class Account extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "账户余额")
    private BigDecimal money;

    @ApiModelProperty(value = "状态：0表示正常，1表示被冻结")
    private Integer status;


}
