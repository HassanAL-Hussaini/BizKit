package org.example.bizkit.Service;

import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiException;
import org.example.bizkit.Model.Product;
import org.example.bizkit.Model.Provider;
import org.example.bizkit.Repository.ProductRepository;
import org.example.bizkit.Repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProviderService providerService;
    private final ProviderRepository providerRepository;
    // ===================== READ =====================
    // Get all products (public)
    public List<?> getAllProducts() {
        return productRepository.findAll();
    }
    public List<?> getProductsByProviderId(Integer providerId) {
        //products for specfic company/provider\
        Provider provider = providerRepository.findProviderById(providerId);
        if(provider == null) {
            throw new ApiException("Provider not found");
        }
        return productRepository.findProductsByProviderId(providerId);
    }
    // ===================== CREATE =====================
    // Create a new product
    public void addProduct(Product product) {
        Provider provider = providerRepository.findProviderById(product.getProviderId());
        //TODO you should use FK instead of doing relation manually
        if(provider == null) {
            throw new ApiException("Provider not found");
        }
        if(provider.getIsActive() ==  false) {
            throw new ApiException("Provider not allowed to publish his products Contact technical support");
        }
        productRepository.save(product);
    }

    // ===================== UPDATE =====================
    // Update an existing product by id
    public void updateProduct(Integer productIdUpdated, Product newProduct) {
        Product oldProduct = getProductByIdAndCheckIfExist(productIdUpdated);
        oldProduct.setName(newProduct.getName());
        oldProduct.setDescription(newProduct.getDescription());
        oldProduct.setPrice(newProduct.getPrice());
        oldProduct.setStockQuantity(newProduct.getStockQuantity());
        productRepository.save(oldProduct);
    }

    // ===================== DELETE =====================
    // Delete a product by id
    public void deleteProduct(Integer productIdDeleted) {
        Product productDeleted = productRepository.findProductById(productIdDeleted);
        if (productDeleted == null) {
            throw new ApiException("the Product Not Found");
        }
        productRepository.delete(productDeleted);
    }

    // ===================== INTERNAL HELPERS =====================
    // Fetch product or throw if not found
    protected Product getProductByIdAndCheckIfExist(Integer id) {
        Product product = productRepository.findProductById(id);
        if (product == null) {
            throw new ApiException("the Product Not Found");
        }
        return product;
    }
}
