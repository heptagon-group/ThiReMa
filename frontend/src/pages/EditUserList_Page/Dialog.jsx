import React, { Component } from "react";
import "../../style.css";
import axios from "axios";

export default class Dialog extends Component {
  constructor(props) {
    super(props);
    this.state = {
      persons_main: [],
    };
  }

  state = {
    isOpen: false,
    our_id: "",
  };

  onRemoveUser = (userId) => {
    //const idUser = this.props.idUser;

    const url = process.env.REACT_APP_BACKEND_URL + "/user/" + userId;

    axios.delete(url).then((response) => {
      if (response.status === "error") {
        // se c'è stato un array, rimetto dentro a devices l'array senza l'eliminazione eseguita
        //this.setState({
        //configArray: newConfigArray
        //});
      } else {
        window.location.reload(false);
      }
    });
  };

  render() {
    let dialog = (
      <div className="dialog_style">
        <h1 className="dialog_text1"> Sei sicuro? </h1>
        <h2 className="dialog_text2"> Vuoi eliminare questo utente? </h2>
        <div>
          <button
            className="dialog_button btn1"
            onClick={() => {
              this.onRemoveUser(this.props.our_id);
            }}
          >
            SI
          </button>
          <button onClick={this.props.onClose} className="dialog_button btn2">
            NO
          </button>
        </div>
      </div>
    );
    if (!this.props.isOpen) {
      dialog = null;
    }
    return <div>{dialog}</div>;
  }
}
