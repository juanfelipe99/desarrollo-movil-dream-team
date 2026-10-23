"""Muestra en el log de GitHub Actions cada prueba con su resultado y los totales. Uso: test_report.py <título> <patrón de XML>"""
import glob
import os
import sys
import xml.etree.ElementTree as ET

title, pattern = sys.argv[1], sys.argv[2]
cases = []
for path in sorted(glob.glob(pattern, recursive=True)):
    for case in ET.parse(path).getroot().iter("testcase"):
        problem = case.find("failure")
        if problem is None:
            problem = case.find("error")
        status = "omitida" if case.find("skipped") is not None else ("falló" if problem is not None else "pasó")
        reason = ""
        if problem is not None:
            reason = (problem.get("message") or problem.text or "").strip().splitlines()[0][:300]
        cases.append((case.get("classname", "").split(".")[-1], case.get("name", ""), float(case.get("time") or 0), status, reason))

passed = sum(1 for c in cases if c[3] == "pasó")
failed = [c for c in cases if c[3] == "falló"]
skipped = sum(1 for c in cases if c[3] == "omitida")
icons = {"pasó": "✅", "falló": "❌", "omitida": "⏭️"}

print(f"\n{title}\n{'=' * len(title)}")
for test_class, name, seconds, status, reason in cases:
    print(f"{icons[status]} {test_class} > {name} ({seconds:.1f} s)")
    if reason:
        print(f"     Motivo: {reason}")
print(f"\nTotal: {len(cases)}   Pasaron: {passed}   Fallaron: {len(failed)}   Omitidas: {skipped}\n")

for test_class, name, _, _, reason in failed:
    print(f"::error title={test_class} > {name}::{reason or 'La prueba falló'}")

summary = os.environ.get("GITHUB_STEP_SUMMARY")
if summary:
    with open(summary, "a", encoding="utf-8") as out:
        out.write(f"## {title}\n\n**Total:** {len(cases)} · **Pasaron:** {passed} · **Fallaron:** {len(failed)} · **Omitidas:** {skipped}\n\n")
        out.write("| | Clase | Prueba | Duración |\n|---|---|---|---|\n")
        for test_class, name, seconds, status, _ in cases:
            out.write(f"| {icons[status]} | {test_class} | {name} | {seconds:.1f} s |\n")
        out.write("\n")

if not cases:
    print("::error::No se ejecutó ninguna prueba")
    sys.exit(1)
sys.exit(1 if failed else 0)
