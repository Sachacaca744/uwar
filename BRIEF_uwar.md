# Mission : étendre le mod Forge « uwar » (Minecraft 1.20.1 / Forge 47.x)

Je n'ai que le jar compilé `uwar-1.0.0.jar` (pas de source). Fais tout le travail sur cette machine, compile, teste, puis donne-moi le `.jar` final (et un zip contenant le jar).

## Étape 0 - Mise en place (outils gratuits)
1. Vérifie qu'un JDK 17 est installé (sinon l'installer : Temurin 17).
2. Crée un projet Forge 1.20.1 (MDK 47.3.0 ou plus récent 47.x), mappings **official** (ou parchment).
3. Décompile `uwar-1.0.0.jar` avec CFR (https://github.com/leibnitz27/cfr/releases). Le jar est **reobfusqué en noms SRG** (`m_8097_`, `f_19811_`...) : il faut remapper SRG -> noms officiels (les fichiers de mappings sont téléchargés par ForgeGradle) pour obtenir un code qui compile dans le MDK.
4. Reprends les ressources du jar telles quelles (`assets/uwar/**`, `data/uwar/**`, `pack.mcmeta`, `META-INF/mods.toml`).
5. Le projet doit compiler (`./gradlew build`) et le jar produit doit fonctionner. Lance un vrai serveur de test (`./gradlew runServer`) pour vérifier le chargement, les entités, les tirs et les effets décrits plus bas. Corrige toutes les erreurs avant de me rendre le résultat.

## Structure actuelle du mod (déjà analysée)
- Package `fr.uwar` : `Uwar`, `Reg`, `Aircraft` (entité pilotable, ~44 Ko), `AircraftItem`, `Shell` (projectile), `Blast` (explosions), `W` (enum des armes), `Kind` (enum des appareils), `Net` (messages réseau : `InputMsg`, `CmdMsg`, `LoadoutMsg`), `Cfg` (config `uwar-common.toml`), `SupplyItem` (munitions/bidon/kit), `TabHook` (onglet créatif `utab:main`).
- Package `fr.uwar.client` : `AircraftRenderer`, `Hud`, `LoadoutScreen` (écran d'armement, touche 5), `Models`, `Mesh`, `ShellRenderer`, `AircraftSound`, `ClientEvents`, `ClientHooks`.
- Appareils actuels : hélicoptère AH-64 (3 emplacements + canon), avion F-16 (4 emplacements + canon).
- `W` : chaque arme = (label, shortName, dmg, radius, speed, cooldown, perItem, item, heavy, desc). Armes actuelles : CANNON, ROCKET, MISSILE, BOMB, IR, FLAK, CRUISE, BALLISTIC, BUSTER, BAY.
- Les armes « heavy » détruisent les blocs (option `heavyBlockDamage`), les autres non (option `blockDamage`).
- Chaque arme a un objet de munition, une recette (`data/uwar/recipes`), une texture d'objet, un modèle d'objet, des entrées de langue `fr_fr.json` et `en_us.json`.

Respecte ce style : réutilise `W`, `Shell`, `Blast`, `Cfg`, `Net`, `LoadoutScreen` et `Hud` au lieu de réinventer.

## Tâche 1 - 5 nouvelles armes
Ajoute 5 armes (installables via l'écran d'armement, avec objet de munition, recette survie, texture 16x16, modèle d'objet, textes FR/EN, ligne dans le HUD et dans la touche G) :
1. **Mitraillette gros calibre** (tir automatique, dégâts élevés par balle, cadence rapide, traceur) qui **casse très peu de blocs** (par exemple 0 à 1 bloc tendre par impact, désactivable dans `Cfg`).
2. Trois autres armes cohérentes avec le jeu, par exemple : missile anti-char (gros dégâts directs sur une cible), bombe à sous-munitions, napalm / bombe incendiaire (zone de feu), roquettes lourdes. Chacune doit avoir un rôle distinct.
3. La cinquième est libre. Elle doit être différente des quatre autres.
Équilibre les valeurs par rapport aux armes existantes. Les nouvelles armes sont utilisables sur l'AH-64, le F-16 et le B-21 (sauf mention contraire).

## Tâche 2 - Nouvel appareil : bombardier B-21 Raider
- Nouvelle valeur dans `Kind`, nouvel objet (version normale + version équipée, comme les autres), recette de survie coûteuse.
- Modèle 3D procédural dans le style de `Models`/`Mesh` (aile volante noire, sans empennage), texture dans `textures/entity/`.
- Pilotage de type avion (réutiliser la physique du F-16, plus lourd et plus lent, grand rayon de virage), 2 places (pilote + passager) si possible, sinon 1.
- Armement : 6 emplacements + canon optionnel, soute à bombes lourdes (BAY) disponible, **plus l'arme exclusive ci-dessous**.
- HUD, sons, rendu et caméra 3e personne comme pour les autres appareils.

## Tâche 3 - Missile nucléaire (exclusif au B-21)
Arme `NUKE` : « missile de puissance 1/10 » (un dixième de la puissance d'une vraie arme nucléaire). N'est installable QUE sur le B-21 (refus sinon dans `LoadoutMessage` côté serveur ET masqué côté client).
Munition : objet `nuke_missile`, recette très chère (ex. blocs de netherite/diamant + TNT + étoile du Nether).

Effets à l'impact (tous les rayons et durées doivent être dans `Cfg`, avec ces valeurs par défaut) :
1. **Explosion immense** : énorme cratère au centre (rayon ~40 blocs), onde de choc avec dégâts décroissants jusqu'à ~120 blocs, bruit d'explosion, flash blanc + particules de champignon.
2. **Brume verte pendant 2 heures réelles** (144 000 ticks) : zone au centre (rayon ~60 blocs) remplie de particules vertes, qui **inflige des dégâts** continus aux joueurs/mobs dedans (poison/wither + dégâts directs, réglable). Se dissipe à la fin du délai.
3. **Zone extérieure** (rayon ~60 à ~150 blocs) : le sol devient **marron** (remplacer herbe/pierre claire par terre/coarse dirt/podzol/mud), **tous les arbres perdent leurs feuilles** (feuilles supprimées, troncs conservés), **fumée noire** (particules de fumée de camp/`LARGE_SMOKE`) et **feux permanents** pendant 2 heures (144 000 ticks) : pas d'extinction par la pluie, les feux sont recréés/entretenus.
4. La durée est une constante `Cfg` (en ticks). Par défaut 144 000 pour la brume. Pour les feux, la fumée et le sol marron, prévoir un réglage séparé (par défaut aussi 144 000 ticks ; le sol et les arbres restent marron/sans feuilles de façon permanente ou jusqu'à la fin selon l'option `fallout_terrain_permanent`).

Exigences techniques (importantes) :
- Sauvegarde persistante via `SavedData` (zones actives avec position, dimension, tick de fin), pour que ça survive à un redémarrage du serveur.
- **Performance** : ne jamais modifier des milliers de blocs dans un seul tick. Mets une file de travail traitée avec un budget par tick (ex. 500 blocs/tick), et traite aussi les chunks non chargés au moment de leur chargement (`ChunkEvent.Load`).
- Particules envoyées seulement aux joueurs proches. Pas de calcul coûteux hors des chunks chargés.
- Les dégâts de la brume doivent suivre `Cfg.damageMultiplier`.
- Option `nukeEnabled` dans `Cfg` pour désactiver l'arme.

## Livrables
- Le jar final `uwar-1.1.0.jar` (version incrémentée dans `mods.toml`), dans un zip avec le `LISEZMOI.txt` mis à jour (nouvelles armes, B-21, missile nucléaire, recettes, commandes, config).
- Le projet source complet (je veux pouvoir le recompiler).
- Un résumé honnête de ce qui a été testé et de ce qui ne l'a pas été.
