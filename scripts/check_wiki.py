#!/usr/bin/env python3
"""Run lightweight structural checks for the tracked CapnoEasy Wiki."""

from __future__ import annotations

import re
import sys
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
DOCS = ROOT / "docs"
LINK_PATTERN = re.compile(r"\[[^\]]+\]\(([^)]+)\)")
MERMAID_PATTERN = re.compile(r"```mermaid\s*\n(.*?)```", re.DOTALL)


def frontmatter_body(content: str) -> str:
    return re.sub(r"\A---\r?\n.*?\r?\n---\r?\n", "", content, count=1, flags=re.DOTALL)


def validate_page(path: Path) -> list[str]:
    failures: list[str] = []
    content = path.read_text(encoding="utf-8")
    body = frontmatter_body(content)
    visible_body = re.sub(r"```.*?```", "", body, flags=re.DOTALL)
    relative = path.relative_to(ROOT)

    h1_count = len(re.findall(r"^# ", visible_body, flags=re.MULTILINE))
    if h1_count != 1:
        failures.append(f"{relative}: expected one H1, found {h1_count}")

    if re.search(r"^# .*gitnexus\..*Dir — Wiki$", body, flags=re.MULTILINE | re.IGNORECASE):
        failures.append(f"{relative}: leaked GitNexus temporary directory heading")

    if "generated/gitnexus" not in relative.as_posix():
        for number, diagram in enumerate(MERMAID_PATTERN.findall(content), start=1):
            if "accTitle:" not in diagram or "accDescr:" not in diagram:
                failures.append(f"{relative}: Mermaid diagram {number} lacks accTitle/accDescr")

    for target in LINK_PATTERN.findall(content):
        target = target.split()[0].strip("<>")
        if not target or target.startswith(("#", "http://", "https://", "mailto:")):
            continue
        local_target = target.split("#", 1)[0]
        if local_target and not (path.parent / local_target).resolve().exists():
            failures.append(f"{relative}: missing local link target {target}")

    return failures


def main() -> int:
    pages = sorted(DOCS.rglob("*.md"))
    failures = [failure for page in pages for failure in validate_page(page)]
    if failures:
        print("Wiki checks failed:")
        for failure in failures:
            print(f"- {failure}")
        return 1
    print(f"OK: validated {len(pages)} Markdown pages")
    return 0


if __name__ == "__main__":
    sys.exit(main())
