FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# Only include the credentials and the systemd override
SRC_URI += "file://wifi.config \
            file://wlan-enable.conf"

do_install:append() {
    # Install the WiFi credentials (SSID/Password)
    install -d ${D}${localstatedir}/lib/connman
    install -m 0600 ${WORKDIR}/wifi.config ${D}${localstatedir}/lib/connman/

    # Install the Systemd Override (The "Forcing" fix)
    install -d ${D}${systemd_system_unitdir}/connman.service.d
    install -m 0644 ${WORKDIR}/wlan-enable.conf ${D}${systemd_system_unitdir}/connman.service.d/
}
# Tell Yocto to include the new directory in the package
FILES:${PN} += "${systemd_system_unitdir}/connman.service.d/*"