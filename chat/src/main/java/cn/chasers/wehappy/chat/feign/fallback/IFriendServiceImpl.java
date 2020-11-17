package cn.chasers.wehappy.chat.feign.fallback;

import cn.chasers.wehappy.chat.feign.IFriendService;
import cn.chasers.wehappy.chat.feign.IUserService;
import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.domain.UserDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 调用 `user` 服务方法的兜底类
 * @author lollipop
 */
@Service
public class IFriendServiceImpl implements IFriendService {

    @Override
    public Mono<CommonResult<Boolean>> isFriend(Long fromId, Long toId) {
        return Mono.just(CommonResult.failed("user service 暂不可用....."));
    }
}
