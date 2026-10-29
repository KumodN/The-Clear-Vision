package com.example.theclearvision.feature.product.mapper;

import com.example.theclearvision.common.dto.response.ProductImageResponse;
import com.example.theclearvision.common.dto.response.ProductVariantResponse;
import com.example.theclearvision.domain.Product;
import com.example.theclearvision.domain.ProductImage;
import com.example.theclearvision.domain.ProductVariant;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-28T11:35:17+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class ProductVariantMapperImpl implements ProductVariantMapper {

    @Override
    public ProductVariantResponse toResponse(ProductVariant variant) {
        if ( variant == null ) {
            return null;
        }

        ProductVariantResponse productVariantResponse = new ProductVariantResponse();

        productVariantResponse.setProductId( variantProductId( variant ) );
        productVariantResponse.setId( variant.getId() );
        productVariantResponse.setVariantName( variant.getVariantName() );
        productVariantResponse.setFrameColor( variant.getFrameColor() );
        productVariantResponse.setFrameSize( variant.getFrameSize() );
        productVariantResponse.setFrameMaterial( variant.getFrameMaterial() );
        productVariantResponse.setPrice( variant.getPrice() );
        productVariantResponse.setStockQuantity( variant.getStockQuantity() );
        productVariantResponse.setSkuSuffix( variant.getSkuSuffix() );
        productVariantResponse.setIsDefault( variant.getIsDefault() );
        productVariantResponse.setCreatedAt( variant.getCreatedAt() );
        productVariantResponse.setUpdatedAt( variant.getUpdatedAt() );
        productVariantResponse.setImages( productImageListToProductImageResponseList( variant.getImages() ) );

        return productVariantResponse;
    }

    @Override
    public List<ProductVariantResponse> toResponseList(List<ProductVariant> variants) {
        if ( variants == null ) {
            return null;
        }

        List<ProductVariantResponse> list = new ArrayList<ProductVariantResponse>( variants.size() );
        for ( ProductVariant productVariant : variants ) {
            list.add( toResponse( productVariant ) );
        }

        return list;
    }

    private String variantProductId(ProductVariant productVariant) {
        if ( productVariant == null ) {
            return null;
        }
        Product product = productVariant.getProduct();
        if ( product == null ) {
            return null;
        }
        String id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected ProductImageResponse productImageToProductImageResponse(ProductImage productImage) {
        if ( productImage == null ) {
            return null;
        }

        ProductImageResponse productImageResponse = new ProductImageResponse();

        productImageResponse.setId( productImage.getId() );
        productImageResponse.setImageUrl( productImage.getImageUrl() );
        productImageResponse.setAltText( productImage.getAltText() );
        productImageResponse.setDisplayOrder( productImage.getDisplayOrder() );
        productImageResponse.setIsPrimary( productImage.getIsPrimary() );
        productImageResponse.setCreatedAt( productImage.getCreatedAt() );

        return productImageResponse;
    }

    protected List<ProductImageResponse> productImageListToProductImageResponseList(List<ProductImage> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductImageResponse> list1 = new ArrayList<ProductImageResponse>( list.size() );
        for ( ProductImage productImage : list ) {
            list1.add( productImageToProductImageResponse( productImage ) );
        }

        return list1;
    }
}
