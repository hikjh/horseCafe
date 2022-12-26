package stable.horseCafe.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;
import stable.horseCafe.domain.review.custom.CustomReviewRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {

}
