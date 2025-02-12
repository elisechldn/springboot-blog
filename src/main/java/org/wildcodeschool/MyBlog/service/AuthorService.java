package org.wildcodeschool.MyBlog.service;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.MyBlog.dto.AuthorDTO;
import org.wildcodeschool.MyBlog.exception.ResourceNotFoundException;
import org.wildcodeschool.MyBlog.mapper.AuthorMapper;
import org.wildcodeschool.MyBlog.model.ArticleAuthor;
import org.wildcodeschool.MyBlog.model.Author;
import org.wildcodeschool.MyBlog.repository.ArticleAuthorRepository;
import org.wildcodeschool.MyBlog.repository.ArticleRepository;
import org.wildcodeschool.MyBlog.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final ArticleAuthorRepository articleAuthorRepository;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper, ArticleAuthorRepository articleAuthorRepository) {
        this.authorRepository = authorRepository;
        this.articleAuthorRepository = articleAuthorRepository;
        this.authorMapper = authorMapper;
    }


    public List<AuthorDTO> getAuthor() {
        List<Author> authors = authorRepository.findAll();
        if(authors.isEmpty()) {
            throw new ResourceNotFoundException("Aucun auteur n'a été trouvé.");
        }
        //Uniquement quand il s'agit de liste, autrement conversion dans le return
        return authors.stream()
                .map(authorMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(@PathVariable Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'auteur correspondant à votre recherche est introuvable."));

        return authorMapper.convertToDTO(author);
    }

    public AuthorDTO createAuthor(@RequestBody Author author) {
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.convertToDTO(savedAuthor);
    }

    public AuthorDTO updateAuthor(@RequestBody Long id, Author authorDetails) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'auteur que vous souhaitez mettre à jour est introuvable."));

        Author updatedAuthor = authorRepository.save(authorDetails);
        return authorMapper.convertToDTO(updatedAuthor);
    }

    public boolean deleteAuthor(@PathVariable Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if (author == null) {
            throw new ResourceNotFoundException("L'auteur à supprimer n'a pas été trouvé.");
        }
        articleAuthorRepository.deleteAll(author.getArticleAuthors());
        authorRepository.delete(author);
        return true;
    }

}
