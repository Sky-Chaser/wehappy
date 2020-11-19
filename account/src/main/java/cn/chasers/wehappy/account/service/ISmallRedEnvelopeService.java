package cn.chasers.wehappy.account.service;

import cn.chasers.wehappy.account.entity.SmallRedEnvelope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
     * 获取大红包的小红包列表
     *
     * @param bigId  大红包 Id
     * @return 小红包列表
     */
    List<SmallRedEnvelope> getAllByBigRedEnvelopeId(Long bigId);
}
