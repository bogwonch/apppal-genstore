#!/bin/sh

apk="${1}"
icon="$(aapt d badging "${apk}" | perl -ne "if (/^application: .* icon='(.*?)'\$/){print \$1;}")"
unzip -p "${apk}" "${icon}"

