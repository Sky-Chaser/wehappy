package cn.chasers.wehappy.group.feign.fallback;

import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.domain.UserDto;
import cn.chasers.wehappy.group.feign.IUserService;

/**
 * @author liamcoder
 * @date 2020/11/12 9:48 下午
 */
public class UserServiceImpl implements IUserService {

    @Override
    public CommonResult<UserDto> query(Long id) {
        return CommonResult.failed("user service 暂不可用.....");
    }
}
