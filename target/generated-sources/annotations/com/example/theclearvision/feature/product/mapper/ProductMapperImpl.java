package com.example.theclearvision.feature.product.mapper;

import com.example.theclearvision.common.dto.request.ProductRequest;
import com.example.theclearvision.common.dto.response.ProductResponse;
import com.example.theclearvision.domain.Product;
import com.example.theclearvision.domain.ProductCategory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-28T11:35:17+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Autowired
    private ProductVariantMapper productVariantMapper;

    @Override
    public Product toEntity(ProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product product = new Product();

        product.setSku( request.getSku() );
        product.setTitle( request.getTitle() );
        product.setSlug( request.getSlug() );
        product.setDescription( request.getDescription() );
        product.setGender( request.getGender() );
        product.setMaterial( request.getMaterial() );
        product.setBasePrice( request.getBasePrice() );
        product.setIsActive( request.getIsActive() );

        return product;
    }

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse productResponse = new ProductResponse();

        productResponse.setCategoryId( productCategoryId( product ) );
        productResponse.setCategoryName( productCategoryName( product ) );
        productResponse.setId( product.getId() );
        productResponse.setSku( product.getSku() );
        productResponse.setTitle( product.getTitle() );
        productResponse.setSlug( product.getSlug() );
        productResponse.setDescription( product.getDescription() );
        productResponse.setGender( product.getGender() );
        productResponse.setMaterial( product.getMaterial() );
        productResponse.setBasePrice( product.getBasePrice() );
        productResponse.setIsActive( product.getIsActive() );
        productResponse.setCreatedAt( product.getCreatedAt() );
        productResponse.setUpdatedAt( product.getUpdatedAt() );
        productResponse.setVariants( productVariantMapper.toResponseList( product.getVariants() ) );

        productResponse.setPrimaryImageUrl( getPrimaryImageUrl(product) );

        return productResponse;
    }

    @Override
    public List<ProductResponse> toResponseList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductResponse> list = new ArrayList<ProductResponse>( products.size() );
        for ( Product product : products ) {
            list.add( toResponse( product ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(ProductRequest request, Product product) {
        if ( request == null ) {
            return;
        }

        product.setSku( request.getSku() );
        product.setTitle( request.getTitle() );
        product.setSlug( request.getSlug() );
        product.setDescription( request.getDescription() );
        product.setGender( request.getGender() );
        product.setMaterial( request.getMaterial() );
        product.setBasePrice( request.getBasePrice() );
        product.setIsActive( request.getIsActive() );
    }

    private Long productCategoryId(Product product) {
        if ( product == null ) {
            return null;
        }
        ProductCategory category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        Long id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String productCategoryName(Product product) {
        if ( product == null ) {
            return null;
        }
        ProductCategory category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        String name = category.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
