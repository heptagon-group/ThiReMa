// to do
import React from "react";
import "../../style.css";
import { Icon } from "@iconify/react";

import deleteFilled from "@iconify/icons-ant-design/delete-filled";
import ValidatorDeviceConfig from "../utilities/ValidatorDeviceConfig";
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

export default class EditConfiguration extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      deviceConfig: [],
      deviceNewData: [],
      errorMessageNameTooShort: null,
      errorMessageFormatIsWrong: null,
      errorMessageThreshold: null,
      isLoading: false,
      hasBeenEdited: [],
      hasListBenLoaded: false,
      errorMessageUpdate: null,
      errorMessageFieldTooLong: null,
      confirmModification: null,
    };
  }

  componentDidMount() {
    const id = Number(this.props.idOfDevice);
    //this.props.indexOfDevice contiene l'indice del device di cui voglio modificare la configurazione
    //const index = this.setState({ isLoading: true });
    const API_GET_DEVICE_CONFIG =
      process.env.REACT_APP_BACKEND_URL + "/user/device/" + id + "/config";

    axios
      .get(API_GET_DEVICE_CONFIG)
      .then((result) => {
        this.setState(
          {
            configArray: result.data,
            isLoading: false,
          },
          () => {
            let arrayEditedId = [];
            for (let i = 0; i < this.state.configArray.length; ++i) {
              arrayEditedId[i] = null;
            }
            this.setState({
              hasBeenEdited: arrayEditedId,
            });
          }
        );
      })
      .catch((error) =>
        this.setState({
          error,
          isLoading: false,
        })
      );

    // inizializzo a falso l'array che mi dice se ho modificato qualcosa
  }

  getIndex = (configId) => {
    const tempArray = this.state.configArray.slice();
    for (let i = 0; i < tempArray.length; ++i) {
      if (tempArray[i].id === configId) return i;
    }
  };

  onChangeValueFormat = (event, configId) => {
    const configValidator = new ValidatorDeviceConfig();

    if (event.target.name === "format") {
      const tempArray = this.state.configArray.slice();
      const index = this.getIndex(configId);
      tempArray[index].format = event.target.value;
      const tempEdited = this.state.hasBeenEdited.slice();
      tempEdited[index] = configId;
      this.setState(
        {
          configArray: tempArray,
          hasBeenEdited: tempEdited,
          errorMessageFormatIsWrong: "",
        },
        () => {
          if (!configValidator.checkIfIsNotNumber(tempArray[index].format)) {
            this.setState({
              errorMessageFormatIsWrong:
                "Inserire un'unità di misura valida nel formato della configurazione numero " +
                Number(index + 1),
            });
          }
        }
      );
    }
  };
  onChangeValueThreshold = (event, configId) => {
    const configValidator = new ValidatorDeviceConfig();

    if (event.target.name === "threshold") {
      const tempArray = this.state.configArray.slice();
      const index = this.getIndex(configId);
      tempArray[index].threshold = event.target.value;
      const tempEdited = this.state.hasBeenEdited.slice();
      tempEdited[index] = configId;
      this.setState(
        {
          configArray: tempArray,
          hasBeenEdited: tempEdited,
          errorMessageThreshold: "",
        },
        () => {
          if (configValidator.checkIfIsNotNumber(tempArray[index].threshold)) {
            this.setState({
              errorMessageThreshold:
                "Inserire un numero nel threshold della configurazione numero " +
                Number(index + 1),
            });
          }
        }
      );
    }
  };

  onChangeInfluence = (configId) => {
    const tempArray = this.state.configArray;
    const tempEdited = this.state.hasBeenEdited;
    const index = this.getIndex(configId);
    tempEdited[index] = configId;
    if (document.getElementById("isInfluencingCheckbox").checked) {
      tempArray[index].isInfluencing = true;
      this.setState({
        configArray: tempArray,
        hasBeenEdited: tempEdited,
      });
    } else {
      tempArray[index].isInfluencing = false;
      this.setState({
        configArray: tempArray,
        hasBeenEdited: tempEdited,
      });
    }
  };

  thresholdGreaterOrLesser = (event, configId) => {
    const tempArray = this.state.configArray.slice();
    const index = this.getIndex(configId);
    if (event.target.value === "greater") {
      tempArray[index].thresholdGreater = true;
    } else {
      tempArray[index].thresholdGreater = false;
    }
    const tempEdited = this.state.hasBeenEdited.slice();
    tempEdited[index] = configId;
    this.setState({
      configArray: tempArray,
      hasBeenEdited: tempEdited,
    });
  };

  editConfig = () => {
    if (
      this.state.errorMessageFormatIsWrong === "" &&
      this.state.errorMessageThreshold === "" &&
      this.state.errorMessageFieldTooLong === ""
    ) {
      //ogni elemento di hasBeenEdited contiene l'id di una configurazione che ho modificato. o c'è dentro l'id è false.
      const idOfDevice = this.props.idOfDevice;
      this.state.hasBeenEdited.forEach((idOfConfig) => {
        // nelle props devo avere l'id del device che sto modificando => non l'indice ma l'id
        if (idOfConfig !== false) {
          /*se dentro ad element c'è un id, vado nell'array con le configurazioni e mi salvo quella configurazione, per poi mandarla alle api*/
          const API_EDIT_CONFIG =
            process.env.REACT_APP_BACKEND_URL +
            "/user/device/" +
            idOfDevice +
            "/config/" +
            idOfConfig;
          this.state.configArray.forEach((element) => {
            if (element.id === idOfConfig) {
              const newConfig = {
                format: element.format,
                threshold: element.threshold,
                influential: element.isInfluencing,
                thresholdGreater: element.thresholdGreater,
              };
              axios
                .patch(API_EDIT_CONFIG, newConfig)
                .then((response) => {
                  if (response.status !== "error") {
                    window.location.reload();
                  }
                })
                .catch(() => {
                  this.setState({
                    errorMessageUpdate:
                      "Modifica della configurazione non riuscita",
                  });
                });
              this.setState({
                confirmModification: "Modifica avvenuta con successo",
              });
            }
          });
        }
      });
    }
  };

  reloadConfigList = () => {
    const idOfDevice = Number(this.props.idOfDevice);
    const API_GET_DEVICE_CONFIG =
      process.env.REACT_APP_BACKEND_URL +
      "/user/device/" +
      idOfDevice +
      "/config";

    axios
      .get(API_GET_DEVICE_CONFIG)
      .then((result) => {
        this.setState(
          {
            configArray: result.data,
            isLoading: false,
            hasListBenLoaded: true,
          },
          () => {
            let boolArray = [];
            for (let i = 0; i < this.state.configArray.length; ++i) {
              boolArray[i] = false;
            }
            this.setState({
              hasBeenEdited: boolArray,
            });
          }
        );
      })
      .catch((error) =>
        this.setState({
          error,
          isLoading: false,
        })
      );
  };

  onRemoveConfig = (configId) => {
    const idOfDevice = this.props.idOfDevice;
    const url =
      process.env.REACT_APP_BACKEND_URL +
      "/user/device/" +
      idOfDevice +
      "/config/" +
      configId;

    const newConfigArray = this.state.configArray;
    this.setState({
      configArray: newConfigArray.filter((config) => config.id !== configId),
    });
    axios.delete(url).then((response) => {
      if (response.status === "error") {
        // se c'è stato un array, rimetto dentro a devices l'array senza l'eliminazione eseguita
        this.setState({
          configArray: newConfigArray,
        });
      } else {
        window.location.reload(false);
      }
    });
  };

  render() {
    if (this.state.isLoading === false && this.state.configArray) {
      if (!this.state.hasListBenLoaded) {
        this.reloadConfigList();
      }
      return (
        <div className="container-configurations">
          <h1 className="white left-side-title">
            Modifica le configurazioni associate al dispositivo
          </h1>
          {this.state.errorMessageThreshold ? (
            <p className="error-message-form padding">
              {this.state.errorMessageThreshold}{" "}
            </p>
          ) : null}
          {this.state.errorMessageFieldTooLong ? (
            <p className="error-message-form padding">
              {this.state.errorMessageFieldTooLong}
            </p>
          ) : null}

          <form className="form_user" onSubmit={this.editConfig}>
            {this.state.configArray.map((config, i) => (
              <div key={config.id}>
                <p className="white">{config.name}</p>
                <label htmlFor="name" className="label_before_input">
                  Modifica l'unità di misura{" "}
                </label>
                <input
                  type="text"
                  name="format"
                  className="form-input_new_device remove_margin config-input"
                  value={this.state.value}
                  placeholder={config.format}
                  onChange={(e) => {
                    this.onChangeValueFormat(e, Number(config.id));
                  }}
                ></input>

                <label for="name" className="label_before_input">
                  Modifica la soglia di sicurezza{" "}
                </label>

                {config.thresholdGreater === true ? (
                  <select
                    value={this.state.value}
                    onChange={(e) => {
                      this.thresholdGreaterOrLesser(e, Number(config.id));
                    }}
                    className="form-input_new_device add_low_margin"
                  >
                    <option value="greater">Maggiore di</option>
                    <option value="lesser">Minore di </option>
                  </select>
                ) : (
                  <select
                    value={this.state.value}
                    onChange={(e) => {
                      this.thresholdGreaterOrLesser(e, Number(config.id));
                    }}
                    className="form-input_new_device add_low_margin"
                  >
                    <option value="lesser">Minore di </option>
                    <option value="greater">Maggiore di</option>
                  </select>
                )}
                <input
                  type="text"
                  name="threshold"
                  className="form-input_new_device remove_margin config-input"
                  value={this.state.value}
                  placeholder={config.threshold}
                  onChange={(e) => {
                    this.onChangeValueThreshold(e, Number(config.id));
                  }}
                ></input>
                <br></br>
                <div className="checkbox-config">
                  <label
                    htmlFor="isInfluencingCheckbox"
                    className="label_color"
                  >
                    È un dato influenzante?
                  </label>
                  <input
                    type="checkbox"
                    name="isInfluencing"
                    className="form-input_new_device remove_margin config-input"
                    value={this.state.value}
                    id="isInfluencingCheckbox"
                    defaultChecked={config.influential}
                    onClick={() => {
                      this.onChangeInfluence(config.id);
                    }}
                  ></input>
                  <button
                    className="button-user-list white"
                    type="button"
                    title="Elimina il device"
                    onClick={() => {
                      this.onRemoveConfig(config.id);
                    }}
                  >
                    <Icon icon={deleteFilled} className="icon-user-list" />
                  </button>
                </div>
              </div>
            ))}

            <button
              type="submit"
              value="Modifica il device"
              className="form-button configuration-button"
            >
              Modifica le configurazioni
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
    } else {
      return <h1>Stiamo caricando le configurazioni</h1>;
    }
  }
}
