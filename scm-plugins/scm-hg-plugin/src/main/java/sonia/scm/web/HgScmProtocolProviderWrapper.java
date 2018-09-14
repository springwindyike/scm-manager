package sonia.scm.web;

import sonia.scm.api.v2.resources.ScmPathInfoStore;
import sonia.scm.config.ScmConfiguration;
import sonia.scm.plugin.Extension;
import sonia.scm.repository.spi.InitializingHttpScmProtocolWrapper;
import sonia.scm.repository.spi.ScmProviderHttpServlet;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
@Extension
public class HgScmProtocolProviderWrapper extends InitializingHttpScmProtocolWrapper {
  @Inject
  public HgScmProtocolProviderWrapper(@Hg Provider<ScmProviderHttpServlet> servletProvider, Provider<ScmPathInfoStore> uriInfoStore, ScmConfiguration scmConfiguration) {
    super(servletProvider, uriInfoStore, scmConfiguration);
  }

  @Override
  public String getType() {
    return "hg";
  }
}
