import axios from 'axios';

const API_URL = '/api/map';

class UserService {
    getMap(latitude, longitude, radius) {
        return axios.get(API_URL + "?latitude=" + latitude + "&longitude=" + longitude + "&radius=" + radius).then(response => {
            sessionStorage.setItem("mapEntry", JSON.stringify(response.data));
            return response.data;
        });
    }

    getInfoMap() {
        // console.log(JSON.parse(sessionStorage.getItem('mapEntry')));
        return JSON.parse(sessionStorage.getItem('mapEntry'));
    }

}

export default new UserService();
