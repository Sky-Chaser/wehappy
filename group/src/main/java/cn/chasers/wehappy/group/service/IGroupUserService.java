package cn.chasers.wehappy.group.service;

import cn.chasers.wehappy.group.entity.Group;
import cn.chasers.wehappy.group.entity.GroupUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 群聊用户表 服务类
 * </p>
 *
 * @author liamcoder
 * @since 2020-11-12
 */
public interface IGroupUserService extends IService<GroupUser> {
    /**
     * 邀请用户加入群组, 只有管理员/群主才能邀请
     *
     * @param userId  被邀请者ID
     * @param currentUserId 当前操作的用户ID
     * @param groupId 群组ID
     * @return 返回是否成功发出邀请
     */
    boolean invite(Long currentUserId, Long userId, Long groupId);

    /**
     * 申请加入群组
     *
     * @param currentUserId 当前操作的用户ID
     * @param groupId 群组ID
     * @return 返回是否成功发出申请
     */
    boolean apply(Long currentUserId, Long groupId);

    /**
     * 群组管理员处理用户加入群组的申请
     *
     * @param currentUserId 当前操作的用户ID
     * @param groupId    群组的ID
     * @param groupUserId    需要处理的入群申请
     * @param agree 是否同意
     * @return 返回是否成功处理申请
     */
    boolean handleApply(Long currentUserId, Long groupId, Long groupUserId, Boolean agree);

    /**
     * 用户处理管理员/群主的邀请
     *
     * @param currentUserId 被邀请的用户ID
     * @param groupId  群组ID
     * @param agree 是否同意入群
     * @return 返回是否成功处理邀请
     */
    boolean handleInvite(Long currentUserId, Long groupId, Boolean agree);

    /**
     * 添加群组管理员
     *
     * @param currentUserId 当前操作的用户ID
     * @param userId  管理员ID
     * @param groupId 群组ID
     * @return 返回是否成功添加管理员
     */
    boolean addAdmin(Long currentUserId, Long userId, Long groupId);

    /**
     * 删除群组管理员
     *
     * @param currentUserId 当前操作的用户ID
     * @param userId  欲删除的管理员ID
     * @param groupId 群组ID
     * @return 返回是否删除成功
     */
    boolean deleteAdmin(Long currentUserId, Long userId, Long groupId);

    /**
     * 删除群组成员
     *
     * @param currentUserId 当前操作的用户ID
     * @param userId  欲删除的用户ID
     * @param groupId 群组ID
     * @return 返回是否成功删除成员
     */
    boolean deleteMember(Long currentUserId, Long userId, Long groupId);

    /**
     * 返回群组列表
     *
     * @param currentUserId 当前操作的用户ID
     * @return 返回群组列表
     */
    List<Group> getList(Long currentUserId);

    /**
     * 退出群组
     *
     * @param currentUserId 当前操作的用户ID
     * @param groupId 欲退出的群组ID
     * @return 返回是否成功退出
     */
    boolean exit(Long currentUserId, Long groupId);

    /**
     * 查询群组中全部用户 Id
     *
     * @param id 群组 Id
     * @return 用户 Id list
     */
    List<Long> getUserIds(Long id);

    /**
     * 查询群组用户信息
     *
     * @param groupId 群组 Id
     * @param userId  用户 Id
     * @return GroupUser
     */
    GroupUser getByGroupIdAndUserId(Long groupId, Long userId);

    /**
     * 获取所有申请加群的用户信息
     * @param groupId   群组ID
     * @return          返回全部的GroupUser
     */
    List<GroupUser> getAppliedUsers(Long groupId);

    /**
     * 判断是否为管理员/群主
     * @param userId  用户ID
     * @param groupId 群组ID
     * @return   返回是否为管理员的boolean值
     */
    boolean isAdmin(Long userId, Long groupId);
}
