import React from "react";
import "../../style.css";
import Validator from "../utilities/Validator";
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

export default class UserInfo extends React.Component {
  constructor(props) {
    //index è nelle props
    super(props);
    this.state = {
      username: "",
      email: "",
      telegramUsername: "",
      password: "",
      confirmPsw: "",
      vatNumber: "",
      persons_2: [],
      errorMessage: "",
      errorMessageUsernameLength: "",
      errorMessageTelegramLength: "",
      errorMessagePswLength: "",
      errorMessageEmailFormat: "",
      errorMessageVatNumberLength: "",
      errorMessagePswTooLong: "",
      errorMessageEmailTooLong: "",
      errorMessageUsernameTooLong: "",
      errorMessageTelegramUsernameTooLong: "",
    };
    this.checkUsername = this.checkUsername.bind(this);
    this.checkTelegram = this.checkTelegram.bind(this);
    this.checkEmail = this.checkEmail.bind(this);
    this.checkVat = this.checkVat.bind(this);
  }

  componentDidMount() {
    const url = process.env.REACT_APP_BACKEND_URL + "/user";
    axios
      .get(url)
      .then((response) => {
        const persons_2 = response.data;
        this.setState({ persons_2 });
      })
      .catch(() => {
        this.setState({
          errorMessage: "Qualcosa è andato storto, riprova più tardi",
        });
      });
  }

  onChangeValueUsername = (event) => {
    const validator = new Validator();
    if (event.target.name === "username") {
      this.setState(
        {
          username: event.target.value,
        },
        () => {
          this.setState({
            errorMessageUsernameLength: validator.isUsernameLengthValid(
              this.state.username
            ),
            errorMessageUsernameTooLong: validator.isUsernameTooLong(
              this.state.username
            ),
          });
        }
      );
    }
  };

  onChangeValueEmail = (event) => {
    const validator = new Validator();
    if (event.target.name === "email") {
      this.setState(
        {
          email: event.target.value,
        },
        () => {
          this.setState({
            errorMessageEmailFormat: validator.isEmailValid(this.state.email),
            errorMessageEmailTooLong: validator.isEmailTooLong(
              this.state.email
            ),
          });
        }
      );
    }
  };

  onChangeValueTelegramUsername = (event) => {
    const validator = new Validator();

    if (event.target.name === "telegramUsername") {
      this.setState(
        {
          telegramUsername: event.target.value,
        },
        () => {
          this.setState({
            errorMessageTelegramLength: validator.isTelegramUsernameLengthValid(
              this.state.telegramUsername
            ),
            errorMessageTelegramUsernameTooLong: validator.isTelegramUsernameTooLong(
              this.state.telegramUsername
            ),
          });
        }
      );
    }
  };

  onChangeValueVat = (event) => {
    const validator = new Validator();
    if (event.target.name === "vatNumber") {
      this.setState(
        {
          vatNumber: event.target.value,
        },
        () => {
          this.setState({
            errorMessageVatNumberLength: validator.isVatNumberLongEnough(
              this.state.vatNumber
            ),
          });
        }
      );
    }
  };

  checkUsername = (index, oldUsername) => {
    if (this.state.username === "") {
      const updatedArray = this.state.persons_2.slice();
      updatedArray[Number(index)].username = oldUsername;
      this.setState({
        persons_2: updatedArray,
      });
    }
  };
  checkEmail = (index, oldEmail) => {
    if (this.state.email === "") {
      const updatedArray = this.state.persons_2.slice();
      updatedArray[Number(index)].email = oldEmail;
      this.setState({
        persons_2: updatedArray,
      });
    }
  };
  checkTelegram = (index, oldTelegramUsername) => {
    if (this.state.telegramUsername === "") {
      const updatedArray = this.state.persons_2.slice();
      updatedArray[Number(index)].telegramUsername = oldTelegramUsername;
      this.setState({
        persons_2: updatedArray,
      });
    }
  };

  checkVat = (index, oldVatNumber) => {
    if (this.state.vatNumber === "") {
      const updatedArray = this.state.persons_2.slice();
      updatedArray[Number(index)].vatNumber = oldVatNumber;
      this.setState({
        persons_2: updatedArray,
      });
    }
  };

  submitHandler = (e) => {
    e.preventDefault();
    const url = process.env.REACT_APP_BACKEND_URL + "/user";

    axios
      .get(url)
      .then((response) => {
        const persons_aux = response.data;
        this.setState({ persons_2: persons_aux });
      })
      .catch(() => {
        this.setState({
          errorMessage: "Qualcosa è andato storto: riprova più tardi",
        });
      });

    var indice = this.state.persons_2[this.props.index].id;
    const new_url = url + "/" + indice;

    var element = {};
    if (this.state.username !== "") {
      element.username = this.state.username;
    }
    if (this.state.telegramUsername !== "") {
      element.telegramUsername = this.state.telegramUsername;
    }
    if (this.state.email !== "") {
      element.email = this.state.email;
    }
    if (this.state.vatNumber !== "") {
      element.vatNumber = this.state.vatNumber;
    }

    axios
      .patch(new_url, element)
      .then((response) => {
        if (response.status !== "error") {
          window.location.reload(false);
        }
      })
      .catch(() => {
        this.setState({
          errorMessage: "Qualcosa è andato storto: riprova più tardi",
        });
      });
  };

  sendModifiedUser = (user) => {
    this.props.parentCallbackEditUser(user);
  };
  sendBoolean = () => {
    this.props.booleanParendCallBackEditUser(true);
  };

  render() {
    if (
      this.props.index === null ||
      this.props.index === undefined ||
      this.props.index.length === 0
    ) {
      return (
        <h1 className="white left-side-title">
          Seleziona un utente da modificare
        </h1>
      );
    } else {
      let index = this.props.index;
      return (
        <div className="user_info">
          <h1 className="white left-side-title">
            Modifica l'account di {this.state.persons_2[Number(index)].username}
          </h1>
          <form className="form_user" onSubmit={this.submitHandler}>
            <p className="error-message-form">{this.state.errorMessage}</p>
            <input
              type="text"
              name="username"
              className="form-input"
              value={this.state.value}
              placeholder={this.state.persons_2[Number(index)].username}
              onChange={this.onChangeValueUsername}
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
              value={this.state.value}
              placeholder={this.state.persons_2[Number(index)].email}
              onChange={this.onChangeValueEmail}
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
              value={this.state.value}
              placeholder={this.state.persons_2[Number(index)].telegramUsername}
              onChange={this.onChangeValueTelegramUsername}
            ></input>
            <p className="error-message-form">
              {this.state.errorMessageTelegramLength}
            </p>
            <p className="error-message-form">
              {this.state.errorMessageTelegramUsernameTooLong}
            </p>
            <input
              type="numbers"
              name="vatNumber"
              className="form-input"
              value={this.state.value}
              placeholder={this.state.persons_2[Number(index)].vatNumber}
              onChange={this.onChangeValueVat}
            ></input>
            <p className="error-message-form">
              {this.state.errorMessageVatNumberLength}
            </p>
            <br></br>
            <button
              type="submit"
              value="Registra l'utente"
              className="form-button"
            >
              Modifica l'utente
            </button>
          </form>
        </div>
      );
    }
  }
}
