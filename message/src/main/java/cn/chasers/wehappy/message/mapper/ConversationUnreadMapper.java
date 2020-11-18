package cn.chasers.wehappy.message.mapper;

import cn.chasers.wehappy.message.entity.ConversationUnread;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 会话未读数表 Mapper 接口
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Repository
public interface ConversationUnreadMapper extends BaseMapper<ConversationUnread> {
    /**
     * 增加未读个数
     *
     * @param conversationUnread ConversationUnread, count 字段用来保存要增加的未读个数
     * @return
     */
    boolean increase(ConversationUnread conversationUnread);
}
