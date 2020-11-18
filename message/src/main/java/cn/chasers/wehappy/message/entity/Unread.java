package cn.chasers.wehappy.message.entity;

import cn.chasers.wehappy.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户未读数表
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Unread对象", description="用户未读数表")
public class Unread extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long userId;

    @ApiModelProperty(value = "总未读消息数")
    private Integer count;

}
