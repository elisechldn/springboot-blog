package org.wildcodeschool.MyBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.MyBlog.dto.AuthorDTO;
import org.wildcodeschool.MyBlog.model.Author;
import org.wildcodeschool.MyBlog.service.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAuthor() {
        List<AuthorDTO> authors = authorService.getAuthor();
        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/id")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        AuthorDTO author = authorService.getAuthorById(id);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody Author author) {
        AuthorDTO savedAuthor = authorService.createAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }

    @PutMapping("/id")
    public ResponseEntity<AuthorDTO> updateAuthor(@RequestBody Long id, Author authorDetails) {
        AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDetails);
        if (updatedAuthor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        if(authorService.deleteAuthor(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
