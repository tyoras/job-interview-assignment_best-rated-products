package com.itravel.core.model

/** Represent a review with only the relevant fields
  *
  * @param asin
  *   ID of the product
  * @param overall
  *   Rating of the product (1 to 5 stars)
  * @param unixReviewTime
  *   Time of the review (unix time)
  */
case class Review(
  asin: Asin,
  overall: Double,
  unixReviewTime: Long
)

/** Represent a product rating
  * @param asin
  *   ID of the rated product
  * @param averageRating
  *   Average rating of the product
  */
case class RatedProduct(
  asin: Asin,
  averageRating: Double
)
