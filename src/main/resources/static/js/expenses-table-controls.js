(function () {
    document.addEventListener('DOMContentLoaded', function () {
        const table = document.querySelector('.expenses-table');
        if (!table) {
            return;
        }

        const tbody = table.querySelector('tbody');
        const storedRows = Array.from(tbody.querySelectorAll('tr')).map(function (row, index) {
            return { row: row, index: index };
        });
        const filterInputs = table.querySelectorAll('.filter-input');
        const sortButtons = table.querySelectorAll('.sort-toggle');
        let currentSort = { column: null, direction: null, type: 'text', key: null };

        function updateSortIndicators() {
            sortButtons.forEach(function (button) {
                const column = parseInt(button.dataset.column, 10);
                const baseLabel = button.dataset.label || 'column';
                let icon = '↕';
                let aria = 'Sort by ' + baseLabel;

                if (currentSort.column === column && currentSort.direction) {
                    if (currentSort.direction === 'asc') {
                        icon = '↑';
                        aria = 'Sort ' + baseLabel + ' ascending';
                    } else if (currentSort.direction === 'desc') {
                        icon = '↓';
                        aria = 'Sort ' + baseLabel + ' descending';
                    }
                }

                button.textContent = icon;
                button.setAttribute('aria-label', aria);
            });
        }

        function getComparableValue(row, column, type, key) {
            let rawValue = '';

            if (key && row.dataset[key] !== undefined) {
                rawValue = row.dataset[key];
            } else {
                const cell = row.cells[column];
                rawValue = cell ? cell.textContent.trim() : '';
            }

            if (type === 'number') {
                const number = parseFloat(rawValue);
                return isNaN(number) ? 0 : number;
            }

            if (type === 'date') {
                const time = Date.parse(rawValue);
                return isNaN(time) ? 0 : time;
            }

            return rawValue.toLowerCase();
        }

        function applyFiltersAndSorting() {
            const activeFilters = Array.from(filterInputs)
                .map(function (input) {
                    return {
                        column: parseInt(input.dataset.column, 10),
                        value: input.value.trim().toLowerCase()
                    };
                })
                .filter(function (filter) {
                    return filter.value;
                });

            let workingSet = storedRows.filter(function (entry) {
                return activeFilters.every(function (filter) {
                    const cell = entry.row.cells[filter.column];
                    if (!cell) {
                        return false;
                    }
                    return cell.textContent.toLowerCase().includes(filter.value);
                });
            });

            if (currentSort.column !== null && currentSort.direction) {
                const column = currentSort.column;
                const direction = currentSort.direction;
                const type = currentSort.type;
                const key = currentSort.key;

                workingSet.sort(function (a, b) {
                    const valueA = getComparableValue(a.row, column, type, key);
                    const valueB = getComparableValue(b.row, column, type, key);

                    if (valueA < valueB) {
                        return direction === 'asc' ? -1 : 1;
                    }
                    if (valueA > valueB) {
                        return direction === 'asc' ? 1 : -1;
                    }
                    return a.index - b.index;
                });
            } else {
                workingSet.sort(function (a, b) {
                    return a.index - b.index;
                });
            }

            tbody.innerHTML = '';
            workingSet.forEach(function (entry) {
                tbody.appendChild(entry.row);
            });
        }

        filterInputs.forEach(function (input) {
            input.addEventListener('input', applyFiltersAndSorting);
        });

        sortButtons.forEach(function (button) {
            button.addEventListener('click', function () {
                const column = parseInt(button.dataset.column, 10);
                const type = button.dataset.type || 'text';
                const key = button.dataset.sortKey || null;

                if (currentSort.column === column) {
                    if (currentSort.direction === 'asc') {
                        currentSort.direction = 'desc';
                    } else if (currentSort.direction === 'desc') {
                        currentSort = { column: null, direction: null, type: 'text', key: null };
                    } else {
                        currentSort.direction = 'asc';
                    }
                } else {
                    currentSort = { column: column, direction: 'asc', type: type, key: key };
                }

                updateSortIndicators();
                applyFiltersAndSorting();
            });
        });

        updateSortIndicators();
        applyFiltersAndSorting();
    });
})();
