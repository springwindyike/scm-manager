package sonia.scm.update;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sonia.scm.lifecycle.RestartEvent;
import sonia.scm.event.ScmEventBus;
import sonia.scm.update.repository.MigrationStrategy;
import sonia.scm.update.repository.MigrationStrategyDao;
import sonia.scm.update.repository.V1Repository;
import sonia.scm.update.repository.XmlRepositoryV1UpdateStep;
import sonia.scm.util.ValidationUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Singleton
class MigrationWizardServlet extends HttpServlet {

  private static final Logger LOG = LoggerFactory.getLogger(MigrationWizardServlet.class);

  private final XmlRepositoryV1UpdateStep repositoryV1UpdateStep;
  private final MigrationStrategyDao migrationStrategyDao;

  @Inject
  MigrationWizardServlet(XmlRepositoryV1UpdateStep repositoryV1UpdateStep, MigrationStrategyDao migrationStrategyDao) {
    this.repositoryV1UpdateStep = repositoryV1UpdateStep;
    this.migrationStrategyDao = migrationStrategyDao;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    List<RepositoryLineEntry> repositoryLineEntries = getRepositoryLineEntries();
    doGet(req, resp, repositoryLineEntries);
  }

  private void doGet(HttpServletRequest req, HttpServletResponse resp, List<RepositoryLineEntry> repositoryLineEntries) {
    HashMap<String, Object> model = new HashMap<>();

    model.put("contextPath", req.getContextPath());
    model.put("submitUrl", req.getRequestURI());
    model.put("repositories", repositoryLineEntries);
    model.put("strategies", getMigrationStrategies());
    model.put("validationErrorsFound", repositoryLineEntries
      .stream()
      .anyMatch(entry -> entry.isNamespaceInvalid() || entry.isNameInvalid()));

    respondWithTemplate(resp, model, "templates/repository-migration.mustache");
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    List<RepositoryLineEntry> repositoryLineEntries = getRepositoryLineEntries();

    boolean validationErrorFound = false;
    for (RepositoryLineEntry repositoryLineEntry : repositoryLineEntries) {
      String id = repositoryLineEntry.getId();

      String strategy = req.getParameter("strategy-" + id);
      if (!Strings.isNullOrEmpty(strategy)) {
        repositoryLineEntry.setSelectedStrategy(MigrationStrategy.valueOf(strategy));
      }

      String namespace = req.getParameter("namespace-" + id);
      repositoryLineEntry.setNamespace(namespace);

      String name = req.getParameter("name-" + id);
      repositoryLineEntry.setName(name);

      if (!ValidationUtil.isRepositoryNameValid(namespace)) {
        repositoryLineEntry.setNamespaceValid(false);
        validationErrorFound = true;
      }
      if (!ValidationUtil.isRepositoryNameValid(name)) {
        repositoryLineEntry.setNameValid(false);
        validationErrorFound = true;
      }
    }

    if (validationErrorFound) {
      doGet(req, resp, repositoryLineEntries);
      return;
    }

    repositoryLineEntries.stream()
      .forEach(
        entry-> {
          String id = entry.getId();
          String originalName = entry.getOriginalName();
          String strategy = req.getParameter("strategy-" + id);
          String namespace = req.getParameter("namespace-" + id);
          String name = req.getParameter("name-" + id);
          migrationStrategyDao.set(id, originalName, MigrationStrategy.valueOf(strategy), namespace, name);
        }
      );

    Map<String, Object> model = Collections.singletonMap("contextPath", req.getContextPath());

    respondWithTemplate(resp, model, "templates/repository-migration-restart.mustache");

    ScmEventBus.getInstance().post(new RestartEvent(MigrationWizardServlet.class, "wrote migration data"));
  }

  private List<RepositoryLineEntry> getRepositoryLineEntries() {
    List<V1Repository> repositoriesWithoutMigrationStrategies =
      repositoryV1UpdateStep.getRepositoriesWithoutMigrationStrategies();
    return repositoriesWithoutMigrationStrategies.stream()
      .map(RepositoryLineEntry::new)
      .sorted(comparing(RepositoryLineEntry::getPath))
      .collect(Collectors.toList());
  }

  private MigrationStrategy[] getMigrationStrategies() {
    return MigrationStrategy.values();
  }

  @VisibleForTesting
  void respondWithTemplate(HttpServletResponse resp, Map<String, Object> model, String templateName) {
    MustacheFactory mf = new DefaultMustacheFactory();
    Mustache template = mf.compile(templateName);

    PrintWriter writer;
    try {
      writer = resp.getWriter();
    } catch (IOException e) {
      LOG.error("could not create writer for response", e);
      resp.setStatus(500);
      return;
    }
    template.execute(writer, model);
    writer.flush();
    resp.setStatus(200);
  }

  private static class RepositoryLineEntry {
    private final String id;
    private final String originalName;
    private final String type;
    private final String path;
    private MigrationStrategy selectedStrategy;
    private String namespace;
    private String name;
    private boolean namespaceValid = true;
    private boolean nameValid = true;

    public RepositoryLineEntry(V1Repository repository) {
      this.id = repository.getId();
      this.originalName = repository.getName();
      this.type = repository.getType();
      this.path = repository.getType() + "/" + repository.getName();
      this.selectedStrategy = MigrationStrategy.COPY;
      this.namespace = computeNewNamespace(repository);
      this.name = computeNewName(repository);
    }

    private static String computeNewNamespace(V1Repository v1Repository) {
      String[] nameParts = getNameParts(v1Repository.getName());
      return nameParts.length > 1 ? nameParts[0] : v1Repository.getType();
    }

    private static String computeNewName(V1Repository v1Repository) {
      String[] nameParts = getNameParts(v1Repository.getName());
      return nameParts.length == 1 ? nameParts[0] : concatPathElements(nameParts);
    }

    private static String[] getNameParts(String v1Name) {
      return v1Name.split("/");
    }

    private static String concatPathElements(String[] nameParts) {
      return Arrays.stream(nameParts).skip(1).collect(Collectors.joining("_"));
    }

    public String getId() {
      return id;
    }

    public String getOriginalName() {
      return originalName;
    }

    public String getType() {
      return type;
    }

    public String getPath() {
      return path;
    }

    public String getNamespace() {
      return namespace;
    }

    public String getName() {
      return name;
    }

    public MigrationStrategy getSelectedStrategy() {
      return selectedStrategy;
    }

    public List<RepositoryLineMigrationStrategy> getStrategies() {
      return Arrays.stream(MigrationStrategy.values())
        .map(s -> new RepositoryLineMigrationStrategy(s.name(), selectedStrategy == s))
        .collect(Collectors.toList());
    }

    public void setNamespace(String namespace) {
      this.namespace = namespace;
    }

    public void setName(String name) {
      this.name = name;
    }

    public void setNamespaceValid(boolean namespaceValid) {
      this.namespaceValid = namespaceValid;
    }

    public void setNameValid(boolean nameValid) {
      this.nameValid = nameValid;
    }

    public void setSelectedStrategy(MigrationStrategy selectedStrategy) {
      this.selectedStrategy = selectedStrategy;
    }

    public boolean isNamespaceInvalid() {
      return !namespaceValid;
    }

    public boolean isNameInvalid() {
      return !nameValid;
    }
  }

  private static class RepositoryLineMigrationStrategy {

    private final String name;
    private final boolean selected;

    private RepositoryLineMigrationStrategy(String name, boolean selected) {
      this.name = name;
      this.selected = selected;
    }

    public String getName() {
      return name;
    }

    public boolean isSelected() {
      return selected;
    }
  }
}
