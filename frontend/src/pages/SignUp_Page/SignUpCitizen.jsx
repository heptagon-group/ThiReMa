import React from "react";
import "../../style.css";
import Validator from "./../utilities/Validator";
import axios from "axios";

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
export default class SignUpFormCitizen extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: "",
      vatNumber: "",
      email: "",
      telegramUsername: "",
      password: "",
      confirmPsw: "",

      errorMessagePswDontMatch: "",
      errorMessageUsernameLength: "",
      errorMessageTelegramLength: "",
      errorMessagePswLength: "",
      errorMessageEmailFormat: "",
      errorMessageCouldntAdd: "",
      errorMessageVatNumberLength: "",
      errorMessagePswTooLong: "",
      errorMessageEmailTooLong: "",
      errorMessageUsernameTooLong: "",
      errorMessageTelegramUsernameTooLong: "",
    };
  }
  onChangeValuePrivato = (event) => {
    const validator = new Validator();
    if (event.target.name === "username")
      this.setState({ username: event.target.value }, () => {
        this.setState({
          errorMessageUsernameLength: validator.isUsernameLengthValid(
            this.state.username
          ),
          errorMessageUsernameTooLong: validator.isUsernameTooLong(
            this.state.username
          ),
        });
      });
    if (event.target.name === "email")
      this.setState({ email: event.target.value }, () => {
        this.setState({
          errorMessageEmailFormat: validator.isEmailValid(this.state.email),
          errorMessageEmailTooLong: validator.isEmailTooLong(this.state.email),
        });
      });
    if (event.target.name === "telegramUsername")
      this.setState({ telegramUsername: event.target.value }, () => {
        this.setState({
          errorMessageTelegramLength: validator.isTelegramUsernameLengthValid(
            this.state.telegramUsername
          ),
          errorMessageTelegramUsernameTooLong: validator.isTelegramUsernameTooLong(
            this.state.telegramUsername
          ),
        });
      });
    if (event.target.name === "password")
      this.setState({ password: event.target.value }, () => {
        this.setState({
          errorMessagePswLength: validator.isPswLongEnough(this.state.password),
          errorMessagePswTooLong: validator.isPswTooLong(this.state.password),
        });
      });
    if (event.target.name === "confirmPsw")
      this.setState({ confirmPsw: event.target.value }, () => {
        this.setState({
          errorMessagePswDontMatch: validator.pswMatch(
            this.state.password,
            this.state.confirmPsw
          ),
        });
      });
  };

  registerCitizen = (e) => {
    e.preventDefault(); //avoid page refresh

    var obj = {
      username: this.state.username,
      email: this.state.email,
      telegramUsername: this.state.telegramUsername,
      password: this.state.password,
    };

    const url = process.env.REACT_APP_BACKEND_URL + "/user";
    axios
      .post(url, obj)
      .then((response) => {
        if (response.status === 201) {
          window.location.href =
            process.env.REACT_APP_FRONTEND_URL + "/#/edituserlist";
        }
      })
      .catch((error) => {
        if (error.response) {
          this.setState({
            errorMessageCouldntAdd: "Aggiunta dell'utente non riuscita",
          });
        }
      });
  };

  render() {
    const { username, email, telegramUsername, password } = this.state;
    return (
      <div className="citizen_signUp">
        <img
          src={require("../../img/logo_registrazione.png")}
          alt="Logo di ThiReMa"
          className="signup_logo"
        />
        <h1 className="white_signUp">Registrazione privato cittadino</h1>
        <form
          autoComplete="off"
          className="stileForm2 own_form"
          id="form"
          onSubmit={this.registerCitizen}
        >
          <input
            type="text"
            name="username"
            className="form-input"
            placeholder="Nome utente"
            value={username}
            onChange={this.onChangeValuePrivato}
          ></input>
          <p className="error-message-form">
            {this.state.errorMessageUsernameLength}
          </p>
          <p className="error-message-form">
            {this.state.errorMessageUsernameTooLong}
          </p>
          <input
            type="text"
            name="email"
            className="form-input"
            placeholder="Email"
            value={email}
            onChange={this.onChangeValuePrivato}
          ></input>
          <p className="error-message-form">
            {this.state.errorMessageEmailFormat}
          </p>
          <p className="error-message-form">
            {this.state.errorMessageEmailTooLong}
          </p>
          <input
            type="text"
            name="telegramUsername"
            className="form-input"
            placeholder="Telegram username"
            value={telegramUsername}
            onChange={this.onChangeValuePrivato}
          ></input>
          <p className="error-message-form">
            {this.state.errorMessageTelegramLength}
          </p>
          <p className="error-message-form">
            {this.state.errorMessageTelegramUsernameTooLong}
          </p>
          <input
            type="password"
            name="password"
            className="form-input"
            placeholder="Password"
            value={password}
            onChange={this.onChangeValuePrivato}
          ></input>
          <p className="error-message-form">
            {this.state.errorMessagePswLength}
          </p>
          <p className="error-message-form">
            {this.state.errorMessagePswTooLong}
          </p>
          <input
            type="password"
            name="confirmPsw"
            className="form-input"
            placeholder="Confirm Pasword"
            value={this.state.value}
            onChange={this.onChangeValuePrivato}
          ></input>
          <p className="error-message-form">
            {this.state.errorMessagePswDontMatch}
          </p>
          <br></br>
          <button
            type="submit"
            value="Registra l'utente"
            className="form-button"
            onClick={this.onAddPrivato}
          >
            Registra l'utente
          </button>
          <p className="error-message-form">
            {this.state.errorMessageCouldntAdd}
          </p>
        </form>
      </div>
    );
  }
}
