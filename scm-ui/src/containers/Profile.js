// @flow

import React from "react";

import { Route, withRouter } from "react-router-dom";
import { getMe } from "../modules/auth";
import { compose } from "redux";
import { connect } from "react-redux";
import { translate } from "react-i18next";
import type { Me } from "@scm-manager/ui-types";
import {
  ErrorPage,
  Page,
  Navigation,
  Section,
  NavLink
} from "@scm-manager/ui-components";
import ChangeUserPassword from "./ChangeUserPassword";
import ProfileInfo from "./ProfileInfo";

type Props = {
  me: Me,

  // Context props
  t: string => string,
  match: any
};
type State = {};

class Profile extends React.Component<Props, State> {
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
    const url = this.matchedUrl();

    const { me, t } = this.props;

    if (!me) {
      return (
        <ErrorPage
          title={t("profile.error-title")}
          subtitle={t("profile.error-subtitle")}
          error={{
            name: t("profile.error"),
            message: t("profile.error-message")
          }}
        />
      );
    }

    return (
      <Page title={me.displayName}>
        <div className="columns">
          <div className="column is-three-quarters">
            <Route path={url} exact render={() => <ProfileInfo me={me} />} />
            <Route
              path={`${url}/password`}
              render={() => <ChangeUserPassword me={me} />}
            />
          </div>
          <div className="column">
            <Navigation>
              <Section label={t("profile.navigation-label")}>
                <NavLink to={`${url}`} label={t("profile.information")} />
              </Section>
              <Section label={t("profile.actions-label")}>
                <NavLink
                  to={`${url}/password`}
                  label={t("profile.change-password")}
                />
              </Section>
            </Navigation>
          </div>
        </div>
      </Page>
    );
  }
}

const mapStateToProps = state => {
  return {
    me: getMe(state)
  };
};

export default compose(
  translate("commons"),
  connect(mapStateToProps),
  withRouter
)(Profile);