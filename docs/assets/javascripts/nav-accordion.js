function getDirectChild(element, selector) {
  return Array.from(element.children).find((child) => child.matches(selector));
}

function getNavigationLabel(item) {
  const label = getDirectChild(item, "label.md-nav__link");
  return label ? label.textContent.replace(/\s+/g, " ").trim() : "";
}

function getDirectNavigationItems(item) {
  const nestedNavigation = getDirectChild(item, "nav.md-nav");
  const nestedList = nestedNavigation
    ? getDirectChild(nestedNavigation, "ul.md-nav__list")
    : null;

  return nestedList
    ? Array.from(nestedList.children).filter((child) =>
        child.classList.contains("md-nav__item"),
      )
    : [];
}

function updateAccordionState(toggle) {
  const item = toggle.parentElement;
  const label = getDirectChild(item, "label.md-nav__link");
  const nestedNavigation = getDirectChild(item, "nav.md-nav");
  const expanded = String(toggle.checked);

  if (label) label.setAttribute("aria-expanded", expanded);
  if (nestedNavigation) nestedNavigation.setAttribute("aria-expanded", expanded);
}

function initializeCodeReferenceAccordion() {
  document.querySelectorAll("nav.md-nav--primary").forEach((primaryNavigation) => {
    const rootList = getDirectChild(primaryNavigation, "ul.md-nav__list");
    if (!rootList) return;

    const codeReference = Array.from(rootList.children).find(
      (item) => getNavigationLabel(item) === "代码模块参考",
    );
    if (!codeReference || codeReference.dataset.accordionReady === "true") return;

    const codeReferenceNavigation = getDirectChild(codeReference, "nav.md-nav");
    const groupList = codeReferenceNavigation
      ? getDirectChild(codeReferenceNavigation, "ul.md-nav__list")
      : null;
    if (!groupList) return;

    const groups = Array.from(groupList.children).filter((item) =>
      item.classList.contains("md-nav__item--nested"),
    );
    if (groups.length === 0) return;

    const collapsibleGroups = groups.filter(
      (group) => getDirectNavigationItems(group).length > 1,
    );
    const staticGroups = groups.filter(
      (group) => getDirectNavigationItems(group).length <= 1,
    );
    const toggles = collapsibleGroups
      .map((item) => getDirectChild(item, "input.md-nav__toggle"))
      .filter(Boolean);

    codeReference.dataset.accordionReady = "true";
    codeReference.classList.add("wiki-nav-accordion");
    groups.forEach((group) => group.classList.add("wiki-nav-accordion__group"));
    collapsibleGroups.forEach((group) =>
      group.classList.add("wiki-nav-accordion__group--collapsible"),
    );
    staticGroups.forEach((group) => {
      group.classList.add("wiki-nav-accordion__group--static");
      const toggle = getDirectChild(group, "input.md-nav__toggle");
      if (!toggle) return;
      toggle.checked = true;
      updateAccordionState(toggle);
    });

    toggles.forEach((toggle) => {
      toggle.addEventListener("change", () => {
        updateAccordionState(toggle);
        if (!toggle.checked) return;

        toggles.forEach((otherToggle) => {
          if (otherToggle === toggle || !otherToggle.checked) return;
          otherToggle.checked = false;
          updateAccordionState(otherToggle);
        });
      });
    });

    const activeGroup = collapsibleGroups.find((group) =>
      group.classList.contains("md-nav__item--active"),
    );
    const activeToggle = activeGroup
      ? getDirectChild(activeGroup, "input.md-nav__toggle")
      : null;
    const initiallyOpen = activeToggle || toggles.find((toggle) => toggle.checked);

    if (activeToggle) activeToggle.checked = true;

    toggles.forEach((toggle) => {
      if (initiallyOpen && toggle !== initiallyOpen) toggle.checked = false;
      updateAccordionState(toggle);
    });
  });
}

if (typeof document$ !== "undefined") {
  document$.subscribe(initializeCodeReferenceAccordion);
} else if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initializeCodeReferenceAccordion);
} else {
  initializeCodeReferenceAccordion();
}
