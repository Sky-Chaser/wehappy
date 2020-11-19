package cn.chasers.wehappy.account.domain;

import cn.chasers.wehappy.account.entity.BigRedEnvelope;
import cn.chasers.wehappy.account.entity.SmallRedEnvelope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 用于存储红包信息
 *
 * @author zhangyuanhang
 * @date 2020/11/20: 12:17 上午
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedEnvelopeInfo {
    private BigRedEnvelope bigRedEnvelope;
    private List<SmallRedEnvelope> smallRedEnvelopes;
}
