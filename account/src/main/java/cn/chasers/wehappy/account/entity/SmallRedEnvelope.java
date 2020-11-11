package cn.chasers.wehappy.account.entity;

import java.math.BigDecimal;
import cn.chasers.wehappy.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 小红包信息表
 * </p>
 *
 * @author lollipop
 * @since 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Small_red_envelope对象", description="小红包信息表")
public class SmallRedEnvelope extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "大红包id")
    private Long bigRedEnvelopeId;

    @ApiModelProperty(value = "金额")
    private BigDecimal money;

    @ApiModelProperty(value = "领取用户id")
    private Long userId;


}
