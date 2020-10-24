CREATE TABLE Units (
    unit_id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL
);

CREATE TABLE Category (
    category_id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(200) NOT NULL
);

CREATE TABLE Category_Tree_Path (
    ancestor_id BIGINT NOT NULL,
    descendant_id BIGINT NOT NULL,
    depth INT NOT NULL,
    PRIMARY KEY (ancestor_id, descendant_id),
    FOREIGN KEY (ancestor_id) REFERENCES Category(category_id) ON DELETE CASCADE,
    FOREIGN KEY (descendant_id) REFERENCES Category(category_id) ON DELETE CASCADE
);