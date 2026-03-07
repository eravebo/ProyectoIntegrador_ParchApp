const PARCHAPP_USERS_KEY = 'parchapp_users';
const PARCHAPP_CURRENT_USER_KEY = 'parchapp_currentUser';

function loadUsers() {
    try {
        const raw = localStorage.getItem(PARCHAPP_USERS_KEY);
        return raw ? JSON.parse(raw) : [];
    } catch (e) {
        return [];
    }
}

function saveUsers(users) {
    try {
        localStorage.setItem(PARCHAPP_USERS_KEY, JSON.stringify(users));
    } catch (e) {
        // Ignorar errores de almacenamiento
    }
}

function registerUser(email, password) {
    const users = loadUsers();
    const exists = users.some(function (u) {
        return u.email === email;
    });

    if (exists) {
        return { ok: false, message: 'Ya existe un usuario registrado con este correo.' };
    }

    users.push({ email: email, password: password });
    saveUsers(users);
    return { ok: true };
}

function findUser(emailOrUsername, password) {
    const users = loadUsers();
    return users.find(function (u) {
        return (u.email === emailOrUsername) && u.password === password;
    }) || null;
}

function setCurrentUser(userInfo) {
    try {
        localStorage.setItem(PARCHAPP_CURRENT_USER_KEY, JSON.stringify(userInfo));
    } catch (e) {
        // Ignorar
    }
}

function getCurrentUser() {
    try {
        const raw = localStorage.getItem(PARCHAPP_CURRENT_USER_KEY);
        return raw ? JSON.parse(raw) : null;
    } catch (e) {
        return null;
    }
}

