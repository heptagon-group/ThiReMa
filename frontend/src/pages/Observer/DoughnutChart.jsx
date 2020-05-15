import React from "react";
import "../../style.css";
import { Doughnut } from "react-chartjs-2";

export default class DoughnutChart extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      influentialData: [],
      operativeData: [],
      defaultProps: {
        displayTitle: true,
        displayLegent: true,
        legendPosition: "bottom",
      },
    };
  }

  calculateTotals = () => {
    var totOp = 0;
    for (let i = 0; i < this.props.operativeData.length; ++i) {
      for (let j = 0; j < this.props.operativeData[i].length; ++j) {
        totOp = totOp + this.props.operativeData[i][j];
      }
    }
    var totInf = 0;
    for (let i = 0; i < this.props.influentialData.length; ++i) {
      for (let j = 0; j < this.props.influentialData[i].length; ++j) {
        totInf = totInf + this.props.influentialData[i][j];
      }
    }
    return { totOp, totInf };
  };

  loadTheData = () => {
    const { totOp, totInf } = this.calculateTotals();
    var data = {
      labels: ["Fattori influenzanti", "Dati operativi"],
      datasets: [
        {
          label: "Dati",
          backgroundColor: [
            "rgba(255, 99, 132, 0.6)",
            "rgba(168, 230, 255, 0.6)",
          ],
          data: [totInf, totOp],
        },
      ],
    };
    return data;
  };

  render() {
    if (!this.state.loading) {
      return (
        <div className="chart">
          <Doughnut
            data={this.loadTheData}
            width={500}
            height={500}
            options={{
              title: {
                display: this.state.defaultProps.displayTitle,
                text: "Fattori operativi vs fattori influenzanti",
                fontSize: 25,
              },
              legend: {
                display: this.state.defaultProps.displayLegent,
                position: this.state.defaultProps.legendPosition,
              },
            }}
          />{" "}
        </div>
      );
    } else {
      return <h1>loading</h1>;
    }
  }
}
