CREATE TABLE expense_condition
(
    id              SERIAL,
    expense_field   VARCHAR(255) NOT NULL,
    predicate       VARCHAR(255) NOT NULL,
    value           VARCHAR(255) NOT NULL,
    priority        INTEGER NOT NULL,
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

CREATE TABLE expense_outbox
(
    id          SERIAL,
    event       VARCHAR(255) NOT NULL,
    expense_id  INTEGER NOT NULL,
    status      VARCHAR(255) NOT NULL,
    created_at  timestamptz NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (expense_id) REFERENCES expense
);

CREATE TABLE expense_ruleset_audit
(
    id          SERIAL,
    expense     INTEGER NOT NULL,
    condition   INTEGER NOT NULL,
    ruleset     INTEGER NOT NULL,
    created_at  timestamptz NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (expense) REFERENCES expense,
    FOREIGN KEY (condition) REFERENCES expense_condition,
    FOREIGN KEY (ruleset) REFERENCES expense_ruleset
);
