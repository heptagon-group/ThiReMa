import React from "react";
import "../../style.css";
import { Icon } from "@iconify/react";
import bxsPencil from "@iconify/icons-bx/bxs-pencil";
import deleteFilled from "@iconify/icons-ant-design/delete-filled";
import axios from "axios";
import Dialog from "./Dialog";

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
//import localStorageService from '../LocalStorageService'

//classe che mi genera la lista degli utenti
export default class List extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      id: "",
      username: "",
      telegramUsername: "",
      email: "",
      vatNumber: "",
      isEdited: false,
      persons_main: [], //Per fare la GET
      isOpen: false,
      our_id: "",
    };
  }

  componentDidMount() {
    const url = process.env.REACT_APP_BACKEND_URL + "/user";
    axios
      .get(url)
      .then((response) => {
        const persons = [...response.data];
        this.setState({ persons_main: [...persons] }); //TO DO: add persons: persons once it's setted
      })
      .catch(() => {
        this.setState({ errorMsg: "Error from the API" });
      });
  }

  sendData = (index) => {
    this.props.parentCallback(index);
  };

  updateArrayIfAnUserIsModified = () => {
    let newArray = [...this.state.persons];
    let newUser = this.props.newUserInfo;
    newArray[Number(this.props.indexOfModifiedElement)] = newUser;
    this.setState({
      persons: newArray,
      isEdited: true,
    });
  };

  render() {
    if (this.props.toEdit === true && this.state.isEdited === false) {
      this.updateArrayIfAnUserIsModified();
    }
    const { posts, errorMsg } = this.state; //Per aggiungere i dati provenienti dalla GET requests, o in caso di errore con l'api viene mostrato un messaggio

    return (
      <div className="right-list right-list-users">
        <h1 className="to_hide"> Lista degli Utenti</h1>
        <table className="user-list">
          <thead>
            <tr>
              <th>Username</th>
              <th>Username di Telegram</th>
              <th>Email</th>
              <th>Partita Iva (se presente)</th>
              <th>Modifica</th>
              <th>Elimina</th>
            </tr>
          </thead>
          <tbody className="tbody">
            {this.state.persons_main.map((persons_main, index) => (
              <tr key={persons_main.id}>
                <td className="array-element">{persons_main.username}</td>
                <td className="array-element">
                  {persons_main.telegramUsername}
                </td>
                <td className="array-element">{persons_main.email}</td>
                <td className="array-element">{persons_main.vatNumber}</td>
                <td className="icon">
                  <button
                    type="button"
                    onClick={() => {
                      this.sendData(index);
                    }}
                    className="button-user-list"
                  >
                    <Icon icon={bxsPencil} className="icon-user-list" />
                  </button>
                </td>
                <td className="icon">
                  <button
                    onClick={(e) =>
                      this.setState({ isOpen: true, our_id: persons_main.id })
                    }
                    className="button-user-list"
                    type="button"
                  >
                    <Icon icon={deleteFilled} className="icon-user-list" />
                  </button>
                  <Dialog
                    isOpen={this.state.isOpen}
                    our_id={this.state.our_id}
                    onClose={(e) => this.setState({ isOpen: false })}
                  />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  }
}
