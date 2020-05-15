import React from "react";
import "../../style.css";
import { Scatter } from "react-chartjs-2";
import Chart from "chart.js";

import * as ChartAnnotation from "chartjs-plugin-annotation";
Chart.plugins.register([ChartAnnotation]); // Global

export default class ScatterGraph extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      errorNoData: null,
      loading: false,
      arrayX: [],
      arrayY: [],
      threshold: "",
      defaultProps: {
        displayTitle: true,
        displayLegent: true,
        legendPosition: "bottom",
      },
    };
  }

  componentDidMount() {
    if (!this.props.arrayX || !this.props.arrayY) {
      this.setState({
        errorNoData:
          "Non ci sono ancora dati a sufficienza per calcolare una correlazione",
      });
    }
  }

  loadTheData = () => {
    var data = {
      labels: ["uno", "due", "tre", "quattro", "cinque", "sei"],
      datasets: [
        {
          label: this.props.dataName,
          data: this.props.arrayX.map((x, i) => {
            return {
              x: x,
              y: this.props.arrayY[i],
            };
          }),
          borderColor: "#2196f3",
          fillColor: ["rgba(90, 204, 78, 0.6)"],
          backgroundColor: "#2196f3",
        },
      ],
    };
    return data;
  };

  render() {
    if (this.props.dataName) {
      return (
        <div className="chart">
          <Scatter
            data={this.loadTheData}
            width={500}
            height={500}
            options={{
              responsive: true,
              maintainAspectRatio: false,
              title: {
                display: false,
                text: this.props.dataName,
                fontSize: 25,
              },
              tooltips: {
                mode: "index",
                intersect: true,
              },
              legend: {
                display: false,
                //display: this.state.defaultProps.displayLegent,
                position: this.state.defaultProps.legendPosition,
              },
              scales: {
                yAxes: [
                  {
                    scaleLabel: {
                      display: true,
                      labelString: this.props.dataName[1],
                    },
                  },
                ],
                xAxes: [
                  {
                    scaleLabel: {
                      display: true,
                      labelString: this.props.dataName[0],
                    },
                  },
                ],
              },
            }}
          />{" "}
        </div>
      );
    } else {
      return null;
    }
  }
}
