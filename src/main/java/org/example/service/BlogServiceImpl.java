package org.example.service;

import org.example.annotation.Service;
import org.example.entity.Blog;

import java.util.Random;

@Service
public class BlogServiceImpl implements BlogService{
    @Override
    public Blog findBlogById(Integer id) {
        Random random = new Random();
        Blog blog = Blog.builder().id(random.nextInt())
                .data("12fffdsfdsfds")
                .title("2b").build();
        return blog;
    }
}
