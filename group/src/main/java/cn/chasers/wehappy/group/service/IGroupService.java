package cn.chasers.wehappy.group.service;

import cn.chasers.wehappy.group.dto.UpdateGroupParams;
import cn.chasers.wehappy.group.entity.Group;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 群聊信息表 服务类
 * </p>
 *
 * @author liamcoder
 * @since 2020-11-12
 */
public interface IGroupService extends IService<Group> {
    /**
     * 创建群组
     *
     * @param currentUserId  进行当前操作的用户ID
     * @param name 群名称
     * @param avatar 头像url
     * @return 返回是否创建成功
     */
    boolean create(Long currentUserId, String name, String avatar);

    /**
     * 解散群组
     * @param currentUserId  进行当前操作的用户ID
     * @param groupId 群组ID
     * @return 返回是否解散成功
     */
    boolean remove(Long currentUserId, Long groupId);

    /**
     * 修改群组信息
     *
     * @param updateGroupParams 需要修改的具体信息
     * @param currentUserId  进行当前操作的用户ID
     * @return 返回是否修改成功
     */
    boolean update(Long currentUserId, UpdateGroupParams updateGroupParams);

    /**
     * 转让群组
     *
     * @param groupId 群组ID
     * @param toId    新群主的ID
     * @param currentUserId  进行当前操作的用户ID
     * @return 返回是否转让成功
     */
    boolean transfer(Long currentUserId, Long groupId, Long toId);

    /**
     * 根据群名称搜索群组
     *
     * @param groupName 群组名称
     * @param currentPage 当前页
     * @param size 每页数据
     * @return 返回群组具体信息
     */
    IPage<Group> search(String groupName, long currentPage, long size);
}
