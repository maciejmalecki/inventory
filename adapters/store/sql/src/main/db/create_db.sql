CREATE TABLE Units
(
    unit_id BIGSERIAL PRIMARY KEY,
    code    VARCHAR(50) UNIQUE NOT NULL,
    name    VARCHAR(200)       NOT NULL
);

CREATE TABLE ItemClasses
(
    item_class_id BIGSERIAL PRIMARY KEY,
    name          VARCHAR(50)  NOT NULL,
    description   VARCHAR(200) NOT NULL
);

CREATE TABLE Attributes
(
    attribute_id BIGSERIAL PRIMARY KEY,
    name         VARCHAR(50) NOT NULL
);

CREATE TABLE Categories
(
    category_id BIGSERIAL PRIMARY KEY,
    code        VARCHAR(50)  NOT NULL,
    name        VARCHAR(200) NOT NULL
);

CREATE TABLE Categories_Tree_Path
(
    ancestor_id   BIGINT NOT NULL,
    descendant_id BIGINT NOT NULL,
    depth         INT    NOT NULL,
    PRIMARY KEY (ancestor_id, descendant_id),
    FOREIGN KEY (ancestor_id) REFERENCES Categories (category_id),
    FOREIGN KEY (descendant_id) REFERENCES Categories (category_id)
);