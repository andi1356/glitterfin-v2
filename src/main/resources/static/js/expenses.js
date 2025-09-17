(function () {
    function ready(fn) {
        if (document.readyState !== 'loading') {
            fn();
        } else {
            document.addEventListener('DOMContentLoaded', fn);
        }
    }

    ready(function () {
        document.querySelectorAll('.delete-expense').forEach(function (button) {
            button.addEventListener('click', function (event) {
                var row = button.closest('tr');
                var merchantCell = row ? row.querySelector('td:nth-child(2) div') : null;
                var merchant = merchantCell ? merchantCell.textContent.trim() : '';
                var confirmationMessage = 'Are you sure you want to delete this expense' + (merchant ? ' for ' + merchant : '') + '?';
                if (!window.confirm(confirmationMessage)) {
                    event.preventDefault();
                }
            });
        });
    });
})();
