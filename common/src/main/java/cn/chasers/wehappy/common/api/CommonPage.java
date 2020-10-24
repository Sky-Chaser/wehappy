package cn.chasers.wehappy.common.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * 分页数据封装类
 * @author zhangyuanhang
 */
@ApiModel(value = "分页查询结果")
@Data
public class CommonPage<T> {
    private Long pageNum;
    private Long pageSize;
    private Long totalPage;
    private Long total;
    private List<T> list;

    /**
     * 分页后的IPage转为分页信息
     */
    public static <T> CommonPage<T> restPage(IPage<T> pageModel) {
        CommonPage<T> result = new CommonPage<>();
        result.setTotalPage(pageModel.getPages());
        result.setPageNum(pageModel.getCurrent());
        result.setPageSize(pageModel.getSize());
        result.setTotal(pageModel.getTotal());
        result.setList(pageModel.getRecords());
        return result;
    }
}
