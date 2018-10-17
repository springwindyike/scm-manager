// @flow

import React from "react";
import type { Branch, Repository } from "@scm-manager/ui-types";
import { Route, withRouter } from "react-router-dom";
import Changesets from "./Changesets";
import BranchSelector from "./BranchSelector";
import { connect } from "react-redux";
import { Loading } from "@scm-manager/ui-components";
import {
  fetchBranches,
  getBranches,
  getFetchBranchesFailure,
  isFetchBranchesPending
} from "../modules/branches";
import { compose } from "redux";

type Props = {
  repository: Repository,
  baseUrl: string,
  selected: string,

  // State props
  branches: Branch[],
  loading: boolean,

  // Dispatch props
  fetchBranches: Repository => void,

  // Context props
  history: History,
  match: any
};

class BranchRoot extends React.Component<Props> {
  constructor(props: Props) {
    super(props);
    this.state = {};
  }

  componentDidMount() {
    this.props.fetchBranches(this.props.repository);
  }

  stripEndingSlash = (url: string) => {
    if (url.endsWith("/")) {
      return url.substring(0, url.length - 1);
    }
    return url;
  };

  matchedUrl = () => {
    return this.stripEndingSlash(this.props.baseUrl);
  };

  branchSelected = (branch: Branch) => {
    const url = this.matchedUrl();
    this.props.history.push(
      `${url}/${encodeURIComponent(branch.name)}/changesets`
    );
  };

  findSelectedBranch = () => {
    const { selected, branches } = this.props;
    return branches.find((branch: Branch) => branch.name === selected);
  };

  render() {
    // TODO error???
    const { repository, loading, match, branches } = this.props;
    const url = this.stripEndingSlash(match.url);

    if (loading) {
      return <Loading />;
    }
    if (!repository || !branches) {
      return null;
    }

    const branch = this.findSelectedBranch();
    const changesets = <Changesets repository={repository} branch={branch} />;

    return (
      <>
        <BranchSelector
          branches={branches}
          selected={(b: Branch) => {
            this.branchSelected(b);
          }}
        />
        <Route path={`${url}/:page?`} component={() => changesets} />
      </>
    );
  }
}

const mapDispatchToProps = dispatch => {
  return {
    fetchBranches: (repo: Repository) => {
      dispatch(fetchBranches(repo));
    }
  };
};

const mapStateToProps = (state: any, ownProps: Props) => {
  const { repository, match } = ownProps;
  const loading = isFetchBranchesPending(state, repository);
  const error = getFetchBranchesFailure(state, repository);
  const branches = getBranches(state, repository);
  const selected = decodeURIComponent(match.params.branch);

  return {
    loading,
    error,
    branches,
    selected
  };
};

export default compose(
  withRouter,
  connect(
    mapStateToProps,
    mapDispatchToProps
  )
)(BranchRoot);
