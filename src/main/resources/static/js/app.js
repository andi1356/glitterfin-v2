(function () {
    function ready(fn) {
        if (document.readyState !== 'loading') {
            fn();
        } else {
            document.addEventListener('DOMContentLoaded', fn);
        }
    }

    ready(function () {
        var toggle = document.querySelector('.navbar__toggle');
        var nav = document.getElementById(toggle ? toggle.getAttribute('data-target') : '');

        if (toggle && nav) {
            toggle.addEventListener('click', function () {
                nav.classList.toggle('is-open');
            });

            nav.querySelectorAll('a, form').forEach(function (interactive) {
                interactive.addEventListener('click', function () {
                    nav.classList.remove('is-open');
                });
            });
        }

        var alerts = document.querySelectorAll('.alert');
        if (alerts.length > 0) {
            setTimeout(function () {
                alerts.forEach(function (alert) {
                    alert.style.transition = 'opacity 0.4s ease';
                    alert.style.opacity = '0';
                    setTimeout(function () {
                        alert.remove();
                    }, 400);
                });
            }, 4000);
        }
    });
})();
