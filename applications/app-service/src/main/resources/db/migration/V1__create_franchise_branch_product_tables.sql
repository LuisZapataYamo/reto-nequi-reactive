CREATE TABLE IF NOT EXISTS franchise (
                                         id UUID PRIMARY KEY,
                                         name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS branch (
                                      id UUID PRIMARY KEY,
                                      name VARCHAR(255),
                                      franchise_id UUID,
                                      CONSTRAINT fk_branch_franchise FOREIGN KEY (franchise_id) REFERENCES franchise (id)
);


CREATE TABLE IF NOT EXISTS product (
                                       id UUID PRIMARY KEY,
                                       name VARCHAR(255),
                                       stock INT,
                                       branch_id UUID,
                                       CONSTRAINT fk_product_branch FOREIGN KEY (branch_id) REFERENCES branch (id)
);