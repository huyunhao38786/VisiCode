import React, { Component } from "react";

import { withRouter } from '../common/with-router';
import { Routes, Route, Link } from "react-router-dom";

import axios from "axios";

import "../Project.css";
import Project from "./project.component";
import "bootstrap/dist/css/bootstrap.min.css";

function projApi(str) {
  return `/api/project${str}`
}

class Landing extends Component {
  constructor(props) {
    super(props);
    this.state = {
      projects: []
    }
    this.addProject = this.addProject.bind(this);
  }

  componentDidMount() {
    axios
      .get(projApi(''))
      .then(response => {
        if (response.data.error == null) {
          this.setState({ projects: response.data })
        }
        return response.data;
      });
  }

  addProject() {
    const name = prompt('Create Project');
    if (name) {
      axios
        .post(projApi('/create'), { name })
        .then(response => {
          if (response.data.error == null) {
            this.props.router.navigate("/");
          }
        });
    }
  }

  render() {
    return <div id="landing">
      <button className={"plus"} onClick={this.addProject}>+</button>
      {(this.state?.projects || []).map(project => (
        <Link to={`${project}`}>
          <div className="frame">
            <h3>{project}</h3>
          </div>
        </Link>)
      )}
    </div>
  }
}

export default Landing;