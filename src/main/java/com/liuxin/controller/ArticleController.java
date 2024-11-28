package com.liuxin.controller;

import com.liuxin.pojo.ApiResponse;
import com.liuxin.pojo.Post;
import com.liuxin.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")  // 修改为“posts”以符合 RESTful 风格
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    // 创建文章
    @PostMapping
    public ResponseEntity<ApiResponse<Post>> createArticle(@RequestBody Post post) {
        // 验证标题和内容不能为空
        if (post.getTitle() == null || post.getTitle().isEmpty() ||
                post.getContent() == null || post.getContent().isEmpty()) {
            ApiResponse<Post> response = new ApiResponse<>(null, 400, "标题和内容不能为空");
            return ResponseEntity.status(response.getCode()).body(response);
        }

        // 调用服务层创建文章
        ApiResponse<Post> response = articleService.createArticle(post);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    // 获取所有文章列表
    @GetMapping
    public ResponseEntity<ApiResponse<List<Post>>> getAllArticles() {
        ApiResponse<List<Post>> response = articleService.getAllArticles();
        return ResponseEntity.status(response.getCode()).body(response);
    }


    // 获取文章详情
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Post>> getArticleById(@PathVariable Long id) {
        ApiResponse<Post> response = articleService.getArticleById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // 获取文章列表（按状态）
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Post>>> getArticlesByStatus(@PathVariable String status) {
        ApiResponse<List<Post>> response = articleService.getArticlesByStatus(status);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // 搜索文章
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Post>>> searchArticles(@RequestParam String searchTerm) {
        ApiResponse<List<Post>> response = articleService.searchArticles(searchTerm);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // 修改文章
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Post>> updateArticle(@PathVariable Long id, @RequestBody Post post) {
        post.setId(id);  // 设置ID，以便Mapper更新正确的文章
        ApiResponse<Post> response = articleService.updateArticle(post);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // 删除文章
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable Long id) {
        ApiResponse<Void> response = articleService.deleteArticle(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    //获取热门
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<Post>>> getTopArticles() {
        ApiResponse<List<Post>> response = articleService.getTopArticles();
        return ResponseEntity.status(response.getCode()).body(response);
    }


}
