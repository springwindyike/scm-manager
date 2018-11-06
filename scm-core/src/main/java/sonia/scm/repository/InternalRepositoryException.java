package sonia.scm.repository;

import sonia.scm.ContextEntry;
import sonia.scm.ExceptionWithContext;

import java.util.List;

public class InternalRepositoryException extends ExceptionWithContext {

  public InternalRepositoryException(ContextEntry.ContextBuilder context, String message) {
    this(context, message, null);
  }

  public InternalRepositoryException(ContextEntry.ContextBuilder context, String message, Exception cause) {
    this(context.build(), message, cause);
  }

  public InternalRepositoryException(Repository repository, String message, Exception cause) {
    this(ContextEntry.ContextBuilder.entity(repository), message, cause);
  }

  public InternalRepositoryException(List<ContextEntry> context, String message, Exception cause) {
    super(context, message, cause);
  }

  @Override
  public String getCode() {
    return null;
  }
}
