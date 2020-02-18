package sonia.scm.api.v2.resources;

import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import sonia.scm.security.AllowAnonymousAccess;
import sonia.scm.web.VndMediaType;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@OpenAPIDefinition(security = {
  @SecurityRequirement(name = "Basic Authentication"),
  @SecurityRequirement(name = "Bearer Token Authentication")
})
@Path(IndexResource.INDEX_PATH_V2)
@AllowAnonymousAccess
public class IndexResource {
  public static final String INDEX_PATH_V2 = "v2/";

  private final IndexDtoGenerator indexDtoGenerator;

  @Inject
  public IndexResource(IndexDtoGenerator indexDtoGenerator) {
    this.indexDtoGenerator = indexDtoGenerator;
  }

  @GET
  @Path("")
  @Produces(VndMediaType.INDEX)
  @TypeHint(IndexDto.class)
  public IndexDto getIndex() {
    return indexDtoGenerator.generate();
  }
}
