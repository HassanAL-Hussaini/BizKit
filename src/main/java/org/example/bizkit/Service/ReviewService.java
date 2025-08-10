package org.example.bizkit.Service;

import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiException;
import org.example.bizkit.Model.Orders;
import org.example.bizkit.Model.Review;
import org.example.bizkit.Repository.OrderRepository;
import org.example.bizkit.Repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClientService clientService;
    private final OrderRepository orderRepository;   // avoid circular dependency by using repo instead of OrderService
    private final ProductService productService;

    // ===================== READ (general) =====================
    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Integer id) {
        return getReviewByIdAndCheckIfExist(id);
    }

    // ===================== READ (repo methods exposed as services) =====================
    public List<Review> getReviewsByClient(Integer clientId) {
        clientService.getClientByIdAndCheckIfExist(clientId);
        return reviewRepository.findReviewsByClientId(clientId);
    }

    public Review getReviewsByOrder(Integer orderId) {
        Orders order = orderRepository.findOrdersById(orderId);
        if (order == null) throw new ApiException("Order not found");
        return reviewRepository.findReviewsByOrderId(orderId);
    }

    public List<Review> getReviewsByProduct(Integer productId) {
        productService.getProductByIdAndCheckIfExist(productId);
        return reviewRepository.findReviewsByProductId(productId);
    }

    public List<Review> getReviewsByRatingAtLeast(Integer rating) {
        // adjust bounds if your rating scale is different
        if (rating == null || rating < 1 || rating > 5) {
            throw new ApiException("Rating must be between 1 and 5");
        }
        return reviewRepository.retrieveReviewByRatingMoreThanOrEqual(rating);
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
