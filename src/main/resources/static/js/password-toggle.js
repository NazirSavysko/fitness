function togglePassword(inputId, btn) {
    const passwordInput = document.getElementById(inputId);

    if (passwordInput) {
        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';
            btn.textContent = 'visibility_off';
        } else {
            passwordInput.type = 'password';
            btn.textContent = 'visibility';
        }
    }
}