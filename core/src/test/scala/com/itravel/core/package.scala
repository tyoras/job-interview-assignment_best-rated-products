package com.itravel

import scala.io.Source

package object core {
  def readResourceAsString(rsc: String): String =
    Source.fromResource(rsc).getLines().mkString

  def resourcePath(rsc: String): String =
    getClass.getClassLoader.getResource(rsc).getPath

  val validJsonReview: String           = readResourceAsString("valid_review.json")
  val reviewWithoutAsin: String         = readResourceAsString("review_without_asin.json")
  val reviewWithoutReviewerName: String = readResourceAsString("review_without_reviewerName.json")

  val oneValidJsonReview: String            = resourcePath("one_valid_review.json")
  val invalidJsonReview: String             = resourcePath("review_without_asin.json")
  val severalValidReviews: String           = resourcePath("several_valid_reviews.json")
  val severalValidOneInvalidReviews: String = resourcePath("several_valid_one_invalid_reviews.json")
}
