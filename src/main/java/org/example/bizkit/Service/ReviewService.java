package org.example.bizkit.Service;

import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiException;
import org.example.bizkit.DTO.ReviewInfoDto;
import org.example.bizkit.Model.Client;
import org.example.bizkit.Model.Orders;
import org.example.bizkit.Model.Product;
import org.example.bizkit.Model.Review;
import org.example.bizkit.Repository.ClientRepository;
import org.example.bizkit.Repository.OrderRepository;
import org.example.bizkit.Repository.ProductRepository;
import org.example.bizkit.Repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClientService clientService;
    private final OrderRepository orderRepository;   // avoid circular dependency by using repo instead of OrderService
    private final ProductService productService;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    // ===================== READ (general) =====================
    public List<?> getAll() {
        List<Review> reviews = reviewRepository.findAll();
        ArrayList<ReviewInfoDto> reviewInfoDtos = new ArrayList<>();
        for (Review review : reviews) {
            Client client = clientRepository.findClientById(review.getClientId());
            Product product = productRepository.findProductById(review.getProductId());
            reviewInfoDtos.add(new ReviewInfoDto(client.getName(),product.getName(),
                                                 review.getRating(), review.getComment(),
                                                 review.getCreatedAt(),review.getUpdateAt()));
        }
        return reviewInfoDtos;
    }

    public ReviewInfoDto getReviewById(Integer id) {
        Review review = getReviewByIdAndCheckIfExist(id);

            Client client = clientRepository.findClientById(review.getClientId());
            Product product = productRepository.findProductById(review.getProductId());
            ReviewInfoDto reviewInfoDto =  new ReviewInfoDto(client.getName(),product.getName(),
                    review.getRating(), review.getComment(),
                    review.getCreatedAt(),review.getUpdateAt());

        return reviewInfoDto;
    }

    // ===================== READ (repo methods exposed as services) =====================
    public List<?> getReviewsByClient(Integer clientId) {
        clientService.getClientByIdAndCheckIfExist(clientId);
        List<Review> reviews = reviewRepository.findReviewsByClientId(clientId);
        ArrayList<ReviewInfoDto> reviewInfoDtos = new ArrayList<>();
        for (Review review : reviews) {
            Client client = clientRepository.findClientById(review.getClientId());
            Product product = productRepository.findProductById(review.getProductId());
            reviewInfoDtos.add(new ReviewInfoDto(client.getName(),product.getName(),
                    review.getRating(), review.getComment(),
                    review.getCreatedAt(),review.getUpdateAt()));
        }
        return reviewInfoDtos;
    }

    public List<ReviewInfoDto> getReviewsByOrder(Integer orderId) {
        Orders order = orderRepository.findOrdersById(orderId);
        if (order == null) throw new ApiException("Order not found");
        List<Review> reviews = reviewRepository.findReviewsByOrderId(orderId);
        ArrayList<ReviewInfoDto> reviewInfoDtos = new ArrayList<>();
        for (Review review : reviews) {
            Client client = clientRepository.findClientById(review.getClientId());
            Product product = productRepository.findProductById(review.getProductId());
            reviewInfoDtos.add(new ReviewInfoDto(client.getName(),product.getName(),
                    review.getRating(), review.getComment(),
                    review.getCreatedAt(),review.getUpdateAt()));
        }
        return reviewInfoDtos;
    }

    public List<ReviewInfoDto> getReviewsByProduct(Integer productId) {
        productService.getProductByIdAndCheckIfExist(productId);

        List<Review> reviews = reviewRepository.findReviewsByProductId(productId);
        ArrayList<ReviewInfoDto> reviewInfoDtos = new ArrayList<>();
        for (Review review : reviews) {
            Client client = clientRepository.findClientById(review.getClientId());
            Product product = productRepository.findProductById(review.getProductId());
            reviewInfoDtos.add(new ReviewInfoDto(client.getName(),product.getName(),
                    review.getRating(), review.getComment(),
                    review.getCreatedAt(),review.getUpdateAt()));
        }
        return reviewInfoDtos;
    }

    public List<ReviewInfoDto> getReviewsByRatingAtLeast(Integer rating) {
        // adjust bounds if your rating scale is different
        if (rating == null || rating < 1 || rating > 5) {
            throw new ApiException("Rating must be between 1 and 5");
        }
        List<Review> reviews = reviewRepository.retrieveReviewByRatingMoreThanOrEqual(rating);
        ArrayList<ReviewInfoDto> reviewInfoDtos = new ArrayList<>();
        for (Review review : reviews) {
            Client client = clientRepository.findClientById(review.getClientId());
            Product product = productRepository.findProductById(review.getProductId());
            reviewInfoDtos.add(new ReviewInfoDto(client.getName(),product.getName(),
                    review.getRating(), review.getComment(),
                    review.getCreatedAt(),review.getUpdateAt()));
        }
        return reviewInfoDtos;
    }

    // ===================== CREATE =====================
    public void addReview(Review review) {
        // 1) validate client
        clientService.getClientByIdAndCheckIfExist(review.getClientId());

        // 2) validate order & ownership (the order must belong to the same client)
        Orders order = orderRepository.findOrdersById(review.getOrderId());
        if (order == null) throw new ApiException("Order not found");
        if (!order.getClientId().equals(review.getClientId())) {
            throw new ApiException("This order does not belong to the client");
        }

        // 3) validate product id on the review
        productService.getProductByIdAndCheckIfExist(review.getProductId());


        // 4) save review
        reviewRepository.save(review);
    }

    // ===================== UPDATE =====================
    public void updateReview(Integer reviewId, Review update, Errors errors) {
        if (errors != null && errors.hasErrors()) {
            throw new ApiException(errors.getFieldError().getDefaultMessage());
        }
        Review existing = getReviewByIdAndCheckIfExist(reviewId);

        // update allowed fields
        existing.setComment(update.getComment());
        existing.setRating(update.getRating());
        // If you allow changing productId/orderId, validate them first then set here.

        reviewRepository.save(existing);
    }

    // ===================== DELETE =====================
    public void deleteReview(Integer reviewId) {
        Review r = getReviewByIdAndCheckIfExist(reviewId);
        reviewRepository.delete(r);
    }

    // ===================== HELPERS =====================
    protected Review getReviewByIdAndCheckIfExist(Integer id) {
        Review review = reviewRepository.findReviewById(id);
        if (review == null) {
            throw new ApiException("Review Not Found");
        }
        return review;
    }
}
