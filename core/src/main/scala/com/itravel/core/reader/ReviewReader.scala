package com.itravel.core.reader

import com.itravel.core.model.Review
import fs2.io.file.{Files, Path}
import fs2.{Pipe, Stream}
import io.circe.Decoder
import io.circe.parser.decode

import java.nio.file.{Path => JPath}

trait ReviewReader[F[_]] {
  def stream: Stream[F, Review]
}

object ReviewReader {

  /** Read reviews from a newline delimited JSON file with each line containing one review formatted as a JSON object
    * @param path
    *   Path of the newline delimited json file
    * @tparam F
    *   effect
    * @return
    *   Json file review reader
    */
  def fromJsonFile[F[_] : Files](path: JPath): ReviewReader[F] = new ReviewReader[F] {
    override def stream: Stream[F, Review] =
      readLinesFromFile(Path.fromNioPath(path)).through(jsonDecode[F, Review])
  }

  private def jsonDecode[F[_], A : Decoder]: Pipe[F, String, A] =
    _.map(decode[A](_).toOption).unNone

  private def readLinesFromFile[F[_] : Files](path: Path): Stream[F, String] =
    Files[F]
      .readAll(path)
      .through(fs2.text.utf8.decode)
      .through(fs2.text.lines)

}
