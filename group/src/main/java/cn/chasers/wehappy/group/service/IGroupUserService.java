package cn.chasers.wehappy.group.service;

import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.group.entity.Group;
import cn.chasers.wehappy.group.entity.GroupUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 群聊用户表 服务类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-01
 */
public interface IGroupUserService extends IService<GroupUser> {
    /**
     * 邀请用户加入群组, 只有管理员/群主才能邀请
     *
     * @param userId  被邀请者ID
     * @param groupId 群组ID
     * @return 返回是否成功发出邀请
     */
    boolean invite(Long userId, Long groupId);

    /**
     * 申请加入群组
     *
     * @param groupId 群组ID
     * @return 返回是否成功发出申请
     */
    boolean apply(Long groupId);

    /**
     * 群组管理员处理用户加入群组的申请
     *
     * @param id    group_user表的ID
     * @param agree 是否同意
     * @return 返回是否成功处理申请
     */
    boolean handleApply(Long id, Boolean agree);

    /**
     * 用户处理管理员/群主的邀请
     *
     * @param id    group_user表的ID
     * @param agree 是否同意入群
     * @return 返回是否成功处理邀请
     */
    boolean handleInvite(Long id, Boolean agree);

    /**
     * 添加群组管理员
     *
     * @param userId  管理员ID
     * @param groupId 群组ID
     * @return 返回是否成功添加管理员
     */
    boolean addAdmin(Long userId, Long groupId);

    /**
     * 删除群组管理员
     * @param userId        欲删除的管理员ID
     * @param groupId       群组ID
     * @return              返回是否删除成功
     */
    boolean deleteAdmin(Long userId, Long groupId);

    /**
     * 删除群组成员
     *
     * @param userId  欲删除的用户ID
     * @param groupId 群组ID
     * @return 返回是否成功删除成员
     */
    boolean deleteMember(Long userId, Long groupId);

    /**
     * 管理员/群主给群成员禁言
     *
     * @param userId  欲被禁言的用户ID
     * @param groupId 群组ID
     * @return 返回是否禁言成功
     */
    boolean forbidden(Long userId, Long groupId);

    /**
     * 返回群组列表
     *
     * @return 返回群组列表
     */
    List<Group> getList();

    /**
     * 退出群组
     *
     * @param groupId 欲退出的群组ID
     * @return 返回是否成功退出
     */
    boolean exit(Long groupId);
}
