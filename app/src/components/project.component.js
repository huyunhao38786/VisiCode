import React, { useEffect, useState } from "react";
import "../Project.css"
import { useParams } from "react-router-dom";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

function projApi(str) {
    return `/api/project${str}`
}

function Project(props) {
    const { projectName } = useParams();
    const external = sessionStorage.getItem('external');
    const [project, setProject] = useState(null);

    // component did mount / unmount
    useEffect(() => {
        if (external !== '') {
            // using other person's project
            // the permission will be set in project.permission as either view or edit
            axios
                .get(projApi(`/visit/${external}`))
                .then(response => {
                    if (response.data.error == null) {
                        setProject(response.data)
                    } else {
                        props.router.navigate("/projects")
                    }
                });
        } else {
            // viewing my own project
            axios
                .get(projApi(`/${projectName}`))
                .then(response => {
                    if (response.data.error == null) {
                        setProject(response.data)
                    } else {
                        props.router.navigate("/projects")
                    }
                });
        }

        return function cleanup() {
            console.log("cleaning up");
            sessionStorage.setItem('external', '');
        }
    }, []);

    return <div >
        <h1>{projectName}</h1>
        { project?.viewerId && <p>View with id: {project.viewerId}</p> }
        { project?.editorId && <p>Edit with id: {project.viewerId}</p> }
        <div>{
            (project?.notes || []).map(note => (
                <div className="frame">id: {note}</div>
            ))
        }
        </div>
    </div>
}

export default Project;