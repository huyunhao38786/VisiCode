import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../Project.css";
import Id from "../id.component";
import axios from "axios";
import NoteAdd from "./NoteAdd/NoteAdd.component";
import DeleteForeverOutlinedIcon from "@mui/icons-material/DeleteForeverOutlined";

function Note(id, text, deleteNote, link, deletable) {
    const [content, setContent] = useState(null);
    // component did mount
    useEffect(()=>{
        axios
        .get(`/api/note/${id}?viewerOrEditorId=${link}`)
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

    return <div className="note">
        <div className="note header">
        { content && <em>{content.type || "NO TYPE"} - </em>}
        <Id label="id" value={id}/>
        </div>
        <div className="note-section">
            <NoteAdd />
        </div>
        <div className="note content">
        {text}
        </div>
        {/*<div className="previewPic" >*/}
        {/*    <img src={picture && picture}></img>*/}
        {/*</div>*/}
        <div className="note__footer" style={{ justifyContent: "flex-end" }}>
            { deletable && <DeleteForeverOutlinedIcon
                className="note__delete"
                onClick={() => deleteNote(id)}
                aria-hidden="true"
            ></DeleteForeverOutlinedIcon>}
        </div>
    </div>
}

export default Note;