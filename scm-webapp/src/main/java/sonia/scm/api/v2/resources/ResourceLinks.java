package sonia.scm.api.v2.resources;

import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

class ResourceLinks {

  private final UriInfoStore uriInfoStore;

  @Inject
  ResourceLinks(UriInfoStore uriInfoStore) {
    this.uriInfoStore = uriInfoStore;
  }


  GroupLinks group() {
    return new GroupLinks(uriInfoStore.get());
  }

  static class GroupLinks {
    private final LinkBuilder groupLinkBuilder;

    GroupLinks(UriInfo uriInfo) {
      groupLinkBuilder = new LinkBuilder(uriInfo, GroupRootResource.class, GroupResource.class);
    }

    String self(String name) {
      return groupLinkBuilder.method("getGroupResource").parameters(name).method("get").parameters().href();
    }

    String delete(String name) {
      return groupLinkBuilder.method("getGroupResource").parameters(name).method("delete").parameters().href();
    }

    String update(String name) {
      return groupLinkBuilder.method("getGroupResource").parameters(name).method("update").parameters().href();
    }
  }

  GroupCollectionLinks groupCollection() {
    return new GroupCollectionLinks(uriInfoStore.get());
  }

  static class GroupCollectionLinks {
    private final LinkBuilder collectionLinkBuilder;

    GroupCollectionLinks(UriInfo uriInfo) {
      collectionLinkBuilder = new LinkBuilder(uriInfo, GroupRootResource.class, GroupCollectionResource.class);
    }

    String self() {
      return collectionLinkBuilder.method("getGroupCollectionResource").parameters().method("getAll").parameters().href();
    }

    String create() {
      return collectionLinkBuilder.method("getGroupCollectionResource").parameters().method("create").parameters().href();
    }
  }

  UserLinks user() {
    return new UserLinks(uriInfoStore.get());
  }

  static class UserLinks {
    private final LinkBuilder userLinkBuilder;

    UserLinks(UriInfo uriInfo) {
      userLinkBuilder = new LinkBuilder(uriInfo, UserRootResource.class, UserResource.class);
    }

    String self(String name) {
      return userLinkBuilder.method("getUserResource").parameters(name).method("get").parameters().href();
    }

    String delete(String name) {
      return userLinkBuilder.method("getUserResource").parameters(name).method("delete").parameters().href();
    }

     String update(String name) {
      return userLinkBuilder.method("getUserResource").parameters(name).method("update").parameters().href();
    }
  }

  UserCollectionLinks userCollection() {
    return new UserCollectionLinks(uriInfoStore.get());
  }

  static class UserCollectionLinks {
    private final LinkBuilder collectionLinkBuilder;

    UserCollectionLinks(UriInfo uriInfo) {
      collectionLinkBuilder = new LinkBuilder(uriInfo, UserRootResource.class, UserCollectionResource.class);
    }

    String self() {
      return collectionLinkBuilder.method("getUserCollectionResource").parameters().method("getAll").parameters().href();
    }

    String create() {
      return collectionLinkBuilder.method("getUserCollectionResource").parameters().method("create").parameters().href();
    }
  }

  public RepositoryLinks repository() {
    return new RepositoryLinks(uriInfoStore.get());
  }

  static class RepositoryLinks {
    private final LinkBuilder repositoryLinkBuilder;

    RepositoryLinks(UriInfo uriInfo) {
      repositoryLinkBuilder = new LinkBuilder(uriInfo, RepositoryRootResource.class, RepositoryResource.class);
    }

    String self(String namespace, String name) {
      return repositoryLinkBuilder.method("getRepositoryResource").parameters(namespace, name).method("get").parameters().href();
    }

    String delete(String namespace, String name) {
      return repositoryLinkBuilder.method("getRepositoryResource").parameters(namespace, name).method("delete").parameters().href();
    }

    String update(String namespace, String name) {
      return repositoryLinkBuilder.method("getRepositoryResource").parameters(namespace, name).method("update").parameters().href();
    }
  }

  public TagCollectionLinks tagCollection() {
    return new TagCollectionLinks(uriInfoStore.get());
  }

  static class TagCollectionLinks {
    private final LinkBuilder tagLinkBuilder;

    TagCollectionLinks(UriInfo uriInfo) {
      tagLinkBuilder = new LinkBuilder(uriInfo, RepositoryRootResource.class, RepositoryResource.class, TagRootResource.class, TagCollectionResource.class);
    }

    String self(String namespace, String name) {
      return tagLinkBuilder.method("getRepositoryResource").parameters(namespace, name).method("tags").parameters().method("getTagCollectionResource").parameters().method("getAll").parameters().href();
    }
  }

  public BranchCollectionLinks branchCollection() {
    return new BranchCollectionLinks(uriInfoStore.get());
  }

  static class BranchCollectionLinks {
    private final LinkBuilder branchLinkBuilder;

    BranchCollectionLinks(UriInfo uriInfo) {
      branchLinkBuilder = new LinkBuilder(uriInfo, RepositoryRootResource.class, RepositoryResource.class, BranchRootResource.class, BranchCollectionResource.class);
    }

    String self(String namespace, String name) {
      return branchLinkBuilder.method("getRepositoryResource").parameters(namespace, name).method("branches").parameters().method("getBranchCollectionResource").parameters().method("getAll").parameters().href();
    }
  }
}