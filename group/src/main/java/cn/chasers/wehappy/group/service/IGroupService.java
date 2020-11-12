package cn.chasers.wehappy.group.service;

import cn.chasers.wehappy.group.dto.GroupDto;
import cn.chasers.wehappy.group.entity.Group;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
     * @param groupName    群名称
     * @return             返回是否创建成功
     */
    boolean create(String groupName);

    /**
     * 解散群组
     * @param groupId   群组ID
     * @return          返回是否解散成功
     */
    boolean remove(Long groupId);

    /**
     * 修改群组信息
     * @param groupDto  需要修改的具体信息
     * @return          返回是否修改成功
     */
    boolean update(GroupDto groupDto);

    /**
     * 转让群组
     * @param groupId   群组ID
     * @param toId      新群主的ID
     * @return          返回是否转让成功
     */
    boolean transfer(Long groupId, Long toId);

    /**
     * 根据群名称搜索群组
     * @param groupName     群组名称
     * @return              返回群组具体信息
     */
    Group search(String groupName);
}
