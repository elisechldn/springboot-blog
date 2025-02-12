package org.wildcodeschool.MyBlog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.MyBlog.dto.ImageDTO;
import org.wildcodeschool.MyBlog.mapper.ImageMapper;
import org.wildcodeschool.MyBlog.model.Image;
import org.wildcodeschool.MyBlog.model.Article;
import org.wildcodeschool.MyBlog.repository.ImageRepository;
import org.wildcodeschool.MyBlog.repository.ArticleRepository;
import org.wildcodeschool.MyBlog.service.ImageService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController( ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<List<ImageDTO>> getAllImages() {
        List<ImageDTO> images = imageService.getAllImages();
        if (images.isEmpty()) {
            return ResponseEntity.noContent().build();
        };
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable Long id) {
        ImageDTO image = imageService.getImageById(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(image);
    }

    @PostMapping
    public ResponseEntity<ImageDTO> createImage(@RequestBody Image image) {
        ImageDTO savedImage = imageService.createImage(image);
        return ResponseEntity.status(201).body(savedImage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImageDTO> updateImage(@PathVariable Long id, @RequestBody Image imageDetails) {
        ImageDTO image = imageService.updateImage(id, imageDetails);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(image);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        if (imageService.deleteImage(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}