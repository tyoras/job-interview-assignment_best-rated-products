package com.itravel

import scala.io.Source

package object core {
  def readResourceAsString(rsc: String): String =
    Source.fromResource(rsc).getLines().mkString


  val validJsonReview: String = readResourceAsString("valid_review.json")
  val reviewWithoutAsin: String = readResourceAsString("review_without_asin.json")
  val reviewWithoutReviewerName: String = readResourceAsString("review_without_reviewerName.json")
}
