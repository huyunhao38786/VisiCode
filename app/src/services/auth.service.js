import axios from "axios";

const API_URL = "/api/user/";

class AuthService {
  login(username, password) {
    return axios
        .post(API_URL + "login", {
          username,
          password
        })
        .then(response => {
          if (response.data.error == null) {
            sessionStorage.setItem("user", JSON.stringify({
              username: username,
              ...response.data
            }));
          }
        });
  }

  logout() {
    sessionStorage.removeItem("user");
  }

  register(username, password) {
    return axios.post(API_URL + "create", {
      username,
      password
    });
  }

  getCurrentUser() {
    return JSON.parse(sessionStorage.getItem('user'));
  }
}

export default new AuthService();
