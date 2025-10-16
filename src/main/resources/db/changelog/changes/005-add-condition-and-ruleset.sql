CREATE TABLE expense_condition
(
    id              SERIAL,
    expense_field   VARCHAR(255) NOT NULL,
    predicate       VARCHAR(255) NOT NULL,
    value           VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE expense_ruleset
(
    id                SERIAL,
    condition_id      INTEGER NOT NULL,
    populating_field  VARCHAR(255) NOT NULL,
    value             VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (condition_id) REFERENCES expense_condition ON DELETE cascade
);