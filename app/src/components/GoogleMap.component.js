import React, {useEffect, useState} from 'react'
import GoogleMapReact from 'google-map-react'
import '../map.css'
import { Icon } from '@iconify/react'
import locationIcon from '@iconify/icons-mdi/map-marker'

function Map({coordinates}) {

    if (coordinates === null || coordinates.length === 0) {
        return <div className="map"></div>
    }

    const sum = coordinates.reduce((a, b) => {
        return {
            text: "",
            lat: a.lat + b.lat,
            lng: a.lng + b.lng
        }
    });

    const avg = {
        lat: sum.lat / coordinates.length,
        lng: sum.lng / coordinates.length
    }

    return (<div className="map">
        <div className="google-map" style={{ height: '100vh', width: '100%' }}>
            <GoogleMapReact
                bootstrapURLKeys={{ key: 'AIzaSyArSWYTFqhVyO9TJ7Y6QsPGbGNANCfrKwo' }}
                zoom={17}
                center={avg}
            >
                {coordinates?.map(coord => {
                    return <LocationPin
                    lat={coord.lat}
                    lng={coord.lng}
                    text={coord.name}/>
                }) }
                
            </GoogleMapReact>
        </div>
    </div>)
}



const LocationPin = ({ text }) => (
    <div className="pin">
        <Icon icon={locationIcon} width={20} color="red" className="pin-icon" />
        <p className="pin-text">{text}</p>
    </div>
)

export default Map
