package cn.chasers.wehappy.message.service;

import cn.chasers.wehappy.message.entity.Unread;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户未读数表 服务类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
public interface IUnreadService extends IService<Unread> {

    /**
     * 增加未读个数
     *
     * @param userId 用户Id
     * @param count  个数
     * @return
     */
    boolean increase(Long userId, int count);


    /**
     * 减少未读个数
     *
     * @param userId 用户Id
     * @param count  个数
     * @return
     */
    boolean decrease(Long userId, int count);

    /**
     * 更新未读个数
     *
     * @param userId 用户Id
     * @param count  个数
     * @return
     */
    boolean update(Long userId, int count);

    /**
     * 查询未读个数
     *
     * @param userId 用户Id
     * @return
     */
    boolean get(Long userId);
}
