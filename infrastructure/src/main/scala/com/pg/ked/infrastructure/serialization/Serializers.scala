package com.pg.ked.infrastructure.serialization

import java.time.Instant

import org.json4s.ext.JavaTypesSerializers
import org.json4s.{CustomSerializer, DefaultFormats, Formats, JLong}
import org.json4s.jackson.Serialization

trait RestSerializer {
  protected implicit val serialization: Serialization.type = Serialization
  protected implicit val jsonFormats: Formats              = DefaultFormats.withBigDecimal ++
    JavaTypesSerializers.all + InstantSerializer
}

object InstantSerializer
    extends CustomSerializer[Instant](
      _ =>
        (
          {
            case JLong(l) => Instant.ofEpochMilli(l)
          },
          {
            case instant: Instant => JLong(instant.toEpochMilli)
          }
        )
    )
