const localStorageKeys = {
  CURRENT_USER: 'currentUser'
}

export function getCurrentUser() {
    return JSON.parse(localStorage.getItem(localStorageKeys.CURRENT_USER))
}

export function saveCurrentUser(user) {
    localStorage.setItem(localStorageKeys.CURRENT_USER, JSON.stringify(user))
}