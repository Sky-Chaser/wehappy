package cn.chasers.wehappy.account.entity;

import java.math.BigDecimal;
import cn.chasers.wehappy.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 大红包信息表
 * </p>
 *
 * @author lollipop
 * @since 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="BigRedEnvelope对象", description="大红包信息表")
public class BigRedEnvelope extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "发送红包用户id")
    private Long userId;

    @ApiModelProperty(value = "总金额")
    private BigDecimal money;

    @ApiModelProperty(value = "总份数")
    private Integer total;

    @ApiModelProperty(value = "剩余份数")
    private Integer remains;

    @ApiModelProperty(value = "类型：7表示私聊红包，8表示运气红包")
    private Integer type;


}
