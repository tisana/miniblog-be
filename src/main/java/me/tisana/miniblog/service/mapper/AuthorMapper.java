package me.tisana.miniblog.service.mapper;

import me.tisana.miniblog.domain.Author;
import me.tisana.miniblog.service.dto.AuthorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Author} and its DTO {@link AuthorDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {}
