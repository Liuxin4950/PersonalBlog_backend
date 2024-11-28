package com.liuxin.service;

import com.liuxin.mapper.ArticleMapper;
import com.liuxin.pojo.ApiResponse;
import com.liuxin.pojo.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    // 创建文章
    public ApiResponse<Post> createArticle(Post post) {
        try {
            // 设置创建时间和更新时间为当前时间
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            post.setCreatedAt(currentTime);
            post.setUpdatedAt(currentTime);

            // 校验 author_id 是否有效
            System.out.println("用户id：" + post.getAuthorId());
            if (post.getAuthorId() == null || post.getAuthorId() <= 0) {
                return new ApiResponse<>(null, 400, "作者不存在");
            }

            // 调用 Mapper 插入文章数据，post 对象会自动更新 ID
            articleMapper.createArticle(post);

            // post.getId() 现在包含数据库生成的 ID
            return new ApiResponse<Post>(post, 200, "文章创建成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(null, 500, "获取文章失败，系统错误");
        }
    }

    // 获取文章详情
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<Post> getArticleById(Long id) {
        try {
            Post post = articleMapper.getArticleById(id);
            if (post != null) {
                // 增加点击量
                post.setViewCount(post.getViewCount() + 1);
                articleMapper.updateViewCount(id, post.getViewCount());//在数据链路层设置了@Param("viewCount")获取了viewCount字段
            }
            return new ApiResponse<>(post, 200, "文章获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(null, 500, "获取文章失败，系统错误");
        }
    }


    // 获取所有文章列表
    public ApiResponse<List<Post>> getAllArticles() {
        try {
            List<Post> posts = articleMapper.getAllArticles();
            System.out.println(posts);
            return new ApiResponse<>(posts, 200, "文章列表获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(null, 500, "获取文章列表失败，系统错误");
        }
    }


    // 获取指定状态的文章列表
    public ApiResponse<List<Post>> getArticlesByStatus(String status) {
        try {
            List<Post> posts = articleMapper.getArticlesByStatus(status);
            return new ApiResponse<>(posts, 200, "文章列表获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(null, 500, "获取文章列表失败，系统错误");
        }
    }

    // 搜索文章
    public ApiResponse<List<Post>> searchArticles(String searchTerm) {
        try {
            System.out.println(searchTerm);
            List<Post> posts = articleMapper.searchArticles(searchTerm);
            return new ApiResponse<>(posts, 200, "搜索结果获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(null, 500, "搜索失败，系统错误");
        }
    }

    // 修改文章
    public ApiResponse<Post> updateArticle(Post post) {
        try {
            System.out.println("传来的数据:" + post);
            // 设置创建时间和更新时间为当前时间
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            post.setUpdatedAt(currentTime);
            int rowsAffected = articleMapper.updateArticle(post);
            System.out.println("返回数据:" + rowsAffected);
            if (rowsAffected > 0) {
                // 更新后重新查询完整的文章信息
                Post updatedPost = articleMapper.getArticleById(post.getId());
                return new ApiResponse<Post>(updatedPost, 200, "文章修改成功");
            } else {
                return new ApiResponse<>(null, 404, "文章未找到");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(null, 500, "文章修改失败，系统错误");
        }
    }


    // 删除文章
    public ApiResponse<Void> deleteArticle(Long id) {
        try {
            int rowsAffected = articleMapper.deleteArticle(id);
            if (rowsAffected > 0) {
                return new ApiResponse<>(null, 200, "文章删除成功");
            } else {
                return new ApiResponse<>(null, 404, "文章未找到");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(null, 500, "文章删除失败，系统错误");
        }
    }

    //获取点击量热门方法
    public ApiResponse<List<Post>> getTopArticles() {
        try {
            List<Post> posts = articleMapper.getTopArticlesByViewCount();
            return new ApiResponse<>(posts, 200, "热门文章获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(null, 500, "获取热门文章失败，系统错误");
        }
    }

}
