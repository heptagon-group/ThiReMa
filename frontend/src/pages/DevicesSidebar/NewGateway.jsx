import React from "react";
import "../../style.css";
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
export default class NewGateway extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      gatewayId: "",
      errorMessageCouldntAdd: "",
    };
  }

  onChangeGatewayId = (event) => {
    if (event.target.name === "gatewayId") {
      this.setState({
        gatewayId: event.target.value,
      });
    }
  };

  addGateway = (event) => {
    event.preventDefault();
    if (this.state.errorMessageCouldntAdd === "") {
      const API_ADD_DEVICE = process.env.REACT_APP_BACKEND_URL + "/user/device";
      axios.get(API_ADD_DEVICE).catch((error) => {
        if (error.response) {
          this.setState({
            errorMessageCouldntAdd: "Aggiunta del gateway non riuscita",
          });
        }
      });
    }
  };

  render() {
    return (
      <div>
        <form className="form_user" onSubmit={this.addGateway}>
          <h1 className="white left-side-title">Aggiungi un gateway</h1>
          <input
            type="text"
            name="gatewayId"
            className="form-input"
            value={this.state.value}
            placeholder="Inserisci il nome del gateway"
            onChange={this.onChangeGatewayId}
          ></input>

          <br></br>
          <button
            type="submit"
            value="Aggiungi il device"
            className="form-button"
            /*onClick={() => {
              this.props.parentCallbackNowAddConfig();
            }}*/
          >
            Aggiungi il gateway
          </button>
          <p className="error-message-form">
            {this.state.errorMessageCouldntAdd}
          </p>
        </form>
      </div>
    );
  }
}
