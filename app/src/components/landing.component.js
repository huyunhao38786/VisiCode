import React, { Component } from "react";

import { withRouter } from '../common/with-router';
import { Routes, Route, Link } from "react-router-dom";

import axios from "axios";

import "../Project.css";
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
    this.externalProject = this.externalProject.bind(this);
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

  externalProject() {
    const link = prompt('Edit Link');
    if (link) {
      axios
        .get(projApi(`/visit/${link}`))
        .then(response => {
          if (response.data.error == null) {
            sessionStorage.setItem("external", link);
            this.props.router.navigate(`/visit/${response.data.name}`);
          }
        });
    }
  }

  render() {
    return <div>
      <button onClick={this.externalProject}>Edit or View With Link</button>
      <div id="landing">
        <button className="operate" onClick={this.addProject}>+</button>
        {(this.state?.projects || []).map(project => (
          <Link to={`${project}`} onClick={clearEditorLink}>
            <div className="frame">
              <h3>{project}</h3>
            </div>
          </Link>)
        )}
      </div>
    </div>
  }
}

export default Landing;