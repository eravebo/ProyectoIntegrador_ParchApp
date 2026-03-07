document.addEventListener('DOMContentLoaded', function () {
    function logout() {
        try {
            localStorage.removeItem('parchapp_currentUser');
        } catch (e) {
            // Ignorar errores de localStorage en navegadores antiguos
        }
        window.location.href = 'login.html';
    }

    window.logout = logout;
});

