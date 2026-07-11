#!/usr/bin/env bash
set -Eeuo pipefail

readonly SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
readonly GENERATOR_SOURCE="$SCRIPT_DIR/tools/generate_trusted_signers.rs"
readonly GENERATOR_BINARY="${TMPDIR:-/tmp}/android-rasp-generate-trusted-signers"
readonly OUTPUT="$SCRIPT_DIR/src/integrity/trusted_signers_data.rs"

usage() {
  echo "Usage: $0 --apk <signed.apk> [--apk <signed.apk> ...]"
  echo "       $0 --sha256 <certificate-fingerprint> [--sha256 <fingerprint> ...]"
}

digests=()
while [[ $# -gt 0 ]]; do
  case "$1" in
    --apk)
      apk="$2"
      apksigner="${APKSIGNER:-}"
      if [[ -z "$apksigner" ]]; then
        apksigner="$(find "${ANDROID_HOME:-$HOME/Library/Android/sdk}/build-tools" -name apksigner -type f | sort -V | tail -1)"
      fi
      [[ -x "$apksigner" ]] || { echo "apksigner not found" >&2; exit 1; }
      digest="$($apksigner verify --print-certs "$apk" | sed -n 's/^.*certificate SHA-256 digest: //p' | head -1)"
      [[ -n "$digest" ]] || { echo "could not read signing certificate from $apk" >&2; exit 1; }
      digests+=("$digest")
      shift 2
      ;;
    --sha256)
      digests+=("$2")
      shift 2
      ;;
    *) usage; exit 1 ;;
  esac
done

[[ ${#digests[@]} -gt 0 ]] || { usage; exit 1; }
rustc "$GENERATOR_SOURCE" -o "$GENERATOR_BINARY"
"$GENERATOR_BINARY" "$OUTPUT" "${digests[@]}"
echo "Updated $OUTPUT with ${#digests[@]} trusted signer(s)."
