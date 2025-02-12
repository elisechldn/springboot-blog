package org.wildcodeschool.MyBlog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.MyBlog.dto.ImageDTO;
import org.wildcodeschool.MyBlog.mapper.ImageMapper;
import org.wildcodeschool.MyBlog.model.Image;
import org.wildcodeschool.MyBlog.repository.ImageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public ImageService(ImageRepository imageRepository, ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.imageMapper = new ImageMapper();
    }

    public List<ImageDTO> getAllImages() {
        List<Image> images = imageRepository.findAll();
        if (images.isEmpty()) {
            return null;
        }
        return images.stream()
                .map(imageMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public ImageDTO getImageById(@PathVariable Long id) {
        Image image = imageRepository.findById(id).orElse(null);
        if (image == null) {
            return null;
        }
        return imageMapper.convertToDTO(image);
    }

    public ImageDTO createImage(@RequestBody Image image) {
        Image savedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(savedImage);
    }

    @PutMapping("/{id}")
    public ImageDTO updateImage(@PathVariable Long id, @RequestBody Image imageDetails) {
        Image image = imageRepository.findById(id).orElse(null);
        if (image == null) {
            return null;
        }
        image.setUrl(imageDetails.getUrl());
        Image updatedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(updatedImage);
    }

    public boolean deleteImage(@PathVariable Long id) {
        Image image = imageRepository.findById(id).orElse(null);
        if (image == null) {
            return false;
        }
        imageRepository.delete(image);
        return true;
    }
}
