export default function authHeader() {
  const user = JSON.parse(sessionStorage.getItem('user'));

  if (user) {
    return { Authorization: 'Bearer ' + user };
  } else {
    return {};
  }
}
