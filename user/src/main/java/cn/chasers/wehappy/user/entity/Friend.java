package cn.chasers.wehappy.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import cn.chasers.wehappy.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 好友信息表
 * </p>
 *
 * @author lollipop
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Friend对象", description="好友信息表")
public class Friend extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private Long fromId;

    @ApiModelProperty(value = "好友id")
    private Long toId;

    @ApiModelProperty(value = "状态：0表示已申请还未添加，1表示正常，2表示拉黑对方")
    private Integer status;

    @ApiModelProperty(value = "是否被对方拉黑：0表示未拉黑，1表示已拉黑")
    private Integer black;


}
