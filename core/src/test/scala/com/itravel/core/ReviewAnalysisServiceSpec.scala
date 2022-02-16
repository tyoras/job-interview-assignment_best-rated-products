package com.itravel.core

import cats.effect.IO
import com.itravel.core.ReviewAnalysisService.SearchParams
import com.itravel.core.ReviewAnalysisServiceSpec.reviews
import com.itravel.core.model.{RatedProduct, Review}
import com.itravel.core.reader.ReviewReader
import fs2.Stream
import fs2.io.file.Path
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ReviewAnalysisServiceSpec extends AnyFlatSpec with Matchers {
  import cats.effect.unsafe.implicits.global

  "Finding best rated products" should "work with a stream of reviews and a time interval that includes all the reviews" in {
    val search   = SearchParams(1262304000, 1609372800, 2, 2)
    val expected = List(RatedProduct("B000JQ0JNS", 4.5), RatedProduct("B000NI7RW8", 3.666666666666666666666666666666667))
    val found    = ReviewAnalysisService[IO].findBestRated(reviews, search).unsafeRunSync()

    (found should contain).theSameElementsInOrderAs(expected)
  }

  it should "work with a stream of reviews and a time interval that does not include all the reviews" in {
    val search   = SearchParams(1262304000, 1409372800, 2, 2)
    val expected = List(RatedProduct("B0002F40AY", 3.5), RatedProduct("B000KFZ32A", 2d))
    val found    = ReviewAnalysisService[IO].findBestRated(reviews, search).unsafeRunSync()

    (found should contain).theSameElementsInOrderAs(expected)
  }

  it should "return an empty list if there are no reviews" in {
    val search = SearchParams(1262304000, 1609372800, 2, 2)
    val found  = ReviewAnalysisService[IO].findBestRated(Stream.empty, search).unsafeRunSync()
    found shouldBe empty
  }

  it should "return an empty list when the minReview search param is higher to the max number of review" in {
    val search = SearchParams(1262304000, 1609372800, minReview = 4, 2)
    val found  = ReviewAnalysisService[IO].findBestRated(reviews, search).unsafeRunSync()
    found shouldBe empty
  }

  it should "return an empty list when the limit search param is lower than 1" in {
    val search = SearchParams(1262304000, 1609372800, 2, limit = 0)
    val found  = ReviewAnalysisService[IO].findBestRated(reviews, search).unsafeRunSync()
    found shouldBe empty

    val search2 = SearchParams(1262304000, 1609372800, 2, limit = -1)
    val found2  = ReviewAnalysisService[IO].findBestRated(reviews, search2).unsafeRunSync()
    found2 shouldBe empty
  }

}

object ReviewAnalysisServiceSpec {
  val reviews: Stream[IO, Review] = ReviewReader.streamFromJsonFile[IO](Path(severalValidReviews))
}
