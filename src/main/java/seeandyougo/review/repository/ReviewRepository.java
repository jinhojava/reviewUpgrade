package seeandyougo.review.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import seeandyougo.review.domain.Menu;
import seeandyougo.review.domain.Review;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {
    @PersistenceContext
    private final EntityManager em;

    public void save(Review review) {
        em.persist(review);
    }

    public Review findOne(Long id) {
        return em.find(Review.class, id);
    }

    public List<Review> findAll() {
        return em.createQuery("select  r from Review r", Review.class)
                .getResultList();
    }

    public List<Review> getRestaurantReviews(String name) {
        return em.createQuery("SELECT r FROM Review r WHERE r.restaurant.name = :restaurantName", Review.class)
                .setParameter("restaurantName", name)
                .getResultList();
    }

    public List<Review> findByWriter(String writer) {
        return em.createQuery("select r from Review r where r.writer = :writer", Review.class)
                .setParameter("writer", writer)
                .getResultList();
    }

    public List<Review> findByMenu(Menu menu) {
        return em.createQuery("select r from Review r where r.menu = :menu", Review.class)
                .setParameter("menu", menu)
                .getResultList();
    }

    /**
     * 특정 평점 이상의 리뷰 검색
     */
    public List<Review> findByReviewRateGreaterThan(Double reviewRate) {
        return em.createQuery("select r from Review r where r.reviewRate >= :reviewRate", Review.class)
                .setParameter("reviewRate", reviewRate)
                .getResultList();
    }

    /**
     * 좋아요 개수 순으로 리뷰 정렬해서 검색
     */
    public List<Review> findAllOrderByLikeCountDesc() {
        return em.createQuery("select r from Review r order by r.likeCount desc", Review.class)
                .getResultList();
    }

    /**
     * 특정 시간 이후에 작성된 리뷰 검색
     */
    public List<Review> findByMadeTimeAfter(LocalDateTime time) {
        return em.createQuery("select r from Review r where r.madeTime > :time", Review.class)
                .setParameter("time", time)
                .getResultList();
    }

    /**
     * 이미지 링크가 있는 리뷰만 검색
     */
    public List<Review> findByImgLinkIsNotNull() {
        return em.createQuery("select r from Review r where r.imgLink is not null", Review.class)
                .getResultList();
    }

    public void delete(Review review) {
        em.remove(review);
    }

    public List<Review> findTopReviewsByRestaurant(String name) {
        LocalDateTime startDate = LocalDate.now().atStartOfDay();
        LocalDateTime endDate = LocalDate.now().atTime(23, 59, 59);

        return em.createQuery("select r from Review r " +
                        "where r.restaurant.name = :restaurantName " +
                        "and r.madeTime between :startDate " +
                        "and :endDate order by r.madeTime desc", Review.class)
                .setParameter("restaurantName", name)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setMaxResults(5)
                .getResultList();
    }
}
