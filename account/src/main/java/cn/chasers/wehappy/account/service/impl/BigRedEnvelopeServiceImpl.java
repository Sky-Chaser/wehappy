package cn.chasers.wehappy.account.service.impl;

import cn.chasers.wehappy.account.entity.BigRedEnvelope;
import cn.chasers.wehappy.account.mapper.BigRedEnvelopeMapper;
import cn.chasers.wehappy.account.service.IBigRedEnvelopeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 大红包信息表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-12
 */
@Service
public class BigRedEnvelopeServiceImpl extends ServiceImpl<BigRedEnvelopeMapper, BigRedEnvelope> implements IBigRedEnvelopeService {

    @Override
    public Boolean send(Long userId, Long type, Long to, BigDecimal money) {
        return null;
    }

    @Override
    public Boolean snap(Long userId, Long bigRedEnvelopeId) {
        return null;
    }

    @Override
    public BigRedEnvelope get(Long userId, Long id) {
        return null;
    }
}
