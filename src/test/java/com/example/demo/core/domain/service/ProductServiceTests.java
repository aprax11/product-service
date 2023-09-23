package com.example.demo.core.domain.service;

import com.example.demo.core.domain.model.Product;
import com.example.demo.core.domain.service.impl.ProductService;
import com.example.demo.core.domain.service.interfaces.IProductRepository;
import com.example.demo.exception.ProductDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {
    @Mock
    private IProductRepository productRepository;
    @InjectMocks
    private ProductService productService;
    private final Product product = new Product(
            UUID.randomUUID(),
            "Ring",
            "Das ist ein Ring.",
            "22",
            "details",
            "link"
    );
    private final String DELETE_RESPONSE = "deleting product";
    @Test
    void createProductTest(){
        Product ret = productService.createProduct(this.product);

        verify(productRepository).save(this.product);

        assertEquals(this.product, ret);
    }
    @Test
    void updateProductExceptionTest(){
        try{
            productService.updateProduct(this.product);
            fail("Exception was not thrown");
        }catch (ProductDoesNotExistException e){
            verify(productRepository).existsById(this.product.getId());
        }
    }
    @Test
    void updateProductTest(){
        when(productRepository.existsById(any(UUID.class))).thenReturn(true);
        try{
            Product ret = productService.updateProduct(this.product);

            verify(productRepository).save(this.product);

            assertEquals(this.product, ret);

        }catch (ProductDoesNotExistException e){
            fail("Exception was thrown");
        }
    }
    @Test
    void deleteProductExceptionTest(){
        try{
            productService.deleteProduct(this.product.getId());
            fail("Exception was not thrown");
        }catch (ProductDoesNotExistException e){
            verify(productRepository).existsById(this.product.getId());
        }
    }
    @Test
    void deleteProductTest(){
        when(productRepository.existsById(any(UUID.class))).thenReturn(true);
        try{
            String ret = productService.deleteProduct(this.product.getId());

            verify(productRepository).deleteById(this.product.getId());

            assertEquals(DELETE_RESPONSE, ret);

        }catch (ProductDoesNotExistException e){
            fail("Exception was thrown");
        }
    }
    @Test
    void getAllProductsTest(){
        productService.getAllProducts();

        verify(productRepository).findAll();
    }

}
