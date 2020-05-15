import React from "react";
import "../../style.css";
import ValidatorDeviceData from "../utilities/ValidatorDeviceData";
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
/**
 * questa classe mi serve solo per ora che non ci sono le API
 */
export default class NewDevice extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      gateways: [],
      name: "",
      selectedFrequencyType: "seconds",
      frequency: "",
      ipAddress: "",
      brand: "",
      model: "",
      gatewayId: 1, //metto un valore di default
      errorMessageNameTooShort: "",
      errorMessageIpFormatIsWrong: "",
      errorMessageBrandIsTooShort: "",
      errorMessageModelIsTooShort: "",
      errorMessageCouldntAdd: "",
      errorMessageFrequencyFormat: "",
      errorMessageGatewayFormat: "",
      isLoading: false,
      newDeviceId: "",
      errorMessageNameTooLong: "",
      errorMessageBrandTooLong: "",
      errorMessageModelTooLong: "",
    };
    this.frequencyType = this.frequencyType.bind(this);
  }

  onChangeValueName = (event) => {
    const dataValidator = new ValidatorDeviceData();
    if (event.target.name === "name") {
      this.setState(
        {
          errorMessageNameTooShort: "",
          name: event.target.value,
        },
        () => {
          this.setState({
            errorMessageNameTooShort: dataValidator.deviceNameLengthCheck(
              this.state.name
            ),
            errorMessageNameTooLong: dataValidator.deviceNameTooLong(
              this.state.name
            ),
          });
        }
      );
    }
  };

  /**
   * si chiama ip ma mi fa anche il check per l'UUID
   */
  onChangeValueIp = (event) => {
    const dataValidator = new ValidatorDeviceData();
    if (event.target.name === "ip") {
      this.setState(
        {
          errorMessageIpFormatIsWrong: "",
          ipAddress: event.target.value,
        },
        () => {
          this.setState({
            errorMessageIpFormatIsWrong: dataValidator.ipAndUuidFormatCheck(
              this.state.ipAddress
            ),
          });
        }
      );
    }
  };

  onChangeValueBrand = (event) => {
    const dataValidator = new ValidatorDeviceData();
    if (event.target.name === "brand") {
      this.setState(
        {
          errorMessageBrandIsTooShort: "",
          brand: event.target.value,
        },
        () => {
          this.setState({
            errorMessageBrandIsTooShort: dataValidator.deviceBrandCheck(
              this.state.brand
            ),
            errorMessageBrandTooLong: dataValidator.brandTooLong(
              this.state.brand
            ),
          });
        }
      );
    }
  };

  onChangeValueModel = (event) => {
    const dataValidator = new ValidatorDeviceData();
    if (event.target.name === "model") {
      this.setState(
        {
          errorMessageModelIsTooShort: "",
          model: event.target.value,
        },
        () => {
          this.setState({
            errorMessageModelIsTooShort: dataValidator.deviceModelCheck(
              this.state.model
            ),
            errorMessageModelTooLong: dataValidator.modelTooLong(
              this.state.model
            ),
          });
        }
      );
    }
  };

  onChangeValueFrequency = (event) => {
    const dataValidator = new ValidatorDeviceData();
    if (this.state.selectedFrequencyType === "minutes") {
      const frequency = event.target.value * 60;
      this.setState({ errorMessageFrequencyFormat: "", frequency }, () => {
        this.setState({
          errorMessageFrequencyFormat: dataValidator.frequencyCheck(
            this.state.frequency
          ),
        });
      });
    } else {
      this.setState(
        { errorMessageFrequencyFormat: "", frequency: event.target.value },
        () => {
          this.setState({
            errorMessageFrequencyFormat: dataValidator.frequencyCheck(
              this.state.frequency
            ),
          });
        }
      );
    }
  };

  onChangeValueGateway = (event) => {
    this.setState({ gatewayId: event.target.value });
  };
  //mi serve per la select box se l'utente seleziona minuti o secondi
  frequencyType = (event) => {
    this.setState({ frequency: event.target.value });
  };

  sendIdOfNewDevice = (id) => {
    this.props.parentCallbackNewId(id);
  };

  componentDidMount() {
    this.setState({ isLoading: true });
    const url = process.env.REACT_APP_BACKEND_URL + "/user/gateway";

    axios
      .get(url)
      .then((result) => {
        let gatewaysArray = [];
        gatewaysArray = result.data;
        this.setState({
          gateways: gatewaysArray,
          isLoading: false,
        });
      })
      .catch((error) =>
        this.setState({
          error,
          isLoading: false,
        })
      );
  }

  addDevice = (event) => {
    event.preventDefault();
    if (
      this.state.name === "" ||
      this.state.frequency === "" ||
      this.state.ipAddress === "" ||
      this.state.brand === "" ||
      this.state.model === ""
    ) {
      this.setState({
        errorMessageCouldntAdd:
          "Aggiunta non riuscita: c'è almeno un campo vuoto",
      });
      return;
    }
    if (
      this.state.errorMessageNameTooShort === "" &&
      this.state.errorMessageIpFormatIsWrong === "" &&
      this.state.errorMessageModelIsTooShort === "" &&
      this.state.errorMessageBrandIsTooShort === "" &&
      this.state.errorMessageNameTooLong === "" &&
      this.state.errorMessageBrandTooLong === "" &&
      this.state.errorMessageModelTooLong === ""
    ) {
      const newDevice = {
        name: this.state.name,
        frequency: this.state.frequency,
        ipAddress: this.state.ipAddress,
        brand: this.state.brand,
        model: this.state.model,
        gatewayId: Number(this.state.gatewayId),
      };
      const API_ADD_DEVICE = process.env.REACT_APP_BACKEND_URL + "/user/device";
      axios
        .post(API_ADD_DEVICE, newDevice)
        .then((response) => {
          if (response.status === 201) {
            const idNewDevice = response.data.id;
            this.props.parentCallbackNowAddConfig(true);
            this.sendIdOfNewDevice(Number(idNewDevice));
          }
        })
        .catch((error) => {
          if (error.response) {
            this.setState({
              errorMessageCouldntAdd: "Aggiunta del device non riuscita",
            });
          }
        });
    }
  };

  render() {
    if (!this.state.isLoading) {
      return (
        <div>
          <form className="form_user" onSubmit={this.addDevice}>
            <h1 className="white left-side-title">Aggiungi un device</h1>

            <label for="name" className="label_before_input">
              Inserisci il nome del device{" "}
            </label>
            <input
              type="text"
              name="name"
              className="form-input_new_device"
              value={this.state.value}
              placeholder="Inserisci il nome del device"
              onChange={this.onChangeValueName}
            ></input>
            <p className="error-message-form padding">
              {this.state.errorMessageNameTooShort}
            </p>
            <p className="error-message-form padding">
              {this.state.errorMessageNameTooLong}
            </p>
            <div>
              <label for="name" className="label_before_input">
                Inserisci la frequenza di aggiornamento
              </label>
              <input
                type="text"
                name="frequency"
                className="form-input_new_device"
                value={this.state.value}
                placeholder="Inserisci la frequenza di aggiornamento dei dati"
                onChange={this.onChangeValueFrequency}
              ></input>
              <select
                value={this.state.value}
                onChange={this.frequencyType}
                className="form-input_new_device add_low_margin"
              >
                <option value="seconds">Secondi</option>
                <option value="minutes">Minuti </option>
              </select>
            </div>
            <p className="error-message-form padding">
              {this.state.errorMessageFrequencyFormat}
            </p>
            <label for="name" className="label_before_input">
              Inserisci l'indirizzo IP/UUID del device{" "}
            </label>
            <input
              type="text"
              name="ip"
              className="form-input_new_device"
              value={this.state.value}
              placeholder="Inserisci l'indirizzo IP/UUID del device"
              onChange={this.onChangeValueIp}
            ></input>
            <p className="error-message-form padding">
              {this.state.errorMessageIpFormatIsWrong}
            </p>

            <label for="name" className="label_before_input">
              Inserisci la marca del device{" "}
            </label>
            <input
              type="text"
              name="brand"
              className="form-input_new_device"
              value={this.state.value}
              placeholder="Inserisci la marca del device"
              onChange={this.onChangeValueBrand}
            ></input>
            <p className="error-message-form padding">
              {this.state.errorMessageBrandIsTooShort}
            </p>
            <p className="error-message-form padding">
              {this.state.errorMessageBrandTooLong}
            </p>

            <label for="name" className="label_before_input">
              Inserisci il modello del device{" "}
            </label>
            <input
              type="text"
              name="model"
              className="form-input_new_device"
              value={this.state.value}
              placeholder="Inserisci il modello del device"
              onChange={this.onChangeValueModel}
            ></input>
            <p className="error-message-form padding">
              {this.state.errorMessageModelIsTooShort}
            </p>
            <p className="error-message-form padding">
              {this.state.errorMessageModelTooLong}
            </p>
            <div>
              <label htmlFor="gateway" className="label_before_input">
                Scegli un gateway:
              </label>
              <select
                id="gateway"
                className="form-input_new_device"
                onChange={this.onChangeValueGateway}
              >
                {this.state.gateways.map((gateway) => (
                  <option value={gateway.id} key={gateway.id}>
                    {gateway.id}
                  </option>
                ))}
              </select>
            </div>
            <br></br>
            <button
              type="submit"
              value="Aggiungi il device"
              className="form-button"
            >
              Aggiungi il device
            </button>
            <p className="error-message-form padding">
              {this.state.errorMessageCouldntAdd}
            </p>
          </form>
        </div>
      );
    } else {
      return <h1>Sto caricando la lista dei gateway...</h1>;
    }
  }
}
