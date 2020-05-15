import React from "react";
import Particles from "../../Particles";
import "../../style.css";
import SignUpFormBusiness from "../SignUp_Page/SignUpBusiness";
import SignUpFormCitizen from "../SignUp_Page/SignUpCitizen";

export default class SignUp extends React.Component {
  render() {
    return (
      <div className="background">
          <div className="content">
            <div className="left-form">
              <SignUpFormCitizen />
            </div>
            <div className="right-form">
              <SignUpFormBusiness />
            </div>
          </div>
        <Particles/>
      </div>
    );
  }
}
