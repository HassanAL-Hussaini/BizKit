package org.example.bizkit.Repository;

import org.example.bizkit.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
    Review findReviewById(Integer id);
    List<Review> findReviewsByClientId(Integer id);
    Review findReviewsByOrderId(Integer id);
    List<Review> findReviewsByProductId(Integer id);

    @Query("select r from Review r where r.rating >= ?1")//
    List<Review> retrieveReviewByRatingMoreThanOrEqual(Integer ratingNumber);

}
