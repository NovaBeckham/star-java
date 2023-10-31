package com.star.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.entity.ArticleTag;
import com.star.entity.Tag;
import com.star.mapper.ArticleMapper;
import com.star.mapper.ArticleTagMapper;
import com.star.mapper.TagMapper;
import com.star.model.dto.ConditionDTO;
import com.star.model.dto.TagDTO;
import com.star.model.vo.*;
import com.star.service.TagService;
import com.star.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 标签业务接口实现类
 *
 * @author hyx
 **/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PageResult<TagBackVO> listTagBackVO(ConditionDTO condition) {
        // 查询标签数量
        Long total = tagMapper.selectCount(new LambdaQueryWrapper<Tag>().like(StringUtils.hasText(condition.getKeyword()), Tag::getTagName, condition.getKeyword()));
        if (total == 0) {
            return new PageResult<>();
        }
        // 分页查询标签列表
        List<TagBackVO> tagList = tagMapper.selectTagBackVO(PageUtils.getLimit(), PageUtils.getSize(), condition.getKeyword());
        return new PageResult<>(tagList, total, PageUtils.getCurrent(), PageUtils.getSize());
    }

    @Override
    public void addTag(TagDTO tag) {
        // 标签是否存在
        Tag existTag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().select(Tag::getId).eq(Tag::getTagName, tag.getTagName()));
        Assert.isNull(existTag, tag.getTagName() + "标签已存在");
        // 添加新标签
        Tag newTag = Tag.builder().tagName(tag.getTagName()).build();
        baseMapper.insert(newTag);
    }

    @Override
    public void deleteTag(List<Integer> tagIdList) {
        // 标签下是否有文章
        Long count = articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>().in(ArticleTag::getTagId, tagIdList));
        Assert.isFalse(count > 0, "删除失败，标签下存在文章");
        // 批量删除标签
        tagMapper.deleteBatchIds(tagIdList);
    }

    @Override
    public void updateTag(TagDTO tag) {
        // 标签是否存在
        Tag existTag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().select(Tag::getId).eq(Tag::getTagName, tag.getTagName()));
        Assert.isFalse(Objects.nonNull(existTag) && !existTag.getId().equals(tag.getId()), tag.getTagName() + "标签已存在");
        // 修改标签
        Tag newTag = Tag.builder().id(tag.getId()).tagName(tag.getTagName()).build();
        baseMapper.updateById(newTag);
    }

    @Override
    public List<TagOptionVO> listTagOption() {
        return tagMapper.selectTagOptionList();
    }

    @Override
    public List<TagVO> listTagVO() {
        return tagMapper.selectTagVOList();
    }

    @Override
    public ArticleListVO listArticleTag(ConditionDTO condition) {
        List<ArticleConditionVO> articleConditionList = articleMapper.listArticleByCondition(PageUtils.getLimit(), PageUtils.getSize(), condition);
        String name = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().select(Tag::getTagName).eq(Tag::getId, condition.getTagId())).getTagName();
        return ArticleListVO.builder().articleConditionVOList(articleConditionList).name(name).build();
    }
}
