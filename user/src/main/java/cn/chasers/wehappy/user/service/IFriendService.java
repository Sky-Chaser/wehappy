package cn.chasers.wehappy.user.service;

import cn.chasers.wehappy.user.entity.Friend;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 好友信息表 服务类
 * </p>
 *
 * @author lollipop
 * @since 2020-10-26
 */
public interface IFriendService extends IService<Friend> {

    /**
     * 根据用户id添加用户
     *
     * @param fromId 请求添加用户id
     * @param toId   被请求添加用户id
     * @return 返回添加成功或失败
     */
    boolean addFriend(Long fromId, Long toId);

    /**
     * 删除好友
     *
     * @param fromId 请求删除用户id
     * @param toId   被请求删除用户id
     * @return 返回删除成功或失败
     */
    boolean deleteFriend(Long fromId, Long toId);

    /**
     * 处理添加好友的请求
     *
     * @param fromId 请求添加用户id
     * @param toId   被请求添加用户id
     * @param agree  是否同意添加
     * @return 返回处理结果
     */
    boolean handleAddFriend(Long fromId, Long toId, Boolean agree);

    /**
     * 获取好友列表
     *
     * @param userId 用户id
     * @return 返回好友列表数据
     */
    List<Friend> list(Long userId);
}
