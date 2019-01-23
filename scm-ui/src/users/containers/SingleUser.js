//@flow
import React from "react";
import { connect } from "react-redux";
import {
  Page,
  Loading,
  Navigation,
  SubNavigation,
  Section,
  NavLink,
  ErrorPage
} from "@scm-manager/ui-components";
import { Route } from "react-router";
import { Details } from "./../components/table";
import GeneralUser from "./GeneralUser";
import type { User } from "@scm-manager/ui-types";
import type { History } from "history";
import {
  fetchUserByName,
  getUserByName,
  isFetchUserPending,
  getFetchUserFailure
} from "../modules/users";
import { GeneralUserNavLink, SetPasswordNavLink, SetPermissionsNavLink } from "./../components/navLinks";
import { translate } from "react-i18next";
import { getUsersLink } from "../../modules/indexResource";
import SetUserPassword from "../components/SetUserPassword";
import SetPermissions from "../../permissions/components/SetPermissions";

type Props = {
  name: string,
  user: User,
  loading: boolean,
  error: Error,
  usersLink: string,

  // dispatcher function
  fetchUserByName: (string, string) => void,

  // context objects
  t: string => string,
  match: any,
  history: History
};

class SingleUser extends React.Component<Props> {
  componentDidMount() {
    this.props.fetchUserByName(this.props.usersLink, this.props.name);
  }

  stripEndingSlash = (url: string) => {
    if (url.endsWith("/")) {
      return url.substring(0, url.length - 2);
    }
    return url;
  };

  matchedUrl = () => {
    return this.stripEndingSlash(this.props.match.url);
  };

  render() {
    const { t, loading, error, user } = this.props;

    if (error) {
      return (
        <ErrorPage
          title={t("singleUser.errorTitle")}
          subtitle={t("singleUser.errorSubtitle")}
          error={error}
        />
      );
    }

    if (!user || loading) {
      return <Loading />;
    }

    const url = this.matchedUrl();

    return (
      <Page title={user.displayName}>
        <div className="columns">
          <div className="column is-three-quarters">
            <Route path={url} exact component={() => <Details user={user} />} />
            <Route
              path={`${url}/settings/general`}
              component={() => <GeneralUser user={user} />}
            />
            <Route
              path={`${url}/settings/password`}
              component={() => <SetUserPassword user={user} />}
            />
            <Route
              path={`${url}/permissions`}
              component={() => (
                <SetPermissions
                  selectedPermissionsLink={user._links.permissions}
                />
              )}
            />
          </div>
          <div className="column">
            <Navigation>
              <Section label={t("singleUser.menu.navigationLabel")}>
                <NavLink
                  to={`${url}`}
                  label={t("singleUser.menu.informationNavLink")}
                />
                <SubNavigation
                  to={`${url}/settings/general`}
                  label={t("singleUser.menu.settingsNavLink")}
                >
                  <GeneralUserNavLink
                    user={user}
                    editUrl={`${url}/settings/general`}
                  />
                  <SetPasswordNavLink
                    user={user}
                    passwordUrl={`${url}/settings/password`}
                  />
                  <SetPermissionsNavLink
                    user={user}
                    permissionsUrl={`${url}/settings/permissions`}
                  />
                </SubNavigation>
              </Section>
            </Navigation>
          </div>
        </div>
      </Page>
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  const name = ownProps.match.params.name;
  const user = getUserByName(state, name);
  const loading = isFetchUserPending(state, name);
  const error = getFetchUserFailure(state, name);
  const usersLink = getUsersLink(state);
  return {
    usersLink,
    name,
    user,
    loading,
    error
  };
};

const mapDispatchToProps = dispatch => {
  return {
    fetchUserByName: (link: string, name: string) => {
      dispatch(fetchUserByName(link, name));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(translate("users")(SingleUser));
