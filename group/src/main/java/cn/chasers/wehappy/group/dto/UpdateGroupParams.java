package cn.chasers.wehappy.group.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author liamcoder
 * @date 2020/11/12 11:00 下午
 */
@Data
@Builder
public class UpdateGroupParams {
    private Long id;
    private String name;
    private String avatar;
}
