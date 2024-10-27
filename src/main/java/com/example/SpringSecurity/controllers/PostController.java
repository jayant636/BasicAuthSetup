package com.example.SpringSecurity.controllers;

import com.example.SpringSecurity.dtos.PostDto;
import com.example.SpringSecurity.repository.PostRepository;
import com.example.SpringSecurity.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPosts(@RequestBody PostDto postDto){
        PostDto postDto1 = postService.createPost(postDto);
        return new ResponseEntity<>(postDto1,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(){
        List<PostDto> postDtos = postService.getAllPosts();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id){
        Optional<PostDto> postDto = postService.getPostById(id);
        return postDto.map(postDto1 -> ResponseEntity.ok(postDto1)).orElseThrow(()->new RuntimeException("Post Not Found with id "+id));
    }

}
