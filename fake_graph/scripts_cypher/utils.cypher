// Guarda todos los usuarios
:auto with 'file:///D:/Fake%20Graph/temp/usuarios/user_0.csv' as rute
LOAD CSV WITH HEADERS FROM rute AS row
CALL {
    WITH row
        MERGE (id:IdNumerico {name:'SocialUser'})
            ON CREATE SET id.total = 1
            ON MATCH SET id.total = id.total + 1
        WITH id.total AS nid, row
            MERGE (u:SocialUser {identifier:randomUUID(),
                idn: nid,
                dateBorn : localdatetime(row.dateBorn),
                registrationDate : localdatetime(row.registrationDate),
                fullName : row.fullName,
                userName : row.userName,
                email : row.email,
                language : row.language,
                onVacation : row.onVacation,
                isActive : row.isActive,
                friendRequestCount : row.friendRequestCount
            })
} IN TRANSACTIONS OF 500 ROWS

// Para eliminar sin problemas usar apoc
CALL apoc.periodic.iterate(
  "MATCH (n) RETURN n",
  "DETACH DELETE n",
  {batchSize: 50000}
)

// Asigna el tiempo medio que existe entre la fecha de Registro superior y el now()
MATCH (u1:SocialUser)-[f:FOLLOWS]-(u2:SocialUser)
WITH f, CASE WHEN u1.registrationDate > u2.registrationDate THEN u1.registrationDate ELSE u2.registrationDate END AS baseMoment
WITH f, datetime.realtime() as now, datetime(baseMoment) as lowerBounds
WITH f, now, lowerBounds, duration.inSeconds(lowerBounds, now) as durationInSeconds
WITH f, now, lowerBounds, durationInSeconds, durationInSeconds.seconds / 2 as offset
WITH f, now, lowerBounds, durationInSeconds, offset, lowerBounds + duration({seconds:offset}) as inbetweenDate
set f.momentOfFollow = inbetweenDate

// Esto se coloca en la terminal de neo4j para la base de datos
url: https://neo4j.com/docs/operations-manual/current/backup-restore/restore-dump/
// Como crear un dump de la base de datos
neo4j-admin database dump neo4j --to-path=D:/backup/
// Como cargar el dump en la base de datos
neo4j-admin database load --from-path=D:/backup/neo4j.dump --overwrite-destination=true

