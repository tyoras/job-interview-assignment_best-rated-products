package com.itravel.api.endpoint

import cats.syntax.all._
import com.itravel.core.ReviewAnalysisService.SearchParams
import com.itravel.core.model.Asin
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._
import io.circe.{Decoder, Encoder}

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalTime, ZoneOffset}

object Payloads {
  implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit class LocalDateOps(val date: LocalDate) extends AnyVal {
    def toUnixTime: Long = date.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC)
  }

  case class SearchParamsPayload(
    start: LocalDate,
    end: LocalDate,
    limit: Int,
    minNumberReviews: Int
  ) {
    lazy val asSearchParams: SearchParams =
      SearchParams(start.toUnixTime, end.toUnixTime, minNumberReviews, limit)
  }

  object SearchParamsPayload {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    implicit val dateDecoder: Decoder[LocalDate] = Decoder.decodeString.emap { s =>
      Either.catchNonFatal(LocalDate.parse(s, dateFormatter)).leftMap(e => s"Failed to decode date $s because ${e.getMessage}")
    }
    implicit val decoder: Decoder[SearchParamsPayload] = deriveConfiguredDecoder
  }

  case class BestRatedPayload(asin: Asin, averageRating: Double)

  object BestRatedPayload {
    implicit val encoder: Encoder[BestRatedPayload] = deriveConfiguredEncoder
  }

}
