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
        const table = document.querySelector('.expenses-table');
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

        if (!table) {
            return;
        }

        const tbody = table.querySelector('tbody');
        const columnFilters = table.querySelectorAll('.column-filter');
        const sortButtons = table.querySelectorAll('.column-sort');
        let currentSort = { column: null, direction: 'asc' };

        function getCellText(row, columnIndex) {
            const cell = row.querySelectorAll('td')[columnIndex];
            return cell ? cell.textContent.trim() : '';
        }

        function normaliseValue(value, type) {
            if (!value || value === '—') {
                return null;
            }

            if (type === 'number') {
                const numeric = parseFloat(value.replace(/[^0-9.-]+/g, ''));
                return Number.isNaN(numeric) ? null : numeric;
            }

            if (type === 'date') {
                const parts = value.split('|').map(function (part) {
                    return part.trim();
                });

                if (parts.length !== 2) {
                    return null;
                }

                const [datePart, timePart] = parts;
                const commaIndex = datePart.indexOf(',');
                const cleanedDate = commaIndex >= 0 ? datePart.slice(commaIndex + 1).trim() : datePart;
                const parsedDate = new Date(cleanedDate + ' ' + timePart);

                return Number.isNaN(parsedDate.getTime()) ? null : parsedDate.getTime();
            }

            return value.toLowerCase();
        }

        function updateSortIndicators() {
            sortButtons.forEach(function (button) {
                const indicator = button.querySelector('.sort-indicator');
                const buttonColumn = Number(button.dataset.column);

                if (!indicator) {
                    return;
                }

                if (currentSort.column === buttonColumn) {
                    const isAscending = currentSort.direction === 'asc';
                    button.setAttribute('aria-sort', isAscending ? 'ascending' : 'descending');
                    indicator.textContent = isAscending ? '▲' : '▼';
                } else {
                    button.setAttribute('aria-sort', 'none');
                    indicator.textContent = '⇅';
                }
            });
        }

        function sortRows(columnIndex, direction, type) {
            if (!tbody) {
                return;
            }

            const sortedRows = Array.from(tbody.querySelectorAll('tr[data-href]'));
            const multiplier = direction === 'asc' ? 1 : -1;

            sortedRows.sort(function (a, b) {
                const aValueRaw = getCellText(a, columnIndex);
                const bValueRaw = getCellText(b, columnIndex);
                const aValue = normaliseValue(aValueRaw, type);
                const bValue = normaliseValue(bValueRaw, type);

                if (aValue === null && bValue === null) {
                    return 0;
                }

                if (aValue === null) {
                    return 1 * multiplier;
                }

                if (bValue === null) {
                    return -1 * multiplier;
                }

                if (type === 'text') {
                    return aValue.localeCompare(bValue) * multiplier;
                }

                if (aValue < bValue) {
                    return -1 * multiplier;
                }

                if (aValue > bValue) {
                    return 1 * multiplier;
                }

                return 0;
            });

            sortedRows.forEach(function (row) {
                tbody.appendChild(row);
            });
        }

        function applyFilters() {
            const filters = Array.from(columnFilters).map(function (input) {
                return input.value.trim().toLowerCase();
            });

            Array.from(tbody.querySelectorAll('tr[data-href]')).forEach(function (row) {
                const cells = Array.from(row.querySelectorAll('td'));

                const matches = filters.every(function (filterValue, index) {
                    if (!filterValue) {
                        return true;
                    }

                    const cellText = cells[index] ? cells[index].textContent.trim().toLowerCase() : '';
                    return cellText.includes(filterValue);
                });

                row.style.display = matches ? '' : 'none';
            });
        }

        columnFilters.forEach(function (input) {
            input.addEventListener('input', applyFilters);
        });

        sortButtons.forEach(function (button) {
            button.addEventListener('click', function () {
                const columnIndex = Number(button.dataset.column);
                const type = button.dataset.type || 'text';

                if (currentSort.column === columnIndex) {
                    currentSort.direction = currentSort.direction === 'asc' ? 'desc' : 'asc';
                } else {
                    currentSort.column = columnIndex;
                    currentSort.direction = 'asc';
                }

                sortRows(columnIndex, currentSort.direction, type);
                updateSortIndicators();
                applyFilters();
            });
        });

        updateSortIndicators();
        applyFilters();
    });
})();
