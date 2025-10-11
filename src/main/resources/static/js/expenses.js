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

        const table = document.querySelector('.expenses-table');
        if (!table) {
            return;
        }

        const tableBody = table.querySelector('tbody');
        if (!tableBody) {
            return;
        }

        const tableWrapper = table.closest('.expenses-table-wrapper');
        const filteredEmptyState = tableWrapper ? tableWrapper.querySelector('.filter-empty-state') : null;

        let bodyRows = Array.from(tableBody.querySelectorAll('tr'));
        const filterInputs = Array.from(table.querySelectorAll('.column-filter'));
        const headers = Array.from(table.querySelectorAll('th[data-sort-key]'));

        function getCell(row, key) {
            if (!key) {
                return null;
            }

            return row.querySelector('td[data-column="' + key + '"]');
        }

        function applyFilters() {
            if (!bodyRows.length) {
                if (filteredEmptyState) {
                    filteredEmptyState.hidden = true;
                }
                return;
            }

            if (!filterInputs.length) {
                bodyRows.forEach(function (row) {
                    row.classList.remove('is-hidden');
                });

                if (filteredEmptyState) {
                    filteredEmptyState.hidden = true;
                }

                return;
            }

            const activeFilters = filterInputs
                .map(function (input) {
                    return {
                        key: input.dataset.filter,
                        value: input.value.trim().toLowerCase()
                    };
                })
                .filter(function (item) {
                    return item.key && item.value;
                });

            const hasActiveFilters = activeFilters.length > 0;
            let visibleCount = 0;

            bodyRows.forEach(function (row) {
                if (!hasActiveFilters) {
                    row.classList.remove('is-hidden');
                    visibleCount += 1;
                    return;
                }

                const matches = activeFilters.every(function (filter) {
                    const cell = getCell(row, filter.key);
                    if (!cell) {
                        return true;
                    }

                    const cellText = cell.textContent ? cell.textContent.toLowerCase() : '';
                    return cellText.indexOf(filter.value) !== -1;
                });

                row.classList.toggle('is-hidden', !matches);
                if (matches) {
                    visibleCount += 1;
                }
            });

            if (filteredEmptyState) {
                filteredEmptyState.hidden = !hasActiveFilters || visibleCount > 0;
            }
        }

        filterInputs.forEach(function (input) {
            input.addEventListener('input', applyFilters);
        });

        let currentSortKey = null;
        let currentSortDirection = 'asc';

        function getSortValue(row, key) {
            const cell = getCell(row, key);
            if (!cell) {
                return '';
            }

            const sortValue = cell.getAttribute('data-sort');
            if (sortValue !== null && sortValue !== '') {
                return sortValue;
            }

            return cell.textContent ? cell.textContent.trim() : '';
        }

        function compareValues(aValue, bValue, type) {
            if (type === 'number') {
                const numA = parseFloat(aValue);
                const numB = parseFloat(bValue);

                const numAInvalid = Number.isNaN(numA);
                const numBInvalid = Number.isNaN(numB);

                if (numAInvalid && numBInvalid) {
                    return 0;
                }

                if (numAInvalid) {
                    return 1;
                }

                if (numBInvalid) {
                    return -1;
                }

                return numA - numB;
            }

            if (type === 'date') {
                const timeA = Date.parse(aValue);
                const timeB = Date.parse(bValue);

                const timeAInvalid = Number.isNaN(timeA);
                const timeBInvalid = Number.isNaN(timeB);

                if (timeAInvalid && timeBInvalid) {
                    return 0;
                }

                if (timeAInvalid) {
                    return 1;
                }

                if (timeBInvalid) {
                    return -1;
                }

                return timeA - timeB;
            }

            const textA = (aValue || '').toString().toLowerCase();
            const textB = (bValue || '').toString().toLowerCase();
            return textA.localeCompare(textB);
        }

        function updateSortIndicators(activeHeader, direction) {
            headers.forEach(function (header) {
                if (header.dataset.sortDirection) {
                    delete header.dataset.sortDirection;
                }
                header.setAttribute('aria-sort', 'none');
            });

            if (activeHeader) {
                activeHeader.dataset.sortDirection = direction;
                activeHeader.setAttribute('aria-sort', direction === 'asc' ? 'ascending' : 'descending');
            }
        }

        function sortRows(key, type, header) {
            if (!key || !header) {
                return;
            }

            const nextDirection = currentSortKey === key && currentSortDirection === 'asc' ? 'desc' : 'asc';

            const sortedRows = bodyRows.slice().sort(function (a, b) {
                const aValue = getSortValue(a, key);
                const bValue = getSortValue(b, key);
                const comparison = compareValues(aValue, bValue, type);

                return nextDirection === 'asc' ? comparison : -comparison;
            });

            sortedRows.forEach(function (row) {
                tableBody.appendChild(row);
            });

            bodyRows = sortedRows;
            currentSortKey = key;
            currentSortDirection = nextDirection;

            updateSortIndicators(header, nextDirection);
            applyFilters();
        }

        headers.forEach(function (header) {
            header.addEventListener('click', function () {
                const key = header.dataset.sortKey;
                const type = header.dataset.sortType || 'text';
                sortRows(key, type, header);
            });

            header.addEventListener('keydown', function (event) {
                if (event.key === 'Enter' || event.key === ' ') {
                    event.preventDefault();
                    const key = header.dataset.sortKey;
                    const type = header.dataset.sortType || 'text';
                    sortRows(key, type, header);
                }
            });
        });

        updateSortIndicators(null, 'asc');
        applyFilters();
    });
})();
