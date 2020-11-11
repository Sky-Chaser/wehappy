package cn.chasers.wehappy.media.service.impl;

import cn.chasers.wehappy.media.entity.Call;
import cn.chasers.wehappy.media.mapper.CallMapper;
import cn.chasers.wehappy.media.service.ICallService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 通话信息表 服务实现类
 * </p>
 *
 * @author liamcoder
 * @since 2020-11-12
 */
@Service
public class CallServiceImpl extends ServiceImpl<CallMapper, Call> implements ICallService {

}
