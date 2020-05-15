import React from "react";
import "../../style.css";
import { Icon } from "@iconify/react";
//import sharpAccountCircle from "@iconify/icons-ic/sharp-account-circle";
import bxsPencil from "@iconify/icons-bx/bxs-pencil";
import deleteFilled from "@iconify/icons-ant-design/delete-filled";
import baselineAdd from "@iconify/icons-ic/baseline-add";
import databaseIcon from "@iconify/icons-jam/database";
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

export default class DevicesInfo extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      devices: [],
      isLoading: false,
      error: null,
      editedDeviceData: "",
      editedDeviceIndex: "",
    };
  }

  componentDidMount() {
    this.setState({ isLoading: true });
    const url = process.env.REACT_APP_BACKEND_URL + "/user/device";

    axios
      .get(url)
      .then((result) => {
        this.setState({
          devices: result.data,
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

  onRemoveDevice = (index) => {
    // OCCHIO CHE L'1 NELLA RICHIESTA API ANDRA' MODIFICATA CON L'ID DELL'UTENTE CON LA SESSIONE ATTIVA
    const deviceId = this.state.devices[Number(index)].id;
    const url = process.env.REACT_APP_BACKEND_URL + "/user/device/" + deviceId;

    const newDevicesArray = this.state.devices;
    this.setState({
      devices: newDevicesArray.filter((device) => device.id !== deviceId),
    });
    axios.delete(url).then((response) => {
      if (response.status === "error") {
        // se c'è stato un array, rimetto dentro a devices l'array senza l'eliminazione eseguita
        this.setState({
          devices: newDevicesArray,
        });
      } else {
        window.location.reload(false);
      }
    });
    // in seguito devo rifare una get perché se elimino un device e poi ne aggiungo un altro mi sfasa gli id!

    const url_get = process.env.REACT_APP_BACKEND_URL + "/user/device";

    axios
      .get(url_get)
      .then((result) => {
        this.setState({
          devices: result.data,
          isLoading: false,
        });
      })
      .catch((error) =>
        this.setState({
          error,
          isLoading: false,
        })
      );
  };

  //necessaria per la callback per l'aggiunta di un device
  addDevice = () => {
    this.props.parentCallbackIsThereADeviceToAdd(true);
  };

  //necessaria per la callback per la modifica di un device, mi passa l'indice del device ricevuto
  idOfElementToModify = (idRecieved) => {
    this.props.id(idRecieved);
  };

  idOfConfigurationToModify = (idRecieved) => {
    this.props.idConfigToModify(idRecieved);
  };

  passTheIdToShowGraphs = (id) => {
    this.props.idForGraphs(id);
  };

  passTheNameToShowGraphs = (name) => {
    this.props.nameForGraphs(name);
  };

  render() {
    if (this.state.error) {
      return (
        <h1 className="white left-side-title">{this.state.error.message}</h1>
      );
    }
    if (this.state.isLoading) {
      return (
        <h1 className="white left-side-title">
          Sto caricando la lista dei device
        </h1>
      );
    }
    return (
      <div className="device-list">
        <div className="title-device-list">
          <h2 className="white left-side-title">I tuoi dispositivi</h2>
          <button title="Aggiungi un device" className="no-bg-button">
            <Icon
              icon={baselineAdd}
              className="white button-user-list add-device pointer"
              onClick={() => {
                this.addDevice();
              }}
            />
          </button>
        </div>

        <div className="device-list-container">
          {this.state.devices.map((device, index) => (
            <div className="single-device" key={device.id}>
              <p
                className="device-name pointer"
                onClick={() => {
                  this.passTheIdToShowGraphs(device.id);
                  this.passTheNameToShowGraphs(device.name);
                }}
              >
                {device.name}
              </p>
              <div className="device-list-buttons">
                <button
                  className="button-user-list white"
                  type="button"
                  title="Modifica i dati"
                  onClick={() => this.idOfElementToModify(Number(device.id))}
                >
                  <Icon icon={bxsPencil} className="icon-user-list" />
                </button>
                <button
                  className="button-user-list white"
                  type="button"
                  title="Modifica la configurazione"
                  onClick={() =>
                    this.idOfConfigurationToModify(Number(device.id))
                  }
                >
                  <Icon icon={databaseIcon} className="icon-user-list" />
                </button>
                <button
                  className="button-user-list white"
                  type="button"
                  title="Elimina il device"
                  onClick={() => {
                    this.onRemoveDevice(index);
                  }}
                >
                  <Icon icon={deleteFilled} className="icon-user-list" />
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    );
  }
}
