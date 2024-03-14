CREATE TABLE IF NOT EXISTS Ingredient
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Recipe
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS Unit
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
);

CREATE TABLE IF NOT EXISTS Composition
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    recipe_id     INT,
    ingredient_id INT,
    amount        FLOAT NOT NULL,
    unit_id       INT,

    CONSTRAINT FK_CompositionRecipe FOREIGN KEY (recipe_id) REFERENCES Recipe (id),
    CONSTRAINT FK_CompositionIngredient FOREIGN KEY (ingredient_id) REFERENCES Ingredient (id),
    CONSTRAINT FK_CompositionUnit FOREIGN KEY (unit_id) REFERENCES Unit (id)
)