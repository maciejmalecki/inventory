CREATE TABLE Units
(
    code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE Attribute_Types
(
    name   VARCHAR(50) PRIMARY KEY,
    unit   VARCHAR(20),
    scalar BOOLEAN NOT NULL,
    FOREIGN KEY (unit) REFERENCES Units (code)
);

CREATE TABLE Attribute_Type_Values
(
    attribute_name VARCHAR(50)  NOT NULL,
    value          VARCHAR(200) NOT NULL,
    PRIMARY KEY (attribute_name, value),
    FOREIGN KEY (attribute_name) REFERENCES Attribute_Types (name)
);

CREATE TABLE Item_Classes
(
    name        VARCHAR(50) PRIMARY KEY,
    description VARCHAR(200) NOT NULL
);

CREATE TABLE Attributes
(
    name            VARCHAR(50)  NOT NULL,
    item_class_name VARCHAR(200) NOT NULL,
    PRIMARY KEY (name, item_class_name),
    FOREIGN KEY (item_class_name) REFERENCES Item_Classes (name)
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