package cn.chasers.wehappy.media.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import cn.chasers.wehappy.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 语音信息表
 * </p>
 *
 * @author liamcoder
 * @since 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Voice对象", description="语音信息表")
public class Voice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件路径")
    private String url;

    @ApiModelProperty(value = "时长")
    private Integer time;

    @ApiModelProperty(value = "大小，单位Byte")
    private Long size;

    @ApiModelProperty(value = "md5值")
    private String md5;


}
