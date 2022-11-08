package dev.dipesh.jwtcodejavaapp.productapi;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductApi {
    @Autowired
    ProductRepository repository;

    @GetMapping
    public List<Product> listAll(){
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product){
        Product savedProduct = repository.save(product);
        URI productUri = URI.create("/product/" + savedProduct.getId());
        return ResponseEntity.created(productUri).body(savedProduct);
    }
}
