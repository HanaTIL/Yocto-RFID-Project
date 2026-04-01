FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://wpa_supplicant.conf-sane"

do_install:append() {
    install -m 0600 ${WORKDIR}/wpa_supplicant.conf-sane ${D}${sysconfdir}/wpa_supplicant.conf
}
