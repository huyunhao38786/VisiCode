import React, { useEffect, useState } from "react";
import "../Project.css"
import { useParams } from "react-router-dom";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import Note from "./note.component";
import Id from "./id.component";

function projApi(str) {
    return `/api/project${str}`
}

function Project(props) {
    const { projectName } = useParams();
    const external = sessionStorage.getItem('external');
    const [project, setProject] = useState(null);

    // component did mount / unmount
    useEffect(() => {
        if (external && external !== '') {
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
            sessionStorage.removeItem('external');
        }
    }, []);

    return <div >
        <h1>{projectName}</h1>
        <div className="project info">
            { project?.viewerId && <Id label="View with id" value={project.viewerId}/> }
            { project?.editorId && <Id label="Edit with id" value={project.editorId}/> }
        </div>
        <div>{
            (project?.notes || []).map(note => <Note 
                link={ project?.editorId || project?.viewerId } 
                deletable={ project?.editorId } 
                noteId = {note}/>)
        }
        </div>
    </div>
}

export default Project;