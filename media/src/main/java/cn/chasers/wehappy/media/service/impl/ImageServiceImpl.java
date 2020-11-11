package cn.chasers.wehappy.media.service.impl;

import cn.chasers.wehappy.media.entity.Image;
import cn.chasers.wehappy.media.mapper.ImageMapper;
import cn.chasers.wehappy.media.service.IImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图片信息表 服务实现类
 * </p>
 *
 * @author liamcoder
 * @since 2020-11-12
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements IImageService {

}
