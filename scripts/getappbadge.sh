#!/bin/sh

apk="${1}"
id="$(basename "${apk}" .apk)"
icon="$(aapt d badging "${apk}" | perl -ne "if (/^application: .* icon='(.*?)'\$/){print \$1;}")"
unzip -p "${apk}" "${icon}" >"${id}.png"

