import React from "react";
import "../../style.css";
import { Line } from "react-chartjs-2";
import Chart from "chart.js";
import * as ChartAnnotation from "chartjs-plugin-annotation";
Chart.plugins.register([ChartAnnotation]); // Global

export default class LineChartInfluential extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      influentialData: [],
      threshold: "",
      defaultProps: {
        displayTitle: true,
        displayLegent: true,
        legendPosition: "bottom",
      },
    };
  }

  loadTheData = () => {
    var data = {
      labels: ["uno", "due", "tre", "quattro", "cinque", "sei"],
      datasets: [
        {
          label: this.props.dataName,
          data: this.props.data,
          borderColor: ["rgba(0, 174, 255, 0.6)"],
          backgroundColor: ["rgba(0,0,0,0)"],
        },
      ],
    };
    return data;
  };

  render() {
    return (
      <div className="chart">
        <Line
          data={this.loadTheData}
          width={500}
          height={500}
          options={{
            responsive: true,
            title: {
              display: this.state.defaultProps.displayTitle,
              text: this.props.dataName,
              fontSize: 25,
            },
            legend: {
              display: this.state.defaultProps.displayLegent,
              position: this.state.defaultProps.legendPosition,
            },
            tooltips: {
              mode: "index",
              intersect: true,
            },
            annotation: {
              annotations: [
                {
                  type: "line",
                  mode: "horizontal",
                  scaleID: "y-axis-0",
                  value: this.props.threshold,
                  borderColor: "rgb(225, 0, 0, 0.6)",
                  borderWidth: 4,
                  label: {
                    enabled: true,
                    content: "Soglia di sicurezza",
                  },
                },
              ],
            },
          }}
        />{" "}
      </div>
    );
  }
}
