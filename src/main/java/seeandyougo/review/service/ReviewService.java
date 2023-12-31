package seeandyougo.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seeandyougo.review.domain.Menu;
import seeandyougo.review.domain.Restaurant;
import seeandyougo.review.domain.Review;
import seeandyougo.review.repository.RestaurantRepository;
import seeandyougo.review.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Long registerReview(Review review, String restaurantName) {
        Restaurant restaurant = restaurantRepository.findByName(restaurantName);
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant not found for name: " + restaurantName);
        }

        review.setRestaurant(restaurant);

        reviewRepository.save(review);
        return review.getId();
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findOne(reviewId);
        if (review != null) {
            reviewRepository.delete(review);
        }
    }


    public Review findOne(Long id) {
        return reviewRepository.findOne(id);
    }

    public List<Review> findAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> findReviewsByWriter(String writer) {
        return reviewRepository.findByWriter(writer);
    }

    public List<Review> findReviewsByMenu(Menu menu) {
        return reviewRepository.findByMenu(menu);
    }

    public List<Review> findReviewsWithRatingAbove(Double reviewRate) {
        return reviewRepository.findByReviewRateGreaterThan(reviewRate);
    }

    public List<Review> findAllReviewsSortedByLikes() {
        return reviewRepository.findAllOrderByLikeCountDesc();
    }

    public List<Review> findReviewsAfterTime(LocalDateTime time) {
        return reviewRepository.findByMadeTimeAfter(time);
    }

    public List<Review> findReviewsWithImageLink() {
        return reviewRepository.findByImgLinkIsNotNull();
    }

    @Transactional
    public void increaseLikeCount(Long reviewId) {
        Review review = reviewRepository.findOne(reviewId);
        if (review == null) {
            throw new IllegalArgumentException("Review not found for ID: " + reviewId);
        }
        review.setLikeCount(review.getLikeCount() + 1);
    }

    @Transactional
    public void updateReviewContent(Long reviewId, String newContent) {
        Review review = reviewRepository.findOne(reviewId);
        if (review == null) {
            throw new IllegalArgumentException("Review not found for ID: " + reviewId);
        }
        review.setComment(newContent);
    }

    public List<Review> findTopReviewsByRestaurant(String name) {
        return reviewRepository.findTopReviewsByRestaurant(name);
    }

    public void delete(Review review) {
        reviewRepository.delete(review);
    }

    public List<Review> findRestaurantReviews(String name) {
        return reviewRepository.getRestaurantReviews(name);
    }

}