(function () {
    function navigateToRow(row) {
        const url = row.getAttribute('data-href');
        if (url) {
            window.location.href = url;
        }
    }

    function isInteractiveElement(target) {
        return target.closest('a, button, form, input, select, textarea');
    }

    document.addEventListener('DOMContentLoaded', function () {
        const rows = document.querySelectorAll('tr[data-href]');

        rows.forEach(function (row) {
            row.classList.add('clickable');
            row.setAttribute('tabindex', '0');
            row.setAttribute('role', 'link');

            const expenseName = row.dataset.expenseName;
            if (expenseName) {
                row.setAttribute('aria-label', 'View details for ' + expenseName);
            }

            row.addEventListener('click', function (event) {
                if (isInteractiveElement(event.target)) {
                    return;
                }

                navigateToRow(row);
            });

            row.addEventListener('keydown', function (event) {
                if (isInteractiveElement(event.target)) {
                    return;
                }

                if (event.key === 'Enter') {
                    event.preventDefault();
                    navigateToRow(row);
                }

                if (event.key === ' ') {
                    event.preventDefault();
                    navigateToRow(row);
                }
            });
        });
    });
})();
