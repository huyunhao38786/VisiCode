// components/NoteAdd/NoteAdd.js
import React, { useState } from "react";
import "./NoteAdd.css";
import axios from "axios";

function noteApi(str) {
    return `/api/note${str}`
}
const NoteAdd = (editorId) => {
    const [file, setFile] = useState();
    const [description, setDescription] = useState("");
    let formData = new FormData();

    const handleFileChange = (event) => {
        setFile(URL.createObjectURL(event.target.files[0]));
        formData.append("file", file);
    };

    const handleDescriptionChange = (event) => {
        setDescription(event.target.value);
    };


    const addTextNote = () => {
        if (description !== "") {
            console.log(description);
            console.log(editorId.editorId);
            axios
                .post(noteApi('/text'), {text: description}, { headers: { "Content-Type": "application/json; charset=UTF-8" }, params: {editorId: editorId.editorId}})
                .then(response => {
                    if (response.data.error != null) {
                        console.log(response.data.error);
                        alert("Fail to add note!")
                    } else {
                        console.log(response);
                    }
                }).catch((error) => {
                console.log("Problem submitting New Post", error);
            });
        }
    };

    const addImgNote = () => {
        if (file !== "") {
            axios
                .post(noteApi('/file'), formData, { headers: { "Content-Type": "multipart/form-data"}, params: {editorId: editorId.editorId}})
                .then(response => {
                    if (response.data.error != null) {
                        console.log(response.data.error);
                        alert("Fail to add note!")
                    } else {
                        console.log(response);
                    }
                }).catch((error) => {
                console.log("Problem submitting New Post", error);
            });
        }
    };

    return (
        <>
            <div className="noteadd">
                <h1>Add a New Note</h1>
                <div className="form-group">
                    <input
                        type="file"
                        className="noteadd-header"
                        name="noteadd-header"
                        placeholder="Click to add image"
                        onChange={(val) => handleFileChange(val)}
                    />
                    <img src={file} />
                    <div className="noteadd-button">
                        <button onClick={() => addImgNote()}>Add an Img Note</button>
                    </div>
                </div>
                <div className="form-group">
                  <textarea
                      name="noteadd-description"
                      className="noteadd-description"
                      placeholder="Note Description"
                      value={description}
                      onChange={(val) => handleDescriptionChange(val)}
                  ></textarea>
                </div>
                <div className="noteadd-button">
                    <button onClick={() => addTextNote()}>Add a Text Note</button>
                </div>
            </div>
        </>
    );
};

export default NoteAdd;