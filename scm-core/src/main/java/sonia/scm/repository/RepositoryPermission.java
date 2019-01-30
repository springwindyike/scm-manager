/*
  Copyright (c) 2010, Sebastian Sdorra
  All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.
  2. Redistributions in binary form must reproduce the above copyright notice,
     this list of conditions and the following disclaimer in the documentation
     and/or other materials provided with the distribution.
  3. Neither the name of SCM-Manager; nor the names of its
     contributors may be used to endorse or promote products derived from this
     software without specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
  DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  http://bitbucket.org/sdorra/scm-manager

 */



package sonia.scm.repository;

//~--- non-JDK imports --------------------------------------------------------

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.commons.collections.CollectionUtils;
import sonia.scm.security.PermissionObject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableCollection;

//~--- JDK imports ------------------------------------------------------------

/**
 * Permissions controls the access to {@link Repository}.
 *
 * @author Sebastian Sdorra
 */
@XmlRootElement(name = "permissions")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepositoryPermission implements PermissionObject, Serializable
{

  private static final long serialVersionUID = -2915175031430884040L;

  private boolean groupPermission = false;
  private String name;
  @XmlElement(name = "verb")
  private Collection<String> verbs;

  /**
   * Constructs a new {@link RepositoryPermission}.
   * This constructor is used by JAXB and mapstruct.
   */
  public RepositoryPermission() {}

  public RepositoryPermission(String name, Collection<String> verbs, boolean groupPermission)
  {
    this.name = name;
    this.verbs = unmodifiableCollection(new LinkedHashSet<>(verbs));
    this.groupPermission = groupPermission;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Returns true if the {@link RepositoryPermission} is the same as the obj argument.
   *
   *
   * @param obj the reference object with which to compare
   *
   * @return true if the {@link RepositoryPermission} is the same as the obj argument
   */
  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    final RepositoryPermission other = (RepositoryPermission) obj;

    return Objects.equal(name, other.name)
      && CollectionUtils.isEqualCollection(verbs, other.verbs)
      && Objects.equal(groupPermission, other.groupPermission);
  }

  /**
   * Returns the hash code value for the {@link RepositoryPermission}.
   *
   *
   * @return the hash code value for the {@link RepositoryPermission}
   */
  @Override
  public int hashCode()
  {
    // Normally we do not have a log of repository permissions having the same size of verbs, but different content.
    // Therefore we do not use the verbs themselves for the hash code but only the number of verbs.
    return Objects.hashCode(name, verbs.size(), groupPermission);
  }


  @Override
  public String toString()
  {
    //J-
    return MoreObjects.toStringHelper(this)
            .add("name", name)
            .add("verbs", verbs)
            .add("groupPermission", groupPermission)
            .toString();
    //J+
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Returns the name of the user or group.
   *
   *
   * @return name of the user or group
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * Returns the verb of the permission.
   *
   *
   * @return verb of the permission
   */
  public Collection<String> getVerbs()
  {
    return verbs == null? emptyList(): verbs;
  }

  /**
   * Returns true if the permission is a permission which affects a group.
   *
   *
   * @return true if the permision is a group permission
   */
  @Override
  public boolean isGroupPermission()
  {
    return groupPermission;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Sets true if the permission is a group permission.
   *
   *
   * @param groupPermission true if the permission is a group permission
   */
  public void setGroupPermission(boolean groupPermission)
  {
    this.groupPermission = groupPermission;
  }

  /**
   * The name of the user or group.
   *
   *
   * @param name name of the user or group
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Sets the verb of the permission.
   *
   *
   * @param verbs verbs of the permission
   */
  public void setVerbs(Collection<String> verbs)
  {
    this.verbs = verbs;
  }
}
