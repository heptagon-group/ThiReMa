import React from "react";
import "../../style.css";
import { Line } from "react-chartjs-2";
import Chart from "chart.js";

import * as ChartAnnotation from "chartjs-plugin-annotation";
Chart.plugins.register([ChartAnnotation]); // Global

export default class LineChartOperative extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
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
      labels: this.props.time,
      datasets: [
        {
          label: this.props.dataName,
          data: this.props.data,
          borderColor: ["rgba(90, 204, 78, 0.6)"],
          backgroundColor: ["rgba(0,0,0,0)"],
        },
      ],
    };
    return data;
  };

  render() {
    if (this.props.data.length !== 0) {
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
              tooltips: {
                mode: "index",
                intersect: true,
              },
              legend: {
                display: this.state.defaultProps.displayLegent,
                position: this.state.defaultProps.legendPosition,
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
    } else {
      return null;
    }
  }
}
