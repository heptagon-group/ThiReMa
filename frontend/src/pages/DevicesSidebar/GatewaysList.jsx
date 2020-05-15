import React from "react";
import "../../style.css";
import { Icon } from "@iconify/react";
import deleteFilled from "@iconify/icons-ant-design/delete-filled";
import baselineAdd from "@iconify/icons-ic/baseline-add";
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

export default class GatewaysList extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      gateways: [],
      isLoading: false,
      errorMessageCouldntAdd: "",
    };
  }
  componentDidMount() {
    //questo setState è da tenere
    this.setState({ isLoading: true });
    // ^ lo setto a false dopo che ho fatto la chiamata alle api
    const url = process.env.REACT_APP_BACKEND_URL + "/user/gateway";

    axios
      .get(url)
      .then((response) => {
        const gateway = response.data;
        this.setState({ gateways: gateway });

        //this.setState({isLoading: false });
      })
      .catch((error) => {
        this.setState({
          errorMessageCouldntAdd:
            "Non sono riuscito a caricare la lista dei gateway",
        });
      });
    this.setState({ isLoading: false });
  }
  ß;
  onRemoveGateway = (index) => {
    // OCCHIO CHE L'1 NELLA RICHIESTA API ANDRA' MODIFICATA CON L'ID DELL'UTENTE CON LA SESSIONE ATTIVA
    const gatewayId = this.state.gateways[Number(index)].gatewayId;
    //questa andrà modificata

    const url = process.env.REACT_APP_BACKEND_URL + "/user/gateway";

    axios.get(url).catch((error) =>
      this.setState({
        error,
        isLoading: false,
      })
    );

    const array = this.state.gateways;
    const main_id = array[index].id;
    const API_DELETE_GATEWAY =
      process.env.REACT_APP_BACKEND_URL + "/user/gateway/" + main_id;

    const newGatewaysArray = this.state.gateways;
    this.setState({
      gateways: newGatewaysArray.filter(
        (gateway) => gateway.gatewayId !== gatewayId
      ),
    });
    axios.delete(API_DELETE_GATEWAY).then((response) => {
      if (response.status === "error") {
        // se c'è stato un array, rimetto dentro a devices l'array senza l'eliminazione eseguita
        this.setState({
          devices: newGatewaysArray,
        });
      } else {
        window.location.reload(false);
      }
    });
  };

  addGateway() {
    //this.props.parentCallbackAddNewGateway(true);
    const API_ADD_GATEWAY =
      process.env.REACT_APP_BACKEND_URL + "/user/gateway/new";

    axios
      .get(API_ADD_GATEWAY)
      .then((response) => {
        if (response.status === 201) {
          this.setState((prevState) => ({
            gateways: [response.data, ...prevState.gateways], //Devo chiedere al BackEnd una cosa prima che sia concluso
          }));
        }
      })
      .catch((error) => {
        if (error.response) {
          this.setState({
            errorMessageCouldntAdd: "Aggiunta del gateway non riuscita",
          });
        }
      });
  }

  render() {
    //se ho caricato la lista
    if (!this.state.isLoading) {
      return (
        <div className="device-list">
          <div className="title-device-list">
            <h2 className="white left-side-title">I tuoi gateway</h2>

            <Icon
              icon={baselineAdd}
              className="white button-user-list add-device"
              onClick={() => {
                this.addGateway();
              }}
            />
          </div>

          <div className="device-list-container">
            {this.state.gateways.map((gateway, index) => (
              <div className="single-device" key={gateway.id}>
                <p className="device-name">{gateway.id}</p>
                <div className="device-list-buttons">
                  <button
                    className="button-user-list white"
                    type="button"
                    title="Elimina il gateway"
                    onClick={() => {
                      this.onRemoveGateway(index);
                    }}
                  >
                    <Icon icon={deleteFilled} className="icon-user-list" />
                  </button>
                </div>
              </div>
            ))}
            <p className="error-message-form">{this.state.errorMessageLogin}</p>
          </div>
        </div>
      );
    }
    // se non l'ho ancora caricata:
    else {
      return <p>Sto caricando la lista...</p>;
    }
  }
}
