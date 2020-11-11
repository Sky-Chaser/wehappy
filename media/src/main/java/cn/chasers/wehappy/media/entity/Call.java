package cn.chasers.wehappy.media.entity;

import cn.chasers.wehappy.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 通话信息表
 * </p>
 *
 * @author liamcoder
 * @since 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Call对象", description="通话信息表")
public class Call extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "时长")
    private Integer time;


}
