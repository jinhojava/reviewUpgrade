package seeandyougo.review.contoroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seeandyougo.review.domain.Review;
import seeandyougo.review.dto.ReviewRequestDto;
import seeandyougo.review.service.ReviewService;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 탑 리뷰5개 조회
    @GetMapping("/top5Review/restaurant{number}")
    public ResponseEntity<List<Review>> getTopReviews(@PathVariable String number) {
        List<Review> reviews = reviewService.findTopReviewsByRestaurant("restaurant"+number);
        return ResponseEntity.ok(reviews);
    }

    //각 식당 전체 리뷰 조회
    @GetMapping("/review/restaurant{number}")
    public List<Review> getRestaurantReviews(@PathVariable String number) {
       return reviewService.findRestaurantReviews("restaurant"+number);
    }


    //전체식당 통합 리뷰조회
    @GetMapping("/totalReview")
    public List<Review> getAllReviews() {
        return reviewService.findAllReviews();
    }

    // 리뷰 게시
    @PostMapping("/review")
    public ResponseEntity<Long> postReview(@RequestBody ReviewRequestDto requestDto) {
        Review review = new Review();
        review.setWriter(requestDto.getWriter());
        review.setReviewRate(requestDto.getRate());
        review.setComment(requestDto.getComment());
        review.setImgLink(requestDto.getImage());

        review.setMadeTime(requestDto.getMadeTime()); // 문자열 형태의 madeTime을 그대로 전달

        Long reviewId = reviewService.registerReview(review, requestDto.getRestaurant());

        return new ResponseEntity<>(reviewId, HttpStatus.CREATED);
    }



    // 리뷰 삭제
    @DeleteMapping("/del/review/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        Review review = reviewService.findOne(reviewId);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        reviewService.delete(review);
        return ResponseEntity.ok("Review deleted successfully.");
    }
}

