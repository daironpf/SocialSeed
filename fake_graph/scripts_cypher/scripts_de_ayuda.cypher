// Retorna la cantidad de nodos que hay por cada label en la base de datos
MATCH (n)
RETURN DISTINCT labels(n) AS label, count(*) AS count
ORDER BY label;

// retorna la cantidad de relaciones por tipo
MATCH ()-[r]->()
RETURN DISTINCT type(r) AS relationship_type, count(*) AS count
ORDER BY relationship_type;

// Eliminar nodos en grandes cantidades
:auto MATCH (n)
CALL { WITH n
DETACH DELETE n
} IN TRANSACTIONS OF 10000 ROWS;