import React from "react";
import "./App.css";
import Login from "./pages/Login";
import SignUp from "./pages/SignUp_Page/RenderSignUp";
import Homepage from "./pages/Homepage";
import EditUserList from "./pages/EditUserList_Page/RenderEditUserList";
import { Route, Switch, HashRouter } from "react-router-dom";

require("dotenv").config();

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      token: null,
    };
  }

  sendToken = (token) => {
    this.setState({ token: token });
  };

  render() {
    return (
      <div className="App">
        <HashRouter>
          <Switch>
            <Route path="/homepage" component={Homepage} />
            <Route path="/signup" component={SignUp} />
            <Route path="/edituserlist">
              {" "}
              <EditUserList token={this.state.token} />{" "}
            </Route>
            <Route path="/">
              {" "}
              <Login sendToken={this.setToken} />{" "}
            </Route>
          </Switch>
        </HashRouter>
      </div>
    );
  }
}
