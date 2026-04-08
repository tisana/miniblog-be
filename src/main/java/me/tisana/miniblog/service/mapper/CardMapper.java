package me.tisana.miniblog.service.mapper;

import me.tisana.miniblog.domain.Author;
import me.tisana.miniblog.domain.Card;
import me.tisana.miniblog.domain.Category;
import me.tisana.miniblog.service.dto.AuthorDTO;
import me.tisana.miniblog.service.dto.CardDTO;
import me.tisana.miniblog.service.dto.CategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Card} and its DTO {@link CardDTO}.
 */
@Mapper(componentModel = "spring")
public interface CardMapper extends EntityMapper<CardDTO, Card> {
    @Mapping(target = "author", source = "author", qualifiedByName = "authorUsername")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryName")
    CardDTO toDto(Card s);

    @Named("authorUsername")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    AuthorDTO toDtoAuthorUsername(Author author);

    @Named("categoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryDTO toDtoCategoryName(Category category);
}
