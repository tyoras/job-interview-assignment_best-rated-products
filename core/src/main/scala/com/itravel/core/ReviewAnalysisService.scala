package com.itravel.core

import cats.effect.Sync
import com.itravel.core.ReviewAnalysisService.SearchParams
import com.itravel.core.model.{Asin, RatedProduct, Review}
import fs2.Stream

trait ReviewAnalysisService[F[_]] {
  def findBestRated(reviews: Stream[F, Review], searchParams: SearchParams): F[List[RatedProduct]]
}

object ReviewAnalysisService {

  case class SearchParams(start: Long, end: Long, minReview: Int, limit: Int)

  def apply[F[_] : Sync]: ReviewAnalysisService[F] = new ReviewAnalysisService[F] {
    override def findBestRated(reviews: Stream[F, Review], searchParams: SearchParams): F[List[RatedProduct]] =
      reviews
        .filter(timeRangeFilter(searchParams))
        .fold(Map.empty[Asin, (Double, Long)])(registerProducts)
        .map(selectBestProducts(_, searchParams))
        .compile
        .lastOrError

    private def timeRangeFilter(searchParams: SearchParams)(review: Review): Boolean =
      review.unixReviewTime >= searchParams.start && review.unixReviewTime <= searchParams.end

    private def registerProducts(productsRegister: Map[Asin, (Double, Long)], review: Review): Map[Asin, (Double, Long)] = {
      val info = productsRegister.get(review.asin).fold(review.overall -> 1L) { case (ratingSum, count) =>
        (ratingSum + review.overall) -> (count + 1L)
      }
      val productEntry = review.asin -> info
      productsRegister + productEntry
    }

    private def selectBestProducts(productRegister: Map[Asin, (Double, Long)], searchParams: SearchParams): List[RatedProduct] =
      productRegister
        .collect {
          case asin -> (ratingSum -> count) if count >= searchParams.minReview =>
            RatedProduct(asin, averageRating = ratingSum / count)
        }
        .toList
        .sortBy(_.averageRating)(Ordering[Double].reverse)
        .take(searchParams.limit)
  }

}
