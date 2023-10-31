package com.star.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章后台VO
 *
 * @author hyx
 */
@Data
@ApiModel(description = "文章后台VO")
public class ArticleBackVO {

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private Integer id;

    /**
     * 文章缩略图
     */
    @ApiModelProperty(value = "文章缩略图")
    private String articleCover;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    /**
     * 是否删除 (0否 1是)
     */
    @ApiModelProperty(value = "是否删除 (0否 1是)")
    private Integer isDelete;

    /**
     * 状态 (1公开 2私密 3草稿)
     */
    @ApiModelProperty(value = "状态 (1公开 2私密 3草稿)")
    private Integer status;

    /**
     * 文章分类ID
     */
    @ApiModelProperty(value = "文章分类ID")
    private Integer categoryId;

    /**
     * 文章标签
     */
    @ApiModelProperty(value = "文章标签")
    private List<TagOptionVO> tagList;


    /**
     * 发表时间
     */
    @ApiModelProperty(value = "发表时间")
    private LocalDateTime createTime;

}
