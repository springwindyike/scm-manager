import React, { Component } from "react";
import Main from "./Main";
import Login from "./Login";
import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import { ThunkDispatch } from "redux-thunk";
import { fetchMe } from "../modules/me";

import "./App.css";
import Header from "../components/Header";
import PrimaryNavigation from "../components/PrimaryNavigation";
import Loading from "../components/Loading";
import Notification from "../components/Notification";
import Footer from "../components/Footer";

type Props = {
  login: boolean,
  me: any,
  fetchMe: () => void,
  loading: boolean
};

class App extends Component<Props> {
  componentDidMount() {
    this.props.fetchMe();
  }
  render() {
    const { me, loading } = this.props;

    let content = [];
    let navigation;

    if (loading) {
      content.push(<Loading />);
    } else if (!me) {
      content.push(<Login />);
    } else {
      content.push(<Main />, <Footer me={me} />);
      navigation = <PrimaryNavigation />;
    }

    return (
      <div className="App">
        <Header>{navigation}</Header>
        {content.map(c => {
          return c;
        })}
      </div>
    );
  }
}

const mapDispatchToProps = (dispatch: ThunkDispatch) => {
  return {
    fetchMe: () => dispatch(fetchMe())
  };
};

const mapStateToProps = state => {
  return state.me || {};
};

export default withRouter(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(App)
);
