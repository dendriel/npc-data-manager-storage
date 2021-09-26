INSERT INTO directory (name, storage_id)
SELECT * FROM (
                  SELECT 'default', 'a5226a957ca44cfda8d60f9de5759092'
              ) AS tmp
WHERE NOT EXISTS(SELECT * FROM directory WHERE name='default') LIMIT 1;
