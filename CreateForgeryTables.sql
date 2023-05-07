CREATE TABLE tribes_members (
    id SERIAL,
    member_email VARCHAR(255),
    months_subscribed INTEGER,
    PRIMARY KEY (id)
)

CREATE TABLE patreon_members (
    id SERIAL,
    member_email VARCHAR(255),
    months_subscribed INTEGER,
    PRIMARY KEY (id)
)

CREATE TABLE combined_members (
    id SERIAL,
    member_email VARCHAR(255),
    months_subscribed INTEGER,
    PRIMARY KEY (id)
)