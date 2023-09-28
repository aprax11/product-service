package com.example.demo.core.domain.service;

import com.example.demo.core.domain.model.Product;
import com.example.demo.core.domain.service.impl.ProductService;
import com.example.demo.core.domain.service.interfaces.IProductRepository;
import com.example.demo.exception.ProductDoesNotExistException;
import com.example.demo.port.producer.interfaces.IBasketServiceProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    @Mock
    private IBasketServiceProducer basketServiceProducer;
    @InjectMocks
    private ProductService productService;
    private final Product TEST_PRODUCT = new Product(
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
        Product productWithoutID = new Product(
                null,
                "Ring",
                "Das ist ein Ring.",
                "22",
                "details",
                "link"
        );
        Product ret = productService.createProduct(productWithoutID);


        ArgumentCaptor<Product> argumentCaptor1 = ArgumentCaptor.forClass(Product.class);
        ArgumentCaptor<Product> argumentCaptor2 = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(argumentCaptor1.capture());
        verify(basketServiceProducer).sendCreateProductRequest(argumentCaptor2.capture());

        Product capturedProduct1 = argumentCaptor1.getValue();
        Product capturedProduct2 = argumentCaptor2.getValue();


        assertEquals(capturedProduct1, ret);
        assertEquals(capturedProduct1.getId().getClass(), UUID.class);
        assertEquals(capturedProduct2, ret);
        assertEquals(capturedProduct2.getId().getClass(), UUID.class);
    }
    @Test
    void updateProductExceptionTest(){
        try{
            productService.updateProduct(this.TEST_PRODUCT);
            fail("Exception was not thrown");
        }catch (ProductDoesNotExistException e){
            verify(productRepository).existsById(this.TEST_PRODUCT.getId());
        }
    }
    @Test
    void updateProductTest(){
        when(productRepository.existsById(any(UUID.class))).thenReturn(true);
        try{
            Product ret = productService.updateProduct(this.TEST_PRODUCT);

            verify(productRepository).save(this.TEST_PRODUCT);
            verify(basketServiceProducer).sendUpdateProductMessage(this.TEST_PRODUCT);

            assertEquals(this.TEST_PRODUCT, ret);

        }catch (ProductDoesNotExistException e){
            fail("Exception was thrown");
        }
    }
    @Test
    void deleteProductExceptionTest(){
        try{
            productService.deleteProduct(this.TEST_PRODUCT.getId());
            fail("Exception was not thrown");
        }catch (ProductDoesNotExistException e){
            verify(productRepository).existsById(this.TEST_PRODUCT.getId());
        }
    }
    @Test
    void deleteProductTest(){
        when(productRepository.existsById(any(UUID.class))).thenReturn(true);
        try{
            String ret = productService.deleteProduct(this.TEST_PRODUCT.getId());

            verify(productRepository).deleteById(this.TEST_PRODUCT.getId());
            verify(basketServiceProducer).sendDeleteProductMessage(this.TEST_PRODUCT.getId().toString());

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
