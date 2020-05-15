import React from "react";
import "../../style.css";
import axios from "axios";
import ValidatorDeviceData from "../utilities/ValidatorDeviceData";

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

export default class EditDeviceData extends React.Component {
  constructor(props) {
    //this.props.indexOfDevice contiene l'indice dell'elemento che voglio modificare
    super(props);
    this.state = {
      devices: [],
      name: "",
      frequency: "",
      brand: "",
      model: "",
      isLoading: false,
      error: null,
      deviceImEditing: "",
      errorMessageNameTooShort: "",
      errorMessageIpFormatIsWrong: "",
      errorMessageBrandIsTooShort: "",
      errorMessageModelIsTooShort: "",
      errorMessageFrequencyFormat: "",
      errorMessageUpdate: "",
      errorMessageNameTooLong: "",
      errorMessageBrandTooLong: "",
      errorMessageModelTooLong: "",
      confirmModification: "",
    };
    this.frequencyType = this.frequencyType.bind(this);
  }

  componentDidMount() {
    this.setState({ isLoading: true });
    const url = process.env.REACT_APP_BACKEND_URL + "/user/device";
    axios
      .get(url)
      .then((result) => {
        const stupidArray = result.data.filter(
          (result) => result.id === this.props.id
        );
        this.setState({
          devices: result.data,
          isLoading: false,
          deviceImEditing: stupidArray[0],
        });
      })
      .catch((error) => this.setState({ error, isLoading: false }));
  }
  onChangeValueName = (event) => {
    const dataValidator = new ValidatorDeviceData();
    if (event.target.name === "name") {
      this.setState(
        {
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

  onChangeValueBrand = (event) => {
    const dataValidator = new ValidatorDeviceData();
    if (event.target.name === "brand") {
      this.setState(
        {
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

  //mi serve per la select box se l'utente seleziona minuti o secondi
  frequencyType = (event) => {
    this.setState({ frequency: event.target.value });
  };

  onChangeValueFrequency = (event) => {
    const dataValidator = new ValidatorDeviceData();
    if (this.state.selectedFrequencyType === "minutes") {
      const frequency = event.target.value * 60;
      this.setState({ frequency }, () => {
        this.setState({
          errorMessageFrequencyFormat: dataValidator.frequencyCheck(
            this.state.frequency
          ),
        });
      });
    } else {
      this.setState({ frequency: event.target.value }, () => {
        this.setState({
          errorMessageFrequencyFormat: dataValidator.frequencyCheck(
            this.state.frequency
          ),
        });
      });
    }
  };

  editDevice = (e) => {
    e.preventDefault();

    if (
      this.state.errorMessageNameTooShort === "" &&
      //this.state.errorMessageIpFormatIsWrong === "" &&
      this.state.errorMessageBrandIsTooShort === "" &&
      this.state.errorMessageModelIsTooShort === "" &&
      this.state.errorMessageFrequencyFormat === "" &&
      this.state.errorMessageNameTooLong === "" &&
      this.state.errorMessageBrandTooLong === "" &&
      this.state.errorMessageModelTooLong === ""
    ) {
      const API_EDIT_DEVICE =
        process.env.REACT_APP_BACKEND_URL + "/user/device/" + this.props.id;
      var element = {};
      if (this.state.name !== "") {
        element.name = this.state.name;
      }
      if (this.state.frequency !== "") {
        element.frequency = this.state.frequency;
      }
      if (this.state.brand !== "") {
        element.brand = this.state.brand;
      }
      if (this.state.model !== "") {
        element.model = this.state.model;
      }

      axios.patch(API_EDIT_DEVICE, element).catch(() => {
        this.setState({
          errorMessageUpdate: "Modifica del device non riuscita",
        });
      });
      this.setState({
        confirmModification: "Modifica avvenuta con successo",
      });
    }
  };

  render() {
    const { isLoading, error } = this.state;
    if (isLoading || this.state.deviceImEditing === undefined) {
      return <h1>Sto caricando i dati</h1>;
    }
    if (error) {
      return <h1>{error}</h1>;
    }
    if (this.state.isLoading === false) {
      return (
        <div className="form_container">
          <form className="form_user" onSubmit={this.editDevice}>
            <h1 className="white left-side-title">
              Stai modificando il device {this.state.deviceImEditing.name}
            </h1>

            <label htmlFor="name" className="label_before_input">
              Modifica il nome del device{" "}
            </label>
            <input
              type="text"
              name="name"
              className="form-input_new_device"
              value={this.state.value}
              placeholder={this.state.deviceImEditing.name}
              onChange={this.onChangeValueName}
            />
            <p className="error-message-form padding">
              {this.state.errorMessageNameTooShort}
            </p>
            <p className="error-message-form padding">
              {this.state.errorMessageNameTooLong}
            </p>
            <div>
              <label htmlFor="name" className="label_before_input">
                Modifica la frequenza di aggiornamento{" "}
              </label>
              <input
                type="text"
                name="frequency"
                className="form-input_new_device"
                value={this.state.value}
                placeholder={this.state.deviceImEditing.frequency}
                onChange={this.onChangeValueFrequency}
              />
              <select
                value={this.state.value}
                className="form-input_new_device frequency_options"
                onChange={this.frequencyType}
              >
                <option value="seconds">Secondi</option>
                <option value="minutes">Minuti </option>
              </select>
            </div>
            <p className="error-message-form padding">
              {this.state.errorMessageFrequencyFormat}
            </p>

            <label htmlFor="name" className="label_before_input">
              Modifica il brand del dispositivo{" "}
            </label>
            <input
              type="text"
              name="brand"
              className="form-input_new_device"
              value={this.state.value}
              placeholder={this.state.deviceImEditing.brand}
              onChange={this.onChangeValueBrand}
            />
            <p className="error-message-form padding">
              {this.state.errorMessageBrandIsTooShort}
            </p>
            <p className="error-message-form padding">
              {this.state.errorMessageBrandTooLong}
            </p>
            <label htmlFor="name" className="label_before_input">
              Modifica il modello del dispositivo{" "}
            </label>
            <input
              type="text"
              name="model"
              className="form-input_new_device"
              value={this.state.value}
              placeholder={this.state.deviceImEditing.model}
              onChange={this.onChangeValueModel}
            />
            <p className="error-message-form padding">
              {this.state.errorMessageModelIsTooShort}
            </p>
            <p className="error-message-form padding">
              {this.state.errorMessageModelTooLong}
            </p>
            <br></br>
            <button
              type="submit"
              value="Modifica il device"
              className="form-button"
            >
              Modifica il device
            </button>
            <p className="confirmation-message">
              {this.state.confirmModification}
            </p>
            <p className="error-message-form padding">
              {this.state.errorMessageUpdate}
            </p>
          </form>
        </div>
      );
    }
  }
}
