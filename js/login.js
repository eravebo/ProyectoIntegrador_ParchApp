document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('loginForm');
    const alertContainer = document.getElementById('alertContainer');
    const loginBtn = document.getElementById('loginBtn');
    const userTypeBtns = document.querySelectorAll('.user-type-btn');
    const usernameInput = document.getElementById('username');
    const usernameLabel = document.getElementById('usernameLabel');
    const socialLogin = document.getElementById('socialLogin');
    const registerLink = document.getElementById('registerLink');

    if (!form) {
        return;
    }

    const demoCredentials = {
        user: {
            username: 'usuario',
            password: 'demo123'
        },
        admin: {
            username: 'admin',
            password: 'admin123'
        }
    };

    let currentUserType = 'user';

    function setUserType(type) {
        currentUserType = type;

        userTypeBtns.forEach(function (btn) {
            if (btn.dataset.type === type) {
                btn.classList.add('active');
            } else {
                btn.classList.remove('active');
            }
        });

        if (type === 'admin') {
            usernameLabel.textContent = 'Usuario Administrador';
            usernameInput.placeholder = 'Ingresa tu usuario de administrador';
            socialLogin.style.display = 'none';
            registerLink.style.display = 'none';
        } else {
            usernameLabel.textContent = 'Usuario';
            usernameInput.placeholder = 'Ingresa tu usuario (correo)';
            socialLogin.style.display = 'block';
            registerLink.style.display = 'block';
        }

        usernameInput.value = '';
        document.getElementById('password').value = '';
        clearErrors();
        alertContainer.innerHTML = '';
    }

    function showAlert(message, type) {
        alertContainer.innerHTML = '<div class="alert ' + type + '">' + message + '</div>';

        if (type === 'error') {
            setTimeout(function () {
                alertContainer.innerHTML = '';
            }, 5000);
        }
    }

    function clearErrors() {
        document.querySelectorAll('.error-message').forEach(function (error) {
            error.classList.remove('show');
        });
        document.querySelectorAll('.form-input').forEach(function (input) {
            input.classList.remove('error');
        });
    }

    function toggleError(elementId, show) {
        const errorElement = document.getElementById(elementId);
        const inputElement = document.getElementById(elementId.replace('Error', ''));

        if (!errorElement || !inputElement) return;

        if (show) {
            errorElement.classList.add('show');
            inputElement.classList.add('error');
        } else {
            errorElement.classList.remove('show');
            inputElement.classList.remove('error');
        }
    }

    userTypeBtns.forEach(function (btn) {
        btn.addEventListener('click', function () {
            setUserType(this.dataset.type);
        });
    });

    usernameInput.addEventListener('blur', function () {
        const username = this.value.trim();
        toggleError('usernameError', !username);
    });

    document.getElementById('password').addEventListener('blur', function () {
        const password = this.value;
        toggleError('passwordError', !password);
    });

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const username = usernameInput.value.trim();
        const password = document.getElementById('password').value;

        let isValid = true;

        if (!username) {
            toggleError('usernameError', true);
            isValid = false;
        } else {
            toggleError('usernameError', false);
        }

        if (!password) {
            toggleError('passwordError', true);
            isValid = false;
        } else {
            toggleError('passwordError', false);
        }

        if (!isValid) {
            showAlert('Por favor corrige los errores en el formulario', 'error');
            return;
        }

        loginBtn.textContent = 'Iniciando sesión...';
        loginBtn.disabled = true;

        setTimeout(function () {
            let loggedIn = false;

            if (currentUserType === 'admin') {
                const adminCreds = demoCredentials.admin;
                if (username === adminCreds.username && password === adminCreds.password) {
                    loggedIn = true;
                    setCurrentUser({ role: 'admin', username: username });
                    showAlert('¡Inicio de sesión exitoso! Redirigiendo al panel de administración...', 'success');
                    setTimeout(function () {
                        window.location.href = 'admin.html';
                    }, 2000);
                }
            } else {
                const demoUser = demoCredentials.user;
                const storedUser = findUser(username, password);

                if (storedUser || (username === demoUser.username && password === demoUser.password)) {
                    loggedIn = true;
                    setCurrentUser({ role: 'user', username: username });
                    showAlert('¡Inicio de sesión exitoso! Redirigiendo al inicio...', 'success');
                    setTimeout(function () {
                        window.location.href = 'index.html';
                    }, 2000);
                }
            }

            if (!loggedIn) {
                showAlert('Credenciales incorrectas. Por favor verifica tu usuario y contraseña.', 'error');
                loginBtn.textContent = 'Iniciar Sesión';
                loginBtn.disabled = false;
            }
        }, 1500);
    });

    document.querySelectorAll('.social-btn').forEach(function (button) {
        button.addEventListener('click', function () {
            const provider = this.classList.contains('google') ? 'Google' : 'Facebook';
            showAlert('Iniciando sesión con ' + provider + '...', 'success');

            setTimeout(function () {
                showAlert('La integración con ' + provider + ' estaría aquí en una implementación real', 'success');
            }, 1000);
        });
    });

    setUserType('user');
});

