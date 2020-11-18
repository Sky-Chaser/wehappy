package cn.chasers.wehappy.message.mapper;

import cn.chasers.wehappy.message.entity.Unread;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户未读数表 Mapper 接口
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Repository
public interface UnreadMapper extends BaseMapper<Unread> {

    /**
     * 增加未读个数
     *
     * @param unread Unread, count 字段用来保存要增加的未读个数
     * @return
     */
    boolean increase(Unread unread);
}
