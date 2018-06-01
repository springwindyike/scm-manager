package sonia.scm.api.v2.resources;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@Data
@XmlRootElement
public class Link {

  private URI href;

  public Link(URI href) {
    this.href = href;
  }
}