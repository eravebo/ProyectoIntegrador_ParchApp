document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('registrationForm');
    const alertContainer = document.getElementById('alertContainer');

    function showAlert(message, type) {
        alertContainer.innerHTML = '<div class="alert ' + type + '">' + message + '</div>';
        setTimeout(function () {
            alertContainer.innerHTML = '';
        }, 5000);
    }

    function isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
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

    if (!form) {
        return;
    }

    document.getElementById('email').addEventListener('blur', function () {
        const email = this.value.trim();
        toggleError('emailError', email && !isValidEmail(email));
    });

    document.getElementById('password').addEventListener('blur', function () {
        const password = this.value;
        toggleError('passwordError', password && password.length < 6);
    });

    document.getElementById('confirmPassword').addEventListener('blur', function () {
        const password = document.getElementById('password').value;
        const confirmPassword = this.value;
        toggleError('confirmPasswordError', confirmPassword && password !== confirmPassword);
    });

    document.getElementById('terms').addEventListener('change', function () {
        toggleError('termsError', !this.checked);
    });

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const termsAccepted = document.getElementById('terms').checked;

        let isValid = true;

        if (!email || !isValidEmail(email)) {
            toggleError('emailError', true);
            isValid = false;
        } else {
            toggleError('emailError', false);
        }

        if (!password || password.length < 6) {
            toggleError('passwordError', true);
            isValid = false;
        } else {
            toggleError('passwordError', false);
        }

        if (!confirmPassword || password !== confirmPassword) {
            toggleError('confirmPasswordError', true);
            isValid = false;
        } else {
            toggleError('confirmPasswordError', false);
        }

        if (!termsAccepted) {
            toggleError('termsError', true);
            isValid = false;
        } else {
            toggleError('termsError', false);
        }

        if (!isValid) {
            showAlert('Por favor corrige los errores en el formulario', 'error');
            return;
        }

        // Guardar usuario en localStorage
        if (typeof registerUser === 'function') {
            const result = registerUser(email, password);
            if (!result.ok) {
                showAlert(result.message, 'error');
                return;
            }
        }

        showAlert('¡Registro exitoso! Redirigiendo...', 'success');

        setTimeout(function () {
            window.location.href = 'login.html';
        }, 2000);
    });
});

