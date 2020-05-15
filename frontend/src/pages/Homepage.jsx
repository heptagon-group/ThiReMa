import React from "react";
import Particles from "../Particles";
import "../style.css";
import { Icon } from "@iconify/react";
import logoutIcon from "@iconify/icons-fe/logout";
import DevicesInfo from "./DevicesSidebar/DevicesInfo";
import NewDevice from "./DevicesSidebar/NewDevice";
import EditDeviceData from "./DevicesSidebar/EditDeviceData";
import EditConfiguration from "./DevicesSidebar/EditConfiguration";
import GatewaysList from "./DevicesSidebar/GatewaysList";
import NewGateway from "./DevicesSidebar/NewGateway";
import NewConfiguration from "./DevicesSidebar/NewConfiguration";
import Observer from "./Observer/Observer";

export class WhiteRectangle extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      id: "",
      indexToModifyRecieved: false,
      modifiedDeviceData: "",
      isThereAnUpdatedArray: false,
      devices: [],
      isThereADeviceToAdd: false,
      hasTheDeviceBeenAdded: false,
      numberOfNewConfigurations: 0,
      hasDeviceDataBeenAdded: false,
      hasTheFirstConfigurationBeenAdded: false,
      showDevicesFromMenu: false, // mi mostra i device quando clicco dal bottone sul menu
      showGatewaysFromMenu: false, //mi mostra i gateway quando clicco dal bottone sul menu
      addNewGateway: false, //quando clicco il + nella finestra dei gateway per aggiungerne uno
      idOfConfigToModify: "", // indice con dentro la configurazione da modificare
      indexConfigRecieved: false, // se devo mostrare la pagina di modifica configurazione o meno
      idOfNewAddedDevice: "",
      idToShowDeviceGraphs: "",
      deviceName: "",
    };
    this.onAddConfiguration = this.onAddConfiguration.bind(this);
    this.callbackFunctionDataDeviceHasBeenInserted = this.callbackFunctionDataDeviceHasBeenInserted.bind(
      this
    );
    this.callbackFunctionHasTheFirstConfigBeenAdded = this.callbackFunctionHasTheFirstConfigBeenAdded.bind(
      this
    );
    this.callbackFunctionShowDevicesFromMenu = this.callbackFunctionShowDevicesFromMenu.bind(
      this
    );
    this.callbackFunctionAddNewGateway = this.callbackFunctionAddNewGateway.bind(
      this
    );
  }

  callbackFunctionGetTheDeviceName = (name) => {
    this.setState({ deviceName: name });
  };

  callbackFunctionIdDeviceForGraphs = (id) => {
    this.setState({ idToShowDeviceGraphs: id });
  };

  callbackFunctionIdOfNewDevice = (id) => {
    this.setState({ idOfNewAddedDevice: id });
  };

  callbackFunctionAddNewGateway = () => {
    this.setState({
      addNewGateway: true,
      showGatewaysFromMenu: false,
      showDevicesFromMenu: false,
    });
  };

  callbackFunctionShowGatewaysFromMenu = () => {
    this.setState({
      showGatewaysFromMenu: true,
      showDevicesFromMenu: false,
      addNewGateway: false,
      indexToModifyRecieved: false,
    });
  };
  /**
   * Con questa funzione ogni volta che clicco "gestisci i tuoi dispositivi in alto mi riporta nella pagina con la lista dei device! così se uno ha sbagliato
   * a cliccare aggiungi o modifica può tornare indietro"
   */
  callbackFunctionShowDevicesFromMenu = () => {
    this.setState({
      showDevicesFromMenu: true,
      showGatewaysFromMenu: false,
      isThereADeviceToAdd: false,
      indexToModifyRecieved: false,
      addNewGateway: false,
      indexConfigRecieved: false,
      hasDeviceDataBeenAdded: false,
    });
  };

  callbackFunctionGetTheIdOfDeviceToEdit = (idRecieved) => {
    this.setState({ id: idRecieved, indexToModifyRecieved: true });
  };

  callbackFunctionIdOfConfigToEdit = (idRecieved) => {
    this.setState({
      idOfConfigToModify: idRecieved,
      indexConfigRecieved: true,
      showDevicesFromMenu: false,
    });
  };

  //viene chiamata DOPO che è stato aggiunto un device, necessaria per ricaricare la componente DevicesInfo
  /*
  callbackFunctionHasTheDeviceBeenAdded = () => {
    this.setState({
      hasTheDeviceBeenAdded: true
    });
  };
  */

  //viene chiamata PRIMA di aggiungere un device
  callbackFunctionAddADevice = () => {
    this.setState({ isThereADeviceToAdd: true });
  };

  onAddConfiguration = () => {
    this.setState({
      numberOfNewConfigurations: this.state.numberOfNewConfigurations + 1,
    });
  };

  /* funzione che setta il boolano che mi dice che ho inserito i dati del device, e ora bisogna caricare la pagina di caricamento delle configurazioni
   * mi setta quindi il booleano hasDeviceDataBeenAdded to true
   */
  callbackFunctionDataDeviceHasBeenInserted = () => {
    if (this.state.hasDeviceDataBeenAdded === false) {
      this.setState({
        hasDeviceDataBeenAdded: true,
        isThereADeviceToAdd: false,
      });
    }
  };
  /**
   * mi fa sapere quando l'utente ha finito di aggiungere la configurazione
   */
  callbackFunctionHasTheFirstConfigBeenAdded = () => {
    if (this.state.hasTheFirstConfigurationBeenAdded === false) {
      this.setState({
        hasTheFirstConfigurationBeenAdded: true,
        showDevicesFromMenu: true,
        // showDevices: true
      });
    }
  };
  setShowGatewaysFalse = () => {
    this.setState({ showGatewaysFromMenu: false });
  };
  chooseToLoad = () => {
    if (this.state.indexConfigRecieved === true) {
      return (
        <div>
          {
            //l'indice che passo è quello del device a cui poi modifico la configurazione
          }
          <EditConfiguration idOfDevice={this.state.idOfConfigToModify} />
        </div>
      );
    }
    //devo mostrare la pagina di gateway
    if (this.state.showGatewaysFromMenu === true) {
      return (
        <div>
          <GatewaysList
            parentCallbackAddNewGateway={this.callbackFunctionAddNewGateway}
          />
        </div>
      );
    }
    if (this.state.addNewGateway === true) {
      return (
        <div>
          <NewGateway />
        </div>
      );
    }
    if (
      this.state.hasDeviceDataBeenAdded === true &&
      this.state.isThereADeviceToAdd === false
    ) {
      return <NewConfiguration idOfDevice={this.state.idOfNewAddedDevice} />;
    }
    //devo ancora aggiungere il device, e ho cliccato il +
    if (this.state.isThereADeviceToAdd === true) {
      return (
        <div>
          <NewDevice
            parentCallbackNowAddConfig={
              this.callbackFunctionDataDeviceHasBeenInserted
            }
            parentCallbackNewId={this.callbackFunctionIdOfNewDevice}
          />
        </div>
      );
    }
    // ho confermato l'aggiunta del device, oppure semplicemente devo caricare la lista dei device

    if (
      (this.state.showDevicesFromMenu === true &&
        this.state.showGatewaysFromMenu === true) ||
      this.state.hasTheFirstConfigurationBeenAdded === true ||
      (this.state.indexToModifyRecieved === false &&
        this.state.isThereAnUpdatedArray === false)
    ) {
      if (this.state.isThereADeviceToAdd === true) {
        this.setState({ isThereADeviceToAdd: false }, () => {
          window.location.reload();
        });
      }
      if (this.state.hasTheFirstConfigurationBeenAdded === true) {
        this.setState({ hasTheFirstConfigurationBeenAdded: false }, () => {
          window.location.reload();
        });
      }
      return (
        <div>
          <DevicesInfo
            parentCallbackEditDevice={this.callbackFunctionEditDevice}
            deviceToEdit={this.state.indexToModifyRecieved}
            updatedArray={this.callbackFunctionRecieveUpdatedArray}
            isThereAnUpdatedArray={this.state.isThereAnUpdatedArray}
            parentCallbackIsThereADeviceToAdd={this.callbackFunctionAddADevice}
            id={this.callbackFunctionGetTheIdOfDeviceToEdit}
            idConfigToModify={this.callbackFunctionIdOfConfigToEdit}
            idForGraphs={this.callbackFunctionIdDeviceForGraphs}
            nameForGraphs={this.callbackFunctionGetTheDeviceName}
          />
        </div>
      );
    }

    // devo modificare un device: dati o configurazione

    if (this.state.indexToModifyRecieved === true) {
      return (
        <div>
          <EditDeviceData id={this.state.id} />
        </div>
      );
    }

    //return <NewDevice />;
  };

  render() {
    //questo mi serve per quando schiaccio i + quando aggiungo configurazioni nuove!

    return (
      <div className="white-bg-rectangle centered">
        <div className="left_side">
          <Header />
          {this.chooseToLoad()}
        </div>
        <Data
          parentCallbackShowDevices={this.callbackFunctionShowDevicesFromMenu}
          parentCallbackShowGateways={this.callbackFunctionShowGatewaysFromMenu}
          deviceId={this.state.idToShowDeviceGraphs}
          deviceName={this.state.deviceName}
        />
      </div>
    );
  }
}

