import React from "react";
import "../Project.css"
import { useParams } from "react-router-dom";

function Project() {
    const { projectName } = useParams();
    return <div>{projectName}</div>
}

export default Project;