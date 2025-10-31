CREATE TABLE expense_condition
(
    id              SERIAL,
    owner_username  VARCHAR(255) NOT NULL,
    expense_field   VARCHAR(255) NOT NULL,
    predicate       VARCHAR(255) NOT NULL,
    value           VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE expense_rule
(
    id                SERIAL,
    owner_username    VARCHAR(255) NOT NULL,
    condition_id      INTEGER NULL,
    populating_field  VARCHAR(255) NOT NULL,
    value             VARCHAR(255) NOT NULL,
    priority          INTEGER NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    FOREIGN KEY (condition_id) REFERENCES expense_condition,
    UNIQUE(owner_username, condition_id, populating_field, priority)
);

CREATE TABLE expense_ruleset_audit (
    id           SERIAL,
    condition_id INTEGER NOT NULL,
    rule_id      INTEGER NOT NULL,
    expense_id   INTEGER NOT NULL,
    applied_at   TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (condition_id) REFERENCES expense_condition,
    FOREIGN KEY (rule_id) REFERENCES expense_rule,
    FOREIGN KEY (expense_id) REFERENCES expense
);

--rollback DROP TABLE expense_condition;
--rollback DROP TABLE expense_rule;
--rollback DROP TABLE expense_ruleset_audit;
