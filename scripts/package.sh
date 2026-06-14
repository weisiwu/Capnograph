#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

target=""
variant="debug"
scheme="CapnoGraph"
configuration=""
destination=""
archive_path=""
export_options_plist=""
export_path=""
derived_data_path=""
extra_args=()

usage() {
  cat <<'USAGE'
Usage:
  scripts/package.sh --target android [--variant debug|release] [-- ...extra gradle args]
  scripts/package.sh --target ios [--variant debug|release] [--scheme CapnoGraph] [--configuration Debug|Release]

Options:
  --target                 Build target: android or ios.
  --variant                Build variant: debug or release. Default: debug.
  --scheme                 iOS scheme. Default: CapnoGraph.
  --configuration          iOS configuration. Defaults to Debug/Release from variant.
  --destination            iOS xcodebuild destination.
  --archive-path           iOS archive path for release builds.
  --export-options-plist   ExportOptions.plist used after iOS archive.
  --export-path            iOS export output path. Default: apps/ios/build/export.
  --derived-data-path      iOS DerivedData path. Default: apps/ios/build/DerivedData.
  -h, --help               Show this help.

Examples:
  scripts/package.sh --target android --variant debug
  scripts/package.sh --target android --variant release -- --no-daemon
  scripts/package.sh --target ios --variant debug
  scripts/package.sh --target ios --variant release --export-options-plist ExportOptions.plist
USAGE
}

require_value() {
  local option="$1"
  local value="${2:-}"
  if [[ -z "$value" || "$value" == --* ]]; then
    echo "Missing value for $option" >&2
    exit 2
  fi
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --target)
      require_value "$1" "${2:-}"
      target="$2"
      shift 2
      ;;
    --variant)
      require_value "$1" "${2:-}"
      variant="$2"
      shift 2
      ;;
    --scheme)
      require_value "$1" "${2:-}"
      scheme="$2"
      shift 2
      ;;
    --configuration)
      require_value "$1" "${2:-}"
      configuration="$2"
      shift 2
      ;;
    --destination)
      require_value "$1" "${2:-}"
      destination="$2"
      shift 2
      ;;
    --archive-path)
      require_value "$1" "${2:-}"
      archive_path="$2"
      shift 2
      ;;
    --export-options-plist)
      require_value "$1" "${2:-}"
      export_options_plist="$2"
      shift 2
      ;;
    --export-path)
      require_value "$1" "${2:-}"
      export_path="$2"
      shift 2
      ;;
    --derived-data-path)
      require_value "$1" "${2:-}"
      derived_data_path="$2"
      shift 2
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    --)
      shift
      extra_args=("$@")
      break
      ;;
    *)
      echo "Unknown option: $1" >&2
      usage >&2
      exit 2
      ;;
  esac
done

if [[ -z "$target" ]]; then
  echo "Missing required option: --target" >&2
  usage >&2
  exit 2
fi

build_android() {
  local task
  case "$variant" in
    debug)
      task="assembleDebug"
      ;;
    release)
      task="assembleRelease"
      ;;
    *)
      echo "Unsupported Android variant: $variant" >&2
      exit 2
      ;;
  esac

  cd "$repo_root/apps/android"
  ./gradlew ":app:$task" "${extra_args[@]}"
}

build_ios() {
  if [[ -z "$configuration" ]]; then
    case "$variant" in
      debug)
        configuration="Debug"
        ;;
      release)
        configuration="Release"
        ;;
      *)
        echo "Unsupported iOS variant: $variant" >&2
        exit 2
        ;;
    esac
  fi

  cd "$repo_root/apps/ios"

  if [[ -z "$derived_data_path" ]]; then
    derived_data_path="$repo_root/apps/ios/build/DerivedData"
  fi

  case "$variant" in
    debug)
      if [[ -z "$destination" ]]; then
        destination="generic/platform=iOS Simulator"
      fi
      xcodebuild \
        -project CapnoGraph.xcodeproj \
        -scheme "$scheme" \
        -configuration "$configuration" \
        -destination "$destination" \
        -derivedDataPath "$derived_data_path" \
        build \
        "${extra_args[@]}"
      ;;
    release)
      if [[ -z "$destination" ]]; then
        destination="generic/platform=iOS"
      fi
      if [[ -z "$archive_path" ]]; then
        archive_path="$repo_root/apps/ios/build/${scheme}.xcarchive"
      fi
      xcodebuild \
        -project CapnoGraph.xcodeproj \
        -scheme "$scheme" \
        -configuration "$configuration" \
        -destination "$destination" \
        -archivePath "$archive_path" \
        -derivedDataPath "$derived_data_path" \
        archive \
        "${extra_args[@]}"

      if [[ -n "$export_options_plist" ]]; then
        if [[ -z "$export_path" ]]; then
          export_path="$repo_root/apps/ios/build/export"
        fi
        xcodebuild \
          -exportArchive \
          -archivePath "$archive_path" \
          -exportOptionsPlist "$export_options_plist" \
          -exportPath "$export_path"
      else
        echo "Archive created at: $archive_path"
        echo "Pass --export-options-plist to export an ipa."
      fi
      ;;
    *)
      echo "Unsupported iOS variant: $variant" >&2
      exit 2
      ;;
  esac
}

case "$target" in
  android)
    build_android
    ;;
  ios)
    build_ios
    ;;
  *)
    echo "Unsupported target: $target" >&2
    exit 2
    ;;
esac