//classe che mi genera la lista degli utenti
export class Data extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showDevices: false,
      showGateways: false,
    };
    this.callBackfunctionButtonShowDevicesClicked = this.callBackfunctionButtonShowDevicesClicked.bind(
      this
    );
    this.callBackfunctionButtonShowGatewaysClicked = this.callBackfunctionButtonShowGatewaysClicked.bind(
      this
    );
  }

  sendDataShowGatewayWasClicked = () => {
    this.props.parentCallbackShowGateways(
      this.state.showDevices,
      this.state.showGateways
    );
  };
  sendDataShowDevicesWasClicked = () => {
    this.props.parentCallbackShowDevices(
      this.state.showDevices,
      this.state.showGateways
    );
  };
  callBackfunctionButtonShowDevicesClicked = () => {
    this.setState({ showDevices: true, showGateways: false });
  };
  callBackfunctionButtonShowGatewaysClicked = () => {
    this.setState({ showGateways: true, showDevices: false });
  };
  render() {
    if (this.state.showDevices) {
      this.sendDataShowDevicesWasClicked();
      this.setState({ showDevices: false });
    }
    if (this.state.showGateways) {
      this.sendDataShowGatewayWasClicked();
      this.setState({ showGateways: false });
    }
    return (
      <div className="right-list">
        <Menu
          parentCallbackHaveIClickedShowDevices={
            this.callBackfunctionButtonShowDevicesClicked
          }
          parentCallbackHaveIClickedShowGateways={
            this.callBackfunctionButtonShowGatewaysClicked
          }
        />
        <div className="graphs-container">
          <div>
            {this.props.deviceName ? (
              <h1 className="graphs-title">
                Grafici del device {this.props.deviceName}
              </h1>
            ) : (
              <h1 className="graphs-title">
                Clicca un device per visualizzarne i grafici
              </h1>
            )}
            {this.props.deviceId ? (
              <Observer deviceId={this.props.deviceId} />
            ) : null}
          </div>
        </div>
      </div>
    );
  }
}
export class Menu extends React.Component {
  sendShowDeviceClicked = () => {
    this.props.parentCallbackHaveIClickedShowDevices(true);
  };
  sendShowGatewaysClicked = () => {
    this.props.parentCallbackHaveIClickedShowGateways(true);
  };
  render() {
    return (
      <div className="navbar-line menu-data">
        <button
          type="submit"
          value="show-devices"
          className="button-header margin_button"
          onClick={this.sendShowDeviceClicked}
        >
          Gestisci i tuoi dispositivi
        </button>
        <button
          type="submit"
          value="show-gateways"
          className="button-header margin_button"
          onClick={this.sendShowGatewaysClicked}
        >
          Gestisci i tuoi gateway
        </button>
        <h1 className="data-username margin_text">
          {" "}
          {sessionStorage.username}
        </h1>

        <button
          onClick={() => {
            sessionStorage.token = null;
            window.location.href = process.env.REACT_APP_FRONTEND_URL + "/#";
          }}
          className="transparent-button"
          title="Esci"
        >
          <Icon
            icon={logoutIcon}
            className=" white icon-user-list icon-user-data margin_icon"
          />
        </button>
      </div>
    );
  }
}

export class Header extends React.Component {
  render() {
    return (
      <header className="header">
        <img
          src={require("../img/logo_homepage.png")}
          alt="Logo di ThiReMa"
          className="list-logo"
        />
      </header>
    );
  }
}

export default class Homepage extends React.Component {
  render() {
    return (
      <div className="background">
        <WhiteRectangle />
        <Particles />
      </div>
    );
  }
}
