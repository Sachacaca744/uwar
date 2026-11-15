# UWar - depot GitHub (compilation automatique)

GitHub compile le mod a ta place (gratuit). Rien a installer sur ton PC.

## Mise en place (5 minutes)
1. Va sur github.com -> **New repository** -> nom : `uwar` -> **Public** (pour que Claude puisse lire le depot) -> Create.
2. Clique **uploading an existing file**, glisse TOUT le contenu de ce zip (y compris le dossier `.github`, cache sur Windows : active "Elements masques"), puis **Commit changes**.
   Astuce : si le dossier `.github` ne passe pas par glisser-deposer, cree le fichier a la main : **Add file -> Create new file**, nom `.github/workflows/1-preparer-source.yml`, colle le contenu, puis pareil pour `2-compiler.yml`.
3. Onglet **Actions** -> **1 - Preparer le projet source** -> **Run workflow**. Duree : environ 5 a 10 minutes.
   Il decompile le jar, remet les noms lisibles (Forge/Mojang) et enregistre le dossier `project/` dans ton depot.
4. Des que `project/` apparait, **2 - Compiler le mod** se lance seul et tente de compiler.
5. Envoie-moi le lien du depot (et, si une etape est rouge, le texte du journal d'erreur : clique sur l'etape en echec -> copie les dernieres lignes).

## Ensuite
Je lis le projet depuis GitHub, j'ajoute les 5 armes, le B-21 et le missile nucleaire (voir `BRIEF_uwar.md`),
je te rends les fichiers modifies a remplacer dans `project/`, et GitHub recompile.
Le jar final est dans **Actions -> dernier run -> Artifacts -> resultat-compilation**.

## Honnetete
Ces workflows n'ont pas pu etre testes ici (pas d'acces a Forge). Si une etape echoue, c'est normal au premier essai :
envoie-moi le journal et je corrige. Le jar original est dans `original/`.
