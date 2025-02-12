package org.wildcodeschool.MyBlog.mapper;

import org.springframework.stereotype.Component;
import org.wildcodeschool.MyBlog.dto.AuthorDTO;
import org.wildcodeschool.MyBlog.model.Author;

@Component
public class AuthorMapper {
    public AuthorDTO convertToDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setFirstname(author.getFirstname());
        authorDTO.setLastname(author.getLastname());

        return authorDTO;
    }
}
