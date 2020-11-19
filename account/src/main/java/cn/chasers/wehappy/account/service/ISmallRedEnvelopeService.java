package cn.chasers.wehappy.account.service;

import cn.chasers.wehappy.account.entity.SmallRedEnvelope;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 小红包信息表 服务类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-12
 */
public interface ISmallRedEnvelopeService extends IService<SmallRedEnvelope> {

    /**
     * 获取小红包详细信息
     *
     * @param userId 用户 Id
     * @param id     小红包 Id
     * @return 小红包详细信息
     */
    SmallRedEnvelope get(Long userId, Long id);
}
