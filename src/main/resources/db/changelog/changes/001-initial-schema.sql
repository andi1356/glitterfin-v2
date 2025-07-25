CREATE TABLE owner
(
    id              VARCHAR(255),
    user_agent_id   VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE location
(
    id               SERIAL PRIMARY KEY,
    geocode_place_id INT UNIQUE,
    country_code     VARCHAR(255),
    postcode         VARCHAR(255),
    display_name     VARCHAR(255),
    latitude         FLOAT NOT NULL,
    longitude        FLOAT
);

CREATE TABLE expense (
    id        SERIAL,
    owner_id  VARCHAR(255),
    amount    REAL         NOT NULL,
    timestamp TIMESTAMP    NOT NULL,
    source    VARCHAR(15)  NOT NULL,
    merchant  VARCHAR(255) NOT NULL,

    location_id   INT,
    category      VARCHAR(255),
    receipt_data  VARCHAR,
    description   VARCHAR(255),
    details       VARCHAR,

    shared  BOOLEAN NOT NULL DEFAULT false,
    outlier BOOLEAN NOT NULL DEFAULT false,

    PRIMARY KEY (id),
    FOREIGN KEY (owner_id) REFERENCES owner,
    FOREIGN KEY (location_id) REFERENCES location
);

CREATE TABLE test_entity (
                             id BIGINT PRIMARY KEY,
                             name VARCHAR(50) NOT NULL
);

INSERT INTO owner VALUES ('andi', 'user-agent-123');

--rollback DROP TABLE expense;
--rollback DROP TABLE owner;
--rollback DROP TABLE location;
--rollback DROP TABLE test_entity;
