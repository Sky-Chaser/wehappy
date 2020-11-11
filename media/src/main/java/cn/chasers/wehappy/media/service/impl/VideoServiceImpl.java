package cn.chasers.wehappy.media.service.impl;

import cn.chasers.wehappy.media.entity.Video;
import cn.chasers.wehappy.media.mapper.VideoMapper;
import cn.chasers.wehappy.media.service.IVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 视频信息表 服务实现类
 * </p>
 *
 * @author liamcoder
 * @since 2020-11-12
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {

}
