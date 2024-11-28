package com.liuxin.mapper;

import com.liuxin.pojo.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {

    int createArticle(Post post);

    Post getArticleById(Long id);

    List<Post> getAllArticles();

    List<Post> getArticlesByStatus(String status);

    List<Post> searchArticles(String searchTerm);

    int updateArticle(Post post);

    int deleteArticle(Long id);

    //服务层调用方便取值
    int updateViewCount(@Param("id") Long id, @Param("viewCount") int viewCount);

    List<Post> getTopArticlesByViewCount();
}
