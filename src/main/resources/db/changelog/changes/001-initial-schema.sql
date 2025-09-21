CREATE TABLE owner
(
    user_agent_id VARCHAR(255),
    api_token     VARCHAR(255) NOT NULL,
    details       VARCHAR(255) NOT NULL,
    username      VARCHAR(255) NOT NULL,
    password      VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_agent_id)
);

CREATE TABLE location
(
    id               SERIAL PRIMARY KEY,
    geocode_place_id INT UNIQUE,
    country_code     VARCHAR(255),
    postcode         VARCHAR(255),
    display_name     VARCHAR(255),
    latitude         FLOAT NOT NULL,
    longitude        FLOAT NOT NULL,
    country          VARCHAR(255),
    county           VARCHAR(255),
    city             VARCHAR(255),
    road             VARCHAR(255),
    house_number     VARCHAR(255)
);

CREATE TABLE expense (
    id        SERIAL,
    owner_id  VARCHAR(255),
    amount    NUMERIC(20,2) NOT NULL,
    timestamp TIMESTAMP     NOT NULL,
    timezone  varchar(6)    NOT NULL,
    source    VARCHAR(255)   NOT NULL,
    merchant  VARCHAR(255)  NOT NULL,

    location_id   INT NOT NULL,
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

--rollback DROP TABLE expense;
--rollback DROP TABLE owner;
--rollback DROP TABLE location;
