import React, {Component, useState} from "react";
import Input from "react-validation/build/input";
import Form from "react-validation/build/form";
import CheckButton from "react-validation/build/button";
import { DataGrid } from '@mui/x-data-grid';
import MapSection from './GoogleMap.component'


import UserService from "../services/user.service";


let mapItemList = [];
let columns, rows;
const required = value => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                This field is required!
            </div>
        );
    }
};

const vlatitude = value => {
    if (value < -85 || value > 85) {
        return (
            <div className="alert alert-danger" role="alert">
                Please enter a value between -85 and +85
            </div>
        );
    }
};

const vlongitude = value => {
    if (value < -180 || value > 180) {
        return (
            <div className="alert alert-danger" role="alert">
                Please enter a value between -180 and +180
            </div>
        );
    }
};

const vradius = value => {
    if (value < 0 || value > 50000) {
        return (
            <div className="alert alert-danger" role="alert">
                Please enter a value between 0 and 50000
            </div>
        );
    }
};

export default class Map extends Component {

    constructor(props) {
        super(props);
        this.handleSearchLocation = this.handleSearchLocation.bind(this);
        this.onChangeLatitude = this.onChangeLatitude.bind(this);
        this.onChangeLongitude = this.onChangeLongitude.bind(this);
        this.onChangeRadius = this.onChangeRadius.bind(this);

        this.state = {
            latitude: "",
            longitude: "",
            radius: "",
            successful: false,
            message: "",
            mapData: [],
            mapDataLength: 0
        };
    }

    componentDidMount() {
        columns = [
            { field: 'name', headerName: 'Name', flex: 1 },
            { field: 'address', headerName: 'Address', flex: 1 },
            { field: 'types', headerName: 'Type', flex: 1},
        ];
        // this.setState({ currentUser: currentUser, userReady: true })
    }

    onChangeLatitude(e) {
        this.setState({
            latitude: e.target.value
        }, () => {});
    }

    onChangeLongitude(e) {
        this.setState({
            longitude: e.target.value
        });
    }

    onChangeRadius(e) {
        this.setState({
            radius: e.target.value
        });
    }

    handleSearchLocation(e) {
        e.preventDefault();

        this.setState({
            message: "",
            successful: false
        });

        this.form.validateAll();

        if (this.checkBtn.context._errors.length === 0) {
            UserService.getMap(
                this.state.latitude,
                this.state.longitude,
                this.state.radius
            ).then(
                response => {
                    this.setState({
                        message: "",
                        successful: true,
                        mapData: [...UserService.getInfoMap()]
                        // mapDataLength: UserService.getInfoMap().length
                    })
                },
                error => {
                    const resMessage =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();

                    this.setState({
                        successful: false,
                        message: resMessage
                    });
                }
            );
        }
    }


    render() {
        let data = this.state.mapData;
        for (let i = 0; i < data.length; i++) {
            data[i].id = i + 1;
        }
        data.forEach((item, index) => {
            mapItemList.push(<li key={index}>{item.name}</li>)
        })
        return (
            <div className="col-md-12">

                    <Form
                        onSubmit={this.handleSearchLocation}
                        ref={c => {
                            this.form = c;
                        }}
                    >

                            <div>
                                <div className="form-group">
                                    <label htmlFor="latitude">Latitude</label>
                                    <Input
                                        type="text"
                                        className="form-control"
                                        name="latitude"
                                        value={this.state.latitude}
                                        onChange={this.onChangeLatitude}
                                        validations={[required, vlatitude]}
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="longitude">Longitude</label>
                                    <Input
                                        type="longitude"
                                        className="form-control"
                                        name="longitude"
                                        value={this.state.longitude}
                                        onChange={this.onChangeLongitude}
                                        validations={[required, vlongitude]}
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="radius">Radius</label>
                                    <Input
                                        type="radius"
                                        className="form-control"
                                        name="radius"
                                        value={this.state.radius}
                                        onChange={this.onChangeRadius}
                                        validations={[required, vradius]}
                                    />
                                </div>

                                <div className="form-group">
                                    <button className="btn btn-primary btn-block">Find Places!</button>
                                </div>
                            </div>

                        {this.state.message && (
                            <div className="form-group">
                                <div
                                    className={
                                        this.state.successful
                                            ? "alert alert-success"
                                            : "alert alert-danger"
                                    }
                                    role="alert"
                                >
                                    {this.state.message}
                                </div>
                            </div>
                        )}
                        <CheckButton
                            style={{display: "none"}}
                            ref={c => {
                                this.checkBtn = c;
                            }}
                        />
                    </Form>
                {this.state.successful && (
                    <div>
                        <DataGrid
                            rows={data}
                            columns={columns}
                            getRowId={(row) => row.id}
                            pageSize={5}
                            rowsPerPageOptions={[5]}
                            checkboxSelection
                            autoHeight
                        />
                        <MapSection coordinates={data} /> {/* include it here */}
                    </div>

                )}

            </div>

)}}