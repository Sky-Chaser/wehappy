package cn.chasers.wehappy.account.entity;

import java.math.BigDecimal;
import cn.chasers.wehappy.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 账户表
 * </p>
 *
 * @author lollipop
 * @since 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Account对象", description="账户表")
public class Account extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "账户余额")
    private BigDecimal money;

    @ApiModelProperty(value = "状态：0表示正常，1表示被冻结")
    private Integer status;

}
