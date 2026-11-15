#!/usr/bin/env python3
"""Sortie de CFR -> projet Forge 1.20.1 : remplace les noms SRG (m_123_, f_456_)
par les noms officiels Mojang, avec la table generee par ForgeGradle (createMcpToSrg)."""
import os, re, sys, glob, shutil, zipfile, pathlib

ROOT = pathlib.Path(os.environ.get("ROOT", ".")).resolve()
MDK = ROOT / "mdk"
DEC = ROOT / "decompiled"
JAR = ROOT / "original" / "uwar-1.0.0.jar"
OUT = ROOT / "project"
SRG = re.compile(r"^[mf]_\d+_$")
TOKEN = re.compile(r"\b[mf]_\d+_\b")

def parse(path):
    m = {}
    try:
        lines = open(path, encoding="utf-8", errors="replace").read().splitlines()
    except OSError:
        return m
    for ln in lines:
        if not ln.startswith(("\t", " ")) or not ln.strip():
            continue
        toks = ln.split()
        srg = [t for t in toks if SRG.match(t)]
        if len(srg) != 1:
            continue
        others = [t for t in toks if t != srg[0] and not t.startswith("(")]
        if not others:
            continue
        name = others[0]
        if SRG.match(name) or not re.match(r"^[A-Za-z_$][\w$]*$", name):
            continue
        m[srg[0]] = name
    return m

def find_table():
    cands = []
    for pat in (str(MDK / "build" / "**" / "*.tsrg"), str(MDK / "build" / "**" / "*.srg"),
                os.path.expanduser("~/.gradle/caches/forge_gradle/**/*.tsrg"),
                os.path.expanduser("~/.gradle/caches/forge_gradle/**/*.srg")):
        cands += glob.glob(pat, recursive=True)
    best, bestm = None, {}
    for c in cands:
        m = parse(c)
        print(f"  table candidate {c}: {len(m)} noms")
        if len(m) > len(bestm):
            best, bestm = c, m
    return best, bestm

def main():
    best, table = find_table()
    if not table:
        sys.exit("ERREUR: aucune table de correspondance SRG->officiel trouvee dans mdk/build")
    print(f"Table utilisee: {best} ({len(table)} noms)")

    if OUT.exists():
        shutil.rmtree(OUT)
    OUT.mkdir()
    skip = {"build", ".gradle", "run", "src", ".git", ".idea"}
    for e in MDK.iterdir():
        if e.name in skip or e.suffix == ".zip":
            continue
        if e.is_dir():
            shutil.copytree(e, OUT / e.name)
        else:
            shutil.copy2(e, OUT / e.name)

    gp = OUT / "gradle.properties"
    txt = gp.read_text()
    for key, val in (("mod_id", "uwar"), ("mod_name", "UWar"), ("mod_version", "1.0.0"),
                     ("mod_group_id", "fr.uwar"), ("mod_authors", "uwar"), ("mod_description", "UWar")):
        txt = re.sub(rf"(?m)^{key}=.*$", f"{key}={val}", txt)
    gp.write_text(txt)

    java_out = OUT / "src" / "main" / "java"
    unknown = {}
    count = 0
    for src in DEC.rglob("*.java"):
        rel = src.relative_to(DEC)
        text = src.read_text(encoding="utf-8", errors="replace")
        def sub(mo):
            t = mo.group(0)
            if t in table:
                return table[t]
            unknown[t] = unknown.get(t, 0) + 1
            return t
        text = TOKEN.sub(sub, text)
        dst = java_out / rel
        dst.parent.mkdir(parents=True, exist_ok=True)
        dst.write_text(text, encoding="utf-8")
        count += 1
    print(f"{count} fichiers Java ecrits")

    res_out = OUT / "src" / "main" / "resources"
    res_out.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(JAR) as z:
        for n in z.namelist():
            if n.endswith("/") or n.endswith(".class") or n == "META-INF/MANIFEST.MF":
                continue
            d = res_out / n
            d.parent.mkdir(parents=True, exist_ok=True)
            d.write_bytes(z.read(n))

    rep = ["Noms SRG sans correspondance (a corriger si le build echoue) :"]
    rep += [f"  {k} x{v}" for k, v in sorted(unknown.items())] or ["  aucun"]
    (OUT / "REMAP_REPORT.txt").write_text("\n".join(rep) + "\n")
    print("\n".join(rep))

if __name__ == "__main__":
    main()
