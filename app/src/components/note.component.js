import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "../Project.css";
import Id from "./id.component";
import axios from "axios";
import { Icon } from "@iconify/react";

function Note(props) {
    const [content, setContent] = useState(null);

    // component did mount
    useEffect(()=>{
        axios
        .get(`/api/note/${props.noteId}?viewerOrEditorId=${props.link}`)
        .then(response => {
            if (response.data.error == null) {
                setContent(response.data);
            }
        })
    }, []);

    let inner = <p><em>NO CONTENT</em></p>;
    if (content) {
        inner = <p>{content.data || "EMPTY NOTE"}</p>
    }

    return <div className="frame">
        <div className="note header">
        { content && <em>{content.type || "NO TYPE"} - </em>}
        <Id label="id" value={props.noteId}/>
        { props.deletable && <Icon icon="material-symbols:delete-outline" width="1.5em" className="hoverable"/>}
        </div>
        <div className="note content">
        {inner}
        </div>
    </div>
}

export default Note;