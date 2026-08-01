-- Initial drawing quests available after a fresh database migration.
INSERT INTO quests (title, description, difficulty, xp_reward)
SELECT 'Dibuja una casa flotante',
       'Imagina una casa que flota en el cielo, en el mar o en cualquier lugar imposible.',
       1,
       25
WHERE NOT EXISTS (SELECT 1 FROM quests WHERE title = 'Dibuja una casa flotante');

INSERT INTO quests (title, description, difficulty, xp_reward)
SELECT 'Crea un animal robot',
       'Dibuja tu animal favorito convertido en robot, con piezas mecanicas y personalidad propia.',
       1,
       30
WHERE NOT EXISTS (SELECT 1 FROM quests WHERE title = 'Crea un animal robot');

INSERT INTO quests (title, description, difficulty, xp_reward)
SELECT 'Disena una ciudad bajo el agua',
       'Construye una escena submarina con edificios, habitantes y detalles que cuenten una historia.',
       2,
       50
WHERE NOT EXISTS (SELECT 1 FROM quests WHERE title = 'Disena una ciudad bajo el agua');

INSERT INTO quests (title, description, difficulty, xp_reward)
SELECT 'Inventa un heroe de fantasia',
       'Crea un personaje original con ropa, herramientas o poderes que expliquen quien es.',
       2,
       55
WHERE NOT EXISTS (SELECT 1 FROM quests WHERE title = 'Inventa un heroe de fantasia');

INSERT INTO quests (title, description, difficulty, xp_reward)
SELECT 'Dibuja una escena con luz dramatica',
       'Practica sombras, contraste y composicion creando una escena iluminada por una sola fuente de luz.',
       3,
       80
WHERE NOT EXISTS (SELECT 1 FROM quests WHERE title = 'Dibuja una escena con luz dramatica');
