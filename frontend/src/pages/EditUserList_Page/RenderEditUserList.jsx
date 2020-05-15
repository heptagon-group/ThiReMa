import React from "react";
import Particles from "../../Particles";
import "../../style.css";
import { NavLink } from "react-router-dom";
import UserInfo from "./UserInfo";
import List from "./UserList";
import { Icon } from "@iconify/react";
import logoutIcon from "@iconify/icons-fe/logout";

//guida seguita per il passaggio di dati child1->parent->child2: https://towardsdatascience.com/passing-data-between-react-components-parent-children-siblings-a64f89e24ecf

/* è questa classe che si deve preoccupare di generare le varie classi che ci andranno dentro! 
sostanzialmente le classi che ne racchiudono altre si occupano di generarle */

export class WhiteRectangle extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      index: "",
      editedUser: "",
      needToEdit: false,
    };
    this.callbackFunction = this.callbackFunction.bind(this);
    this.userCallBackFunctionFromUserList = this.userCallBackFunctionFromUserList.bind(
      this
    );
    this.booleanCallBackFunctionFromUserList = this.booleanCallBackFunctionFromUserList.bind(
      this
    );
  }

  callbackFunction = (index) => {
    this.setState({ index: index });
  };

  userCallBackFunctionFromUserList = (user) => {
    this.setState({
      editedUser: user,
    });
  };

  booleanCallBackFunctionFromUserList = (boolean) => {
    this.setState({ needToEdit: boolean });
  };

  render() {
    return (
      <div className="white-bg-rectangle centered">
        <div className="left_side">
          <Header />
          <UserInfo
            index={this.state.index}
            parentCallbackEditUser={this.userCallBackFunctionFromUserList}
            booleanParendCallBackEditUser={
              this.booleanCallBackFunctionFromUserList
            }
          />
        </div>

        <div className="right_side">
          <Menu />
          <List
            parentCallback={this.callbackFunction}
            indexOfModifiedElement={this.state.index}
            newUserInfo={this.state.editedUser}
            toEdit={this.state.needToEdit}
          />
        </div>
      </div>
    );
  }
}

export class Header extends React.Component {
  render() {
    return (
      <header className="header">
        <img
          src={require("../../img/logo_amministratore.png")}
          alt="Logo di ThiReMa"
          className="list-logo"
        />
      </header>
    );
  }
}

export class Menu extends React.Component {
  render() {
    return (
      <div className="navbar-line">
        <ul className="navbar_menu">
          <li>
            <NavLink
              className="text_layout"
              activeClassName="activeLink"
              exact
              to="/signup"
              lang="it"
            >
              <button type="submit" value="Accedi" className="button-header">
                Registrazione
              </button>
            </NavLink>
          </li>
          <li>
            {" "}
            <button
              onClick={() => {
                sessionStorage.token = null;
                window.location.href =
                  process.env.REACT_APP_FRONTEND_URL + "/#";
              }}
              className="transparent-button"
              title="Esci"
            >
              <Icon
                icon={logoutIcon}
                className="white icon-user-list icon-user-data"
              />
            </button>
          </li>
        </ul>
      </div>
    );
  }
}

export default class EditUserList extends React.Component {
  render() {
    return (
      <div className="background">
        <WhiteRectangle />
        <Particles />
      </div>
    );
  }
}
