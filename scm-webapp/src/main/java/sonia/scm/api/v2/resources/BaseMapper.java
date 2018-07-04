package sonia.scm.api.v2.resources;

import de.otto.edison.hal.HalRepresentation;
import org.mapstruct.Mapping;
import sonia.scm.ModelObject;

import java.time.Instant;

abstract class BaseMapper<T extends ModelObject, D extends HalRepresentation> {

  @Mapping(target = "attributes", ignore = true) // We do not map HAL attributes
  public abstract D map(T modelObject);

  Instant mapTime(Long epochMilli) {
    return epochMilli == null? null: Instant.ofEpochMilli(epochMilli);
  }
}