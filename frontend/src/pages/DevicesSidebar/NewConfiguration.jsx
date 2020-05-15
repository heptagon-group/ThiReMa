import React from "react";
import "../../style.css";
import axios from "axios";
import ValidatorDeviceConfig from "../utilities/ValidatorDeviceConfig";
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

export default class NewConfiguration extends React.Component {
  //this.props.idOfDevice contiene l'id del device appena aggiunto
  constructor(props) {
    super(props);
    this.state = {
      name: "",
      format: "",
      threshold: "",
      isInfluencing: false,
      thresholdGreater: true, // di default maggiore di
      errorMessageNameTooShort: "",
      errorMessageFormatIsWrong: "",
      errorMessageThreshold: "",
      errorMessageNameTooLong: "",
    };
  }
  onChangeValueName = (event) => {
    const configValidator = new ValidatorDeviceConfig();

    if (event.target.name === "name") {
      this.setState(
        {
          name: event.target.value,
        },
        () => {
          this.setState({
            errorMessageNameTooShort: configValidator.configNameLengthCheck(
              this.state.name
            ),
            errorMessageNameTooLong: configValidator.configNameTooLong(
              this.state.name
            ),
          });
        }
      );
    }
  };

  onChangeValueFormat = (event) => {
    const configValidator = new ValidatorDeviceConfig();
    if (event.target.name === "format") {
      this.setState(
        {
          errorMessageFormatIsWrong: "",
          format: event.target.value,
        },
        () => {
          if (!configValidator.checkIfIsNotNumber(this.state.format)) {
            this.setState({
              errorMessageFormatIsWrong: "Inserire un'unità di misura valida",
            });
          }
        }
      );
    }
  };

  onChangeValueThreshold = (event) => {
    const configValidator = new ValidatorDeviceConfig();
    if (event.target.name === "threshold") {
      this.setState(
        {
          errorMessageThreshold: "",
          threshold: event.target.value,
        },
        () => {
          if (configValidator.checkIfIsNotNumber(this.state.threshold)) {
            this.setState({
              errorMessageThreshold: "Inserire un numero",
            });
          }
        }
      );
    }
  };

  onChangeInfluence = () => {
    if (document.getElementById("isInfluencingCheckbox").checked) {
      this.setState({ isInfluencing: true });
    } else {
      this.setState({ isInfluencing: false });
    }
  };

  addConfiguration = (event) => {
    event.preventDefault();
    if (
      this.state.name === "" ||
      this.state.format === "" ||
      this.state.threshold === ""
    ) {
      this.setState({
        errorMessageCouldntAdd:
          "C'è almeno un campo vuoto: aggiunta della configurazione non riuscita",
      });
      return;
    }
    if (
      this.state.errorMessageNameTooShort === "" &&
      this.state.errorMessageFormatIsWrong === "" &&
      this.state.errorMessageThreshold === ""
    ) {
      const newConfig = {
        name: this.state.name,
        format: this.state.format,
        threshold: this.state.threshold,
        influential: this.state.isInfluencing,
        thresholdGreater: this.state.thresholdGreater,
      };
      const API_ADD_CONFIG =
        process.env.REACT_APP_BACKEND_URL +
        "/user/device/" +
        this.props.idOfDevice +
        "/config";
      axios
        .post(API_ADD_CONFIG, newConfig)
        .then((response) => {
          if (response.status === 201) {
            this.props.parentCallbackNowAddConfig(true);
          }
        })
        .catch((error) => {
          if (error.response) {
            this.setState({
              errorMessageCouldntAdd:
                "Aggiunta della configurazione non riuscita",
            });
          }
        });
    }
  };

  //mi serve per la select box se l'utente seleziona "maggiore di" o "minore di"
  thresholdGreaterOrLesser = (event) => {
    if (event.target.value === "greater") {
      this.setState({ thresholdGreater: true });
    }
    this.setState({ thresholdGreater: false });
  };

  refreshName = () => {
    document.getElementById("config-input").value = "";
  };
  refreshFormat = () => {
    document.getElementById("config-input-format").value = "";
  };
  refreshThreshold = () => {
    document.getElementById("config-input-threshold").value = "";
  };
  refreshCheckbox = () => {
    document.getElementById("isInfluencingCheckbox").checked = false;
  };

  render() {
    return (
      <div className="form_container">
        <form className="form-configuration" onSubmit={this.addConfiguration}>
          <h1 className="white left-side-title add_margin">
            Aggiungi una configurazione
          </h1>

          <label for="name" className="label_before_input">
            Inserisci il nome di questa configurazione{" "}
          </label>
          <input
            type="text"
            name="name"
            className="form-input_new_device"
            value={this.state.name}
            placeholder="Inserisci il nome di questa configurazione"
            onChange={this.onChangeValueName}
            id="config-input"
          ></input>
          <p className="error-message-form padding">
            {this.state.errorMessageNameTooShort}
          </p>
          <p className="error-message-form padding">
            {this.state.errorMessageNameTooLong}
          </p>
          <label for="name" className="label_before_input">
            Inserisci il formato{" "}
          </label>
          <input
            type="text"
            name="format"
            className="form-input_new_device"
            value={this.state.format}
            placeholder="Inserisci il formato"
            onChange={this.onChangeValueFormat}
            id="config-input-format"
          ></input>
          <p className="error-message-form padding">
            {this.state.errorMessageFormatIsWrong}
          </p>

          <label for="name" className="label_before_input">
            Inserisci la soglia di sicurezza{" "}
          </label>
          <select
            value={this.state.value}
            onChange={this.thresholdGreaterOrLesser}
            className="form-input_new_device add_low_margin"
          >
            <option value="greater">Maggiore di</option>
            <option value="lesser">Minore di </option>
          </select>
          <input
            type="text"
            name="threshold"
            className="form-input_new_device"
            value={this.state.threshold}
            placeholder="Inserisci la soglia di sicurezza"
            onChange={this.onChangeValueThreshold}
            id="config-input-threshold"
          ></input>
          <p className="error-message-form padding">
            {this.state.errorMessageThreshold}
          </p>
          <div className="checkbox-config2">
            <label htmlFor="isInfluencingCheckbox" className="label_color">
              È un dato influenzante?
            </label>
            <input
              type="checkbox"
              name="isInfluencing"
              className="margin-button"
              value={this.state.isInfluencing}
              id="isInfluencingCheckbox"
              onClick={this.onChangeInfluence}
            ></input>
          </div>
          <button
            type="submit"
            className="form-button"
            value="aggiungi la configurazione"
            onClick={() => {
              this.refreshName();
              this.refreshFormat();
              this.refreshThreshold();
              this.refreshCheckbox();
            }}
          >
            Aggiungi la configurazione
          </button>
          <p className="error-message-form padding">
            {this.state.errorMessageCouldntAdd}
          </p>
        </form>
      </div>
    );
  }
}
