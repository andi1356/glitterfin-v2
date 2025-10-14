document.addEventListener('DOMContentLoaded', () => {
    const toggle = document.querySelector('.menu-toggle');
    const mobileMenu = document.getElementById('mobile-menu');
    const backdrop = document.querySelector('.menu-backdrop');
    const body = document.body;

    if (!toggle || !mobileMenu || !backdrop) {
        return;
    }

    const closeMenu = () => {
        toggle.setAttribute('aria-expanded', 'false');
        toggle.classList.remove('is-active');
        mobileMenu.classList.remove('is-open');
        mobileMenu.setAttribute('aria-hidden', 'true');
        backdrop.classList.remove('is-visible');
        body.classList.remove('menu-open');
    };

    const openMenu = () => {
        toggle.setAttribute('aria-expanded', 'true');
        toggle.classList.add('is-active');
        mobileMenu.classList.add('is-open');
        mobileMenu.setAttribute('aria-hidden', 'false');
        backdrop.classList.add('is-visible');
        body.classList.add('menu-open');
    };

    toggle.addEventListener('click', () => {
        const isExpanded = toggle.getAttribute('aria-expanded') === 'true';
        if (isExpanded) {
            closeMenu();
        } else {
            openMenu();
        }
    });

    backdrop.addEventListener('click', closeMenu);

    window.addEventListener('keydown', (event) => {
        if (event.key === 'Escape') {
            closeMenu();
        }
    });

    mobileMenu.addEventListener('click', (event) => {
        if (event.target.closest('a') || event.target.closest('button')) {
            closeMenu();
        }
    });
});
