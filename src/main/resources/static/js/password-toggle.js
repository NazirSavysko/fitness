function togglePassword() {
    const passwordInput = document.getElementById('password');
    const toggleButton = document.querySelector('.material-symbols-outlined');

    if (passwordInput) {
        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';
            if (toggleButton) toggleButton.textContent = 'visibility_off';
        } else {
            passwordInput.type = 'password';
            if (toggleButton) toggleButton.textContent = 'visibility';
        }
    }
}