package com.example.SpringSecurity.service;

import com.example.SpringSecurity.dtos.PostDto;
import com.example.SpringSecurity.entity.PostEntity;
import com.example.SpringSecurity.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostDto createPost(PostDto postDto) {
        PostEntity postEntity = modelMapper.map(postDto, PostEntity.class);
        PostEntity postEntity1 = postRepository.save(postEntity);
        return modelMapper.map(postEntity1, PostDto.class);
    }

    public List<PostDto> getAllPosts() {
        List<PostEntity> postEntity = postRepository.findAll();
        return postEntity.stream().map(postEntity1 -> modelMapper.map(postEntity1, PostDto.class)).collect(Collectors.toList());
    }

    public Optional<PostDto> getPostById(Long id) {
        Optional<PostEntity> postEntity = postRepository.findById(id);
        return Optional.ofNullable(postEntity.map(postEntity1 -> modelMapper.map(postEntity1, PostDto.class)).orElseThrow(() -> new RuntimeException("Post Not Found with id " + id)));
    }
}
