function togglePassword() {
    const passwordInput = document.getElementById('password');
    const toggleButton = document.querySelector('.material-symbols-outlined');

    if (passwordInput) {
        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';
            if (toggleButton) toggleButton.textContent = 'visibility_off'; // Меняем иконку на зачеркнутый глаз
        } else {
            passwordInput.type = 'password';
            if (toggleButton) toggleButton.textContent = 'visibility'; // Возвращаем обычный глаз
        }
    }
}