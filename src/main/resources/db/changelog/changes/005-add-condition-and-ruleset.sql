CREATE TABLE expense_condition
(
    id              SERIAL,
    expense_field   VARCHAR(255) NOT NULL,
    predicate       VARCHAR(255) NOT NULL,
    value           VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE expense_rule
(
    id                SERIAL,
    condition_id      INTEGER NOT NULL,
    populating_field  VARCHAR(255) NOT NULL,
    value             VARCHAR(255) NOT NULL,
    priority          INTEGER NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    FOREIGN KEY (condition_id) REFERENCES expense_condition
);

CREATE TABLE expense_ruleset_audit (
    id           SERIAL,
    condition_id INTEGER NOT NULL,
    rule_id      INTEGER NOT NULL,
    applied_at   TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (condition_id) REFERENCES expense_condition,
    FOREIGN KEY (rule_id) REFERENCES expense_rule
)