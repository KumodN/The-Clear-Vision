package com.example.theclearvision.feature.product.mapper;

import com.example.theclearvision.common.dto.request.ProductCategoryRequest;
import com.example.theclearvision.common.dto.response.ProductCategoryResponse;
import com.example.theclearvision.domain.ProductCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductCategory toEntity(ProductCategoryRequest request);

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    ProductCategoryResponse toResponse(ProductCategory category);

    List<ProductCategoryResponse> toResponseList(List<ProductCategory> categories);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(ProductCategoryRequest request, @MappingTarget ProductCategory category);
}
