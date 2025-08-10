package org.example.bizkit.Controller;

import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiException;
import org.example.bizkit.Api.ApiResponse;
import org.example.bizkit.Model.Review;
import org.example.bizkit.Service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ========= READ =========

    // Get all reviews
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(reviewService.getAll());
    }

    // Get review by id
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    // Get reviews by client id
    @GetMapping("/get-by-client/{clientId}")
    public ResponseEntity<?> getByClient(@PathVariable Integer clientId) {
        return ResponseEntity.ok(reviewService.getReviewsByClient(clientId));
    }

    // Get reviews by order id
    @GetMapping("/get-by-order/{orderId}")
    public ResponseEntity<?> getByOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(reviewService.getReviewsByOrder(orderId));
    }

    // Get reviews by product id
    @GetMapping("/get-by-product/{productId}")
    public ResponseEntity<?> getByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
    }

    // Get reviews with rating >= min
    @GetMapping("/rating/{number}")
    public ResponseEntity<?> getByRatingAtLeast(@PathVariable Integer number) {
        return ResponseEntity.ok(reviewService.getReviewsByRatingAtLeast(number));
    }

    // ========= CREATE =========

    // Create a new review
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@Valid @RequestBody Review review , Errors errors) {
        if(errors.hasErrors()){
            throw new ApiException(errors.getFieldError().getDefaultMessage());
        }
        reviewService.addReview(review);
        // location is optional since service doesn't return the saved id
        return ResponseEntity.status(200).body(new ApiResponse("Review created successfully"));
    }

    // ========= UPDATE =========

    // Update a review (comment/rating)
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Integer id,
                                              @Valid @RequestBody Review update,
                                              Errors errors) {
        reviewService.updateReview(id, update, errors);
        return ResponseEntity.ok(new ApiResponse("Review updated successfully"));
    }

    // ========= DELETE =========

    // Delete a review by id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Integer id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(new ApiResponse("Review deleted successfully"));
    }
}

