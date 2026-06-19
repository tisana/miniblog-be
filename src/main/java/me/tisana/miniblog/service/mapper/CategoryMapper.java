package me.tisana.miniblog.service.mapper;

import me.tisana.miniblog.domain.Category;
import me.tisana.miniblog.service.dto.CategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {}
