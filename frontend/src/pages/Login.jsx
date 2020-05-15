import React from "react";
import Particles from "../Particles";
import "../style.css";
import Validator from "./utilities/Validator";
import axios from "axios";
require("dotenv").config();

axios.interceptors.request.use(
  (config) => {
    var token = sessionStorage.getItem("token");

    if (token) {
      config.headers["Authorization"] = "Bearer " + sessionStorage.token;
    }
    return config;
  },
  (error) => {
    Promise.reject(error);
  }
);

export class LoginForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: "",
      password: "",
      errorMessageLogin: "",
      info: [],
      rememberMe: false,
      token: "",
    };
  }

  handleCheck = (event) => {
    this.setState({ rememberMe: event.target.checked });
  };

  onChangeLoginInput = (event) => {
    const validator = new Validator();
    if (event.target.name === "username") {
      this.setState({ username: event.target.value }, () => {
        if (validator.isUsernameLengthValid(this.state.username) !== null)
          this.setState({
            errorMessageLogin: "Username o password scorretti",
          });
        else this.setState({ errorMessageLogin: null });
      });
    }
    if (event.target.name === "password")
      this.setState({ password: event.target.value }, () => {
        if (validator.isPswLongEnough(this.state.password) !== null)
          this.setState({
            errorMessageLogin: "Username o password scorretti",
          });
        else this.setState({ errorMessageLogin: null });
      });
  };

  submitHandler = (e) => {
    e.preventDefault(); //avoid page refresh

    var obj = {
      username: this.state.username,
      password: this.state.password,
      rememberMe: this.state.rememberMe,
    };

    const url_auth = process.env.REACT_APP_BACKEND_URL + "/auth";
    axios
      .post(url_auth, obj)
      .then((response) => {
        const main_token = response.data.token;

        this.setState({ token: main_token });
        sessionStorage.setItem("token", main_token);
        sessionStorage.setItem("username", obj.username);

        if (response.status === 200) {
          if (obj.username === "admin" && obj.password === "admin") {
            window.location.href =
              process.env.REACT_APP_FRONTEND_URL + "/#/edituserlist";
          } else {
            window.location.href =
              process.env.REACT_APP_FRONTEND_URL + "/#/homepage";
          }
        }
      })
      .catch(() => {
        this.setState({ errorMessageLogin: "Username o password scorretti" });
      });
  };

  render() {
    const { username, password } = this.state;
    return (
      <div className="login_thirema centered">
        <img
          src={require("../img/logo_login.png")}
          alt="Logo di ThiReMa"
          className="login_logo"
        />
        <h1 className="white_login_text">Accedi al tuo account</h1>
        <p className="white_signUp">
          Inserisci il tuo nome utente e la tua password per accedere ai
          contenuti
        </p>
        <form className="form-style" onSubmit={this.submitHandler}>
          <input
            type="text"
            name="username"
            className="form-input"
            placeholder="Nome utente"
            value={username}
            onChange={this.onChangeLoginInput}
          ></input>
          <input
            type="password"
            name="password"
            className="form-input"
            placeholder="Password"
            value={password}
            onChange={this.onChangeLoginInput}
          ></input>
          <br></br>
          <button
            type="submit"
            value="Accedi"
            className="form-button"
            onClick={this.signup}
          >
            Accedi
          </button>
          <p className="error-message-form">{this.state.errorMessageLogin}</p>
          <div className="item">
            <input
              type="checkbox"
              className="rememberMeCheckBox"
              checked={this.state.rememberMe}
              onChange={this.handleCheck}
            />
            <label className="rememberMeLabel">Mantienimi autenticato</label>
          </div>
        </form>
      </div>
    );
  }
}

export default class Login extends React.Component {
  render() {
    return (
      <div className="background">
        <div className="content">
          <div className="form_login">
            <LoginForm />
          </div>
        </div>
        <Particles />
      </div>
    );
  }
}
