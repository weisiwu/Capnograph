#!/usr/bin/env python3
"""Synchronize GitNexus-generated Markdown into the tracked MkDocs tree."""

from __future__ import annotations

import argparse
import json
import re
import sys
from pathlib import Path


REPO_ROOT = Path(__file__).resolve().parents[1]
SOURCE_DIR = REPO_ROOT / ".gitnexus" / "wiki"
TARGET_DIR = REPO_ROOT / "docs" / "generated" / "gitnexus"
CURATED_SLUGS = {
    "business-domain",
    "knowledge",
    "review-guide",
    "technical-architecture",
}
TITLES = {
    "overview": "GitNexus 代码库总览",
    "root": "Root",
    "apps": "apps",
    "apps-android": "Android",
    "apps-ios": "iOS",
    "context": "context",
    "context-docs": "context — docs",
    "context-context": "context — context",
    "docker": "docker",
    "scripts": "scripts",
    "skills": "skills",
}
CURATED_LINKS = {
    "knowledge.md": "../../index.md",
    "business-domain.md": "../../business/domain-and-workflows.md",
}
PLATFORM_ARCHITECTURE_LINKS = "\n".join(
    [
        "- **[Android 架构](../../architecture/android-architecture.md)**：Activity/Compose、全局状态、BLE Kit、Room 与输出链路",
        "- **[iOS 架构](../../architecture/ios-architecture.md)**：SwiftUI、EnvironmentObject、CoreBluetooth、内存历史与 PDF 链路",
    ]
)


def strip_frontmatter(content: str) -> str:
    return re.sub(r"\A---\r?\n.*?\r?\n---\r?\n+", "", content, flags=re.DOTALL)


def yaml_string(value: str) -> str:
    return json.dumps(value, ensure_ascii=False)


def render_page(source: Path, metadata: dict[str, object]) -> str:
    slug = source.stem
    title = TITLES.get(slug, slug)
    commit = str(metadata.get("fromCommit", "unknown"))
    generated_at = str(metadata.get("generatedAt", "unknown"))
    body = strip_frontmatter(source.read_text(encoding="utf-8")).lstrip()
    body = re.sub(r"\A# [^\r\n]+\r?\n+\s*(?=# )", "", body, count=1)
    body = re.sub(
        r"(?m)^- \*\*\[[^\]]+\]\(technical-architecture\.md\)\*\*[^\r\n]*$",
        PLATFORM_ARCHITECTURE_LINKS,
        body,
    )
    body = re.sub(
        r"(?m)^- \*\*\[审核与评审指南\]\(review-guide\.md\)\*\*[^\r\n]*$",
        "- **[数据对象与业务风险](../../business/data-and-risks.md)**：对象不变量、患者数据边界和当前待确认项",
        body,
    )
    body = body.replace(
        "按产品、研发、测试和审核角色组织的阅读入口",
        "按产品、业务和研发任务组织的阅读入口",
    )
    body = body.replace("按角色进入业务、技术或审核知识", "按角色进入业务、技术或代码模块")
    for old_target, new_target in CURATED_LINKS.items():
        body = body.replace(f"]({old_target})", f"]({new_target})")
    body = "\n".join(line.rstrip() for line in body.splitlines())
    frontmatter = "\n".join(
        [
            "---",
            f"title: {yaml_string(title)}",
            "category: generated",
            "tags:",
            "  - area/code-intelligence",
            "  - source/gitnexus",
            f"summary: {yaml_string('由 GitNexus 代码知识图谱生成的模块文档。')}",
            f"source_commit: {yaml_string(commit)}",
            f"generated_at: {yaml_string(generated_at)}",
            "metadata_status: generated",
            "search:",
            "  boost: 0.5",
            "---",
            "",
            '!!! info "GitNexus 自动生成"',
            f"    来源提交：`{commit}`；生成时间：`{generated_at}`。",
            "    本页允许同步脚本覆盖；涉及行为判断时请回到当前源码、配置和测试核验。",
            "",
        ]
    )
    return frontmatter + body.rstrip() + "\n"


def expected_pages() -> dict[Path, str]:
    if not SOURCE_DIR.is_dir():
        raise RuntimeError(
            "GitNexus Wiki 不存在；请先从仓库根目录运行 "
            "`node .gitnexus/run.cjs wiki --lang chinese`。"
        )

    meta_path = SOURCE_DIR / "meta.json"
    metadata = json.loads(meta_path.read_text(encoding="utf-8")) if meta_path.exists() else {}
    pages: dict[Path, str] = {}
    for source in sorted(SOURCE_DIR.glob("*.md")):
        if source.stem in CURATED_SLUGS:
            continue
        pages[TARGET_DIR / source.name] = render_page(source, metadata)

    missing = sorted(set(TITLES) - {path.stem for path in pages})
    if missing:
        raise RuntimeError(f"GitNexus Wiki 缺少预期页面：{', '.join(missing)}")
    return pages


def sync(check: bool) -> int:
    pages = expected_pages()
    stale: list[Path] = []
    changed: list[Path] = []

    if TARGET_DIR.exists():
        stale = sorted(path for path in TARGET_DIR.glob("*.md") if path not in pages)

    for target, expected in pages.items():
        actual = target.read_text(encoding="utf-8") if target.exists() else None
        if actual != expected:
            changed.append(target)

    if check:
        if changed or stale:
            for path in changed:
                print(f"OUTDATED {path.relative_to(REPO_ROOT)}")
            for path in stale:
                print(f"STALE {path.relative_to(REPO_ROOT)}")
            return 1
        print(f"OK: {len(pages)} GitNexus pages are synchronized")
        return 0

    TARGET_DIR.mkdir(parents=True, exist_ok=True)
    for target in stale:
        target.unlink()
        print(f"REMOVE {target.relative_to(REPO_ROOT)}")
    for target in changed:
        target.write_text(pages[target], encoding="utf-8")
        print(f"WRITE {target.relative_to(REPO_ROOT)}")
    print(f"OK: synchronized {len(pages)} GitNexus pages")
    return 0


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--check",
        action="store_true",
        help="report generated documentation drift without writing files",
    )
    args = parser.parse_args()
    try:
        return sync(check=args.check)
    except (OSError, ValueError, RuntimeError) as error:
        print(f"ERROR: {error}", file=sys.stderr)
        return 2


if __name__ == "__main__":
    raise SystemExit(main())
