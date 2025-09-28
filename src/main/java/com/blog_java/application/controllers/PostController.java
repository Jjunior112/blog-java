package com.blog_java.application.controllers;

import com.blog_java.application.services.PostService;
import com.blog_java.domain.dtos.post.PostListDto;
import com.blog_java.domain.dtos.post.PostRegisterDto;
import com.blog_java.domain.dtos.post.UpdatePostDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/post")
@SecurityRequirement(name = "bearer-key")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostListDto> createPost(@ModelAttribute @Valid PostRegisterDto postRegisterDto, @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        byte[] imageBytes = (image!=null)? image.getBytes() : null;

        var response = postService.createPost(postRegisterDto,imageBytes);

        return ResponseEntity.ok(new PostListDto(response));
    }

    @GetMapping
    public ResponseEntity<Page<PostListDto>> getAllPost(@RequestParam Long userId, @PageableDefault(size = 10, sort = {"title"}) Pageable pagination)
    {
        var posts = postService.findAllPosts(userId,pagination).map(PostListDto::new);

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<PostListDto>> getAllPostUnverified(@RequestParam Long userId, @PageableDefault(size = 10, sort = {"title"}) Pageable pagination)
    {
        var posts = postService.findAllPostsUnverified(userId,pagination).map(PostListDto::new);

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostListDto> getPostById(@PathVariable Long id)
    {
        var response = postService.findPostById(id);

        return ResponseEntity.ok(new PostListDto(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostListDto> putPost(@PathVariable Long id, @ModelAttribute UpdatePostDto updatePostDto,@RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        byte[] imageBytes = (image!=null)? image.getBytes() : null;

        var post = postService.updatePostById(id,updatePostDto,imageBytes);

        return ResponseEntity.ok(new PostListDto(post));
    }

    @PutMapping("/pending/{id}")
    public ResponseEntity<PostListDto> verifyPost(@PathVariable Long id) {

        var post = postService.verifyPostById(id);

        return ResponseEntity.ok(new PostListDto(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePosts(@PathVariable Long id)
    {
        postService.deletePost(id);

        return ResponseEntity.noContent().build();
    }

}
