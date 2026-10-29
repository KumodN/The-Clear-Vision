package com.example.theclearvision.feature.product.mapper;

import com.example.theclearvision.common.dto.request.ProductCategoryRequest;
import com.example.theclearvision.common.dto.response.ProductCategoryResponse;
import com.example.theclearvision.domain.ProductCategory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-28T11:35:16+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class ProductCategoryMapperImpl implements ProductCategoryMapper {

    @Override
    public ProductCategory toEntity(ProductCategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        ProductCategory productCategory = new ProductCategory();

        productCategory.setName( request.getName() );
        productCategory.setDescription( request.getDescription() );
        productCategory.setSlug( request.getSlug() );
        productCategory.setIsActive( request.getIsActive() );

        return productCategory;
    }

    @Override
    public ProductCategoryResponse toResponse(ProductCategory category) {
        if ( category == null ) {
            return null;
        }

        ProductCategoryResponse productCategoryResponse = new ProductCategoryResponse();

        productCategoryResponse.setParentId( categoryParentId( category ) );
        productCategoryResponse.setParentName( categoryParentName( category ) );
        productCategoryResponse.setId( category.getId() );
        productCategoryResponse.setName( category.getName() );
        productCategoryResponse.setDescription( category.getDescription() );
        productCategoryResponse.setSlug( category.getSlug() );
        productCategoryResponse.setIsActive( category.getIsActive() );
        productCategoryResponse.setCreatedAt( category.getCreatedAt() );
        productCategoryResponse.setUpdatedAt( category.getUpdatedAt() );

        return productCategoryResponse;
    }

    @Override
    public List<ProductCategoryResponse> toResponseList(List<ProductCategory> categories) {
        if ( categories == null ) {
            return null;
        }

        List<ProductCategoryResponse> list = new ArrayList<ProductCategoryResponse>( categories.size() );
        for ( ProductCategory productCategory : categories ) {
            list.add( toResponse( productCategory ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(ProductCategoryRequest request, ProductCategory category) {
        if ( request == null ) {
            return;
        }

        category.setName( request.getName() );
        category.setDescription( request.getDescription() );
        category.setSlug( request.getSlug() );
        category.setIsActive( request.getIsActive() );
    }

    private Long categoryParentId(ProductCategory productCategory) {
        if ( productCategory == null ) {
            return null;
        }
        ProductCategory parent = productCategory.getParent();
        if ( parent == null ) {
            return null;
        }
        Long id = parent.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String categoryParentName(ProductCategory productCategory) {
        if ( productCategory == null ) {
            return null;
        }
        ProductCategory parent = productCategory.getParent();
        if ( parent == null ) {
            return null;
        }
        String name = parent.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
