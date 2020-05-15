import React from "react";
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
 * Classe che ottiene i dati a db e notifica gli observer.
 */
export default class Subject {
  constructor(idOfDevice, observer) {
    this.intervalId = 0;
    this.idOfDevice = idOfDevice; // id del device che mi arriva quando costruisco il subject
    this.observer = [observer]; // lista di observer
    this.notify = this.notify.bind(this);
    this.getDataFromDatabase = this.getDataFromDatabase.bind(this);
    this.state = {
      hasTheIdChanged: false,
      //ASSOCIO DIRETTAMENTE QUA COSI' I VALORI SENZA USARE SETSTATE
      operativeNames: [],
      operativeData: [],
      thresholdOp: [],
      arrayX: [],
      arrayY: [],
      scatterNames: [],
      time: [],
      errorMessageNoCorrelation: null,
      errorMessage: null,
    };
    this.getDataFromDatabase();
    this.dataLoop();
  }

  /* 
  mi serve se cambio device del quale voglio visualizzare i dati
  devo svuotare tutto quanto contenuto nello state se no o si prende i dati dei device sbagliati oppure non aggiorna quando scelgo un device appena 
  inserito e mi fa vedere i dati del device precedente
  */
  updateTheId = (newId) => {
    if (newId !== this.idOfDevice) {
      this.stopDataLoop();
      this.state.operativeData = [];
      this.state.operativeNames = [];
      this.state.thresholdOp = [];
      this.state.arrayX = [];
      this.state.arrayY = [];
      this.state.scatterNames = [];
      this.state.time = [];
      this.idOfDevice = newId;
      this.getDataFromDatabase();
      this.dataLoop();
      this.state.errorMessageNoCorrelation = null;
      this.state.errorMessage = null;
    }
  };

  /**
   * funzione "standard" notify() che notifica gli observer degli aggiornamenti.
   */
  notify() {
    this.observer[0].update(this.state);
  }

  /**
   * chiamata api per ottenere i dati
   */
  getDataFromDatabase() {
    const URL_GET_DATA =
      process.env.REACT_APP_BACKEND_URL +
      "/user/device/" +
      this.idOfDevice +
      "/data";
    axios
      .get(URL_GET_DATA)
      .then((result) => {
        let datas = [];
        for (let i = 0; i < result.data.time.length; ++i) {
          datas[i] = [];
          for (let j = 0; j < result.data.time[i].length; ++j) {
            datas[i][j] = new Date(result.data.time[i][j]).toLocaleDateString(
              "it-IT"
            );
          }
        }
        if (
          result.data.xvalueCorrelation.length === 0 ||
          result.data.yvalueCorrelation.length === 0
        ) {
          this.state.errorMessageNoCorrelation =
            "Non c'è una correlazione tra i dati operativi e i fattori influenzanti";
        }
        this.state.operativeNames = [...result.data.measureName];
        this.state.operativeData = [...result.data.measureValue];
        this.state.time = [...datas];
        this.state.thresholdOp = [...result.data.threshold];
        this.state.arrayX = [...result.data.xvalueCorrelation];
        this.state.arrayY = [...result.data.yvalueCorrelation];
        this.state.scatterNames = [...result.data.measureNameCorrelation];
        this.notify();
      })
      .catch(() => {
        this.state.errorMessage =
          "C'è un problema con il reperimento dei dati: prova più tardi";
      });
    this.notify();
  }

  /**
   * funzione che mi invoca getDataFromDatabase ogni 3 minuti
   */

  dataLoop() {
    this.intervalId = setInterval(() => {
      this.getDataFromDatabase();
    }, 180000);
  }

  /**
   * mi serve per quando cambio il device => devo fermare il thread dove viene ritmicamente invocata getDataFromDatabase
   */

  stopDataLoop() {
    clearInterval(this.intervalId);
  }

  registerObserver(observer) {
    this.observer.push(observer);
  }

  unregisterObserver(observer) {
    this.observer = this.observer.filter((obs) => obs !== observer);
  }
}
