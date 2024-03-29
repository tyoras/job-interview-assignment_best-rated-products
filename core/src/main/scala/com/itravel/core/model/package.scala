package com.itravel.core

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

package object model {
  type Asin = String

  /** Decode only the relevant field from a review json representation
    */
  implicit val reviewDecoder: Decoder[Review] = deriveDecoder
}
