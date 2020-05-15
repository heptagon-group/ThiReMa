import React from "react";
import "../../style.css";
import Subject from "./Subject";
import LineChartOperative from "./LineChartOperative";
//import LineChartInfluential from "./LineChartInfluential";
//import DoughnutChart from "./DoughnutChart";
import ScatterGraph from "./ScatterGraph";

export default class Observer extends React.Component {
  constructor(props) {
    //{this.props.deviceId} contiene l'id del device
    super(props);
    this.state = {
      operativeNames: [],
      operativeData: [],
      thresholdOp: [],
      loading: false,
      operativeGraphs: [],
      doughnutGraphs: [],
      arrayX: [],
      arrayY: [],
      scatterGraphs: [],
      graphsCleared: false,
      scatterNames: [],
      time: [],
      deviceId: null,
      errorMessageNoCorrelation: "",
      errorMessageApi: "",
    };
    this.subject = new Subject(this.props.deviceId, this);
  }

  /**
   * carica i dati contenuti dentro al Subject nello state dell'observer
   */

  componentDidMount() {
    this.setState({ loading: true });
    this.setState({
      deviceId: this.props.deviceId,
      operativeData: [...this.subject.state.operativeData],
      operativeNames: [...this.subject.state.operativeNames],
      thresholdOp: [...this.subject.state.thresholdOp],
      arrayX: [...this.subject.state.arrayX],
      arrayY: [...this.subject.state.arrayY],
      scatterNames: [...this.subject.state.scatterNames],
      time: [...this.subject.state.time],
      errorMessageNoCorrelation: this.subject.state.errorMessageNoCorrelation,
      errorMessageApi: this.subject.state.errorMessage,
      loading: false,
    });
  }

  /**
   * aggiorna i dati operativi ed i fattori influenzanti.
   *
   * @param {[]} newData dati nuovi ricevuti dalla chiamata notifyObservers() dell'oggetto di tipo Subject.
   *                     this.state.influentualData, this.state.operativeData verranno aggiornati.
   *
   * il setState dovrà essere sistemato per riflettere i dati effettivi che arriveranno dalle API, ma il succo è quello
   */

  update = (newData) => {
    this.setState({
      operativeData: [...newData.operativeData],
      operativeNames: [...newData.operativeNames],
      arrayX: [...newData.arrayX],
      arrayY: [...newData.arrayY],
      thresholdOp: [...newData.thresholdOp],
      scatterNames: [...newData.scatterNames],
      time: [...newData.time],
      errorMessageNoCorrelation: newData.errorMessageNoCorrelation,
      errorMessage: newData.errorMessage,
    });
  };

  /**
   * ripulisce l'array contenente i grafici
   */

  clearGraphsArray = () => {
    if (!this.state.graphsCleared || this.state.graphsCleared) {
      this.state.operativeGraphs.splice(0, this.state.operativeGraphs.length);
      this.state.scatterGraphs.splice(0, this.state.scatterGraphs.length);
      this.setState({
        operativeGraphs: [],
        scatterGraphs: [],
        graphsCleared: !this.state.graphsCleared,
      });
    }
  };
  /**
   * carica i grafici necessari nella vista
   */

  loadOperativeData = () => {
    this.state.operativeData.forEach((opData, i) => {
      var lineChartOperative = (
        <LineChartOperative
          className="chart"
          data={opData}
          dataName={this.state.operativeNames[i]}
          threshold={Number(this.state.thresholdOp.flat()[i])}
          time={this.state.time[i]}
        />
      );
      this.state.operativeGraphs.push(lineChartOperative);
      i = i + 1;
    });
  };
  loadScatteredData = () => {
    this.state.arrayX.forEach((x, i) => {
      var scatterChart = (
        <ScatterGraph
          className="chart"
          arrayX={x}
          arrayY={this.state.arrayY[i]}
          dataName={this.state.scatterNames[i]}
        />
      );
      this.state.scatterGraphs.push(scatterChart);
      i = i + 1;
    });
  };

  loadGraphs = () => {
    this.loadOperativeData();
    this.loadScatteredData();
  };

  render() {
    if (
      this.state.operativeGraphs.length !== 0 ||
      this.state.doughnutGraphs.length !== 0
    ) {
      this.clearGraphsArray();
    }

    if (!this.props.deviceId && this.state.operativeGraphs.length !== 0) {
      return <h1>Seleziona un device</h1>;
    }

    if (!this.state.loading) {
      this.loadGraphs();
      this.subject.updateTheId(this.props.deviceId);
      if (this.state.operativeData.length === 0) {
        return <p>Non ci sono ancora dati da mostrare</p>;
      } else {
        return (
          <div>
            <p>{this.state.errorMessageApi}</p>
            {this.state.errorMessageApi ? null : (
              <p>{this.state.errorMessageNoCorrelation}</p>
            )}

            <div className="graph-container">
              {this.state.operativeGraphs.map((graph, i) => (
                <div key={i}>{graph}</div>
              ))}
              {this.state.scatterGraphs.map((graph, i) => (
                <div key={i}>{graph}</div>
              ))}
              {this.state.doughnutGraphs.map((graph, i) => (
                <div key={i}>{graph}</div>
              ))}
            </div>
          </div>
        );
      }
    }
    if (this.state.loading) {
      return <p>Sto caricando i dati</p>;
    }
  }
}
