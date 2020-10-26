package cn.chasers.wehappy.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 所有Entity的父类
 * 定义了每张表都有的主键字段、创建时间字段、更新时间字段
 * @author lollipop
 */
@Data
public class BaseEntity implements Serializable {

    protected static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    protected Long id;

    @ApiModelProperty(value = "创建时间", example = "2020-10-03 14:00:00")
    @TableField(fill = FieldFill.INSERT)
    protected Date gmtCreate;

    @ApiModelProperty(value = "更新时间", example = "2020-10-03 14:00:00")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Date gmtModified;
}