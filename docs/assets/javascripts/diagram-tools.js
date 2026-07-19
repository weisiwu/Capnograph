function initializeDiagramTools() {
  document.querySelectorAll("figure.wiki-diagram").forEach((figure, index) => {
    if (figure.dataset.diagramToolsReady === "true") return;

    const diagram = figure.querySelector(".mermaid");
    if (!diagram) return;

    figure.dataset.diagramToolsReady = "true";
    figure.dataset.diagramScale = "1";

    const toolbar = document.createElement("div");
    toolbar.className = "wiki-diagram__toolbar";
    toolbar.setAttribute("role", "group");
    toolbar.setAttribute("aria-label", `图表 ${index + 1} 查看工具`);

    const viewport = document.createElement("div");
    viewport.className = "wiki-diagram__viewport";
    diagram.parentNode.insertBefore(viewport, diagram);
    viewport.appendChild(diagram);

    function setScale(value) {
      const scale = Math.min(1.8, Math.max(0.7, value));
      figure.dataset.diagramScale = String(scale);
      diagram.style.transform = `scale(${scale})`;
      diagram.style.transformOrigin = "top left";
      viewport.style.minHeight = `${Math.ceil(diagram.scrollHeight * scale)}px`;
      toolbar.querySelector("[data-diagram-scale]").textContent = `${Math.round(scale * 100)}%`;
    }

    function addButton(label, action, className = "") {
      const button = document.createElement("button");
      button.type = "button";
      button.className = `wiki-diagram__button ${className}`.trim();
      button.textContent = label;
      button.addEventListener("click", action);
      toolbar.appendChild(button);
      return button;
    }

    addButton("−", () => setScale(Number(figure.dataset.diagramScale) - 0.1), "wiki-diagram__button--compact").setAttribute("aria-label", "缩小图表");
    const scaleLabel = document.createElement("span");
    scaleLabel.dataset.diagramScale = "true";
    scaleLabel.className = "wiki-diagram__scale";
    scaleLabel.textContent = "100%";
    scaleLabel.setAttribute("aria-live", "polite");
    toolbar.appendChild(scaleLabel);
    addButton("+", () => setScale(Number(figure.dataset.diagramScale) + 0.1), "wiki-diagram__button--compact").setAttribute("aria-label", "放大图表");
    addButton("重置", () => setScale(1));

    if (figure.requestFullscreen) {
      addButton("全屏", async (event) => {
        if (document.fullscreenElement === figure) {
          await document.exitFullscreen();
          event.currentTarget.textContent = "全屏";
        } else {
          await figure.requestFullscreen();
          event.currentTarget.textContent = "退出全屏";
        }
      }, "wiki-diagram__button--primary");
    }

    addButton("打印 / 保存", () => window.print());
    viewport.parentNode.insertBefore(toolbar, viewport);
    requestAnimationFrame(() => setScale(1));
  });
}

if (typeof document$ !== "undefined") {
  document$.subscribe(initializeDiagramTools);
} else if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initializeDiagramTools);
} else {
  initializeDiagramTools();
}
