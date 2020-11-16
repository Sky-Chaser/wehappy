package cn.chasers.wehappy.account.mapper;

import cn.chasers.wehappy.account.entity.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 账户表 Mapper 接口
 * </p>
 *
 * @author lollipop
 * @since 2020-11-12
 */
public interface AccountMapper extends BaseMapper<Account> {
    /**
     * 修改 money 字段值，加上传入的参数的值
     *
     * @param userId 用户Id
     * @param money 加上的值
     * @return 修改是否成功
     */
    boolean addMoney(@Param("userId") Long userId, @Param("money") BigDecimal money);
}
