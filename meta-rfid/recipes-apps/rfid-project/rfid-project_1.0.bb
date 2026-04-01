SUMMARY = "RFID Project with Shared Library and Dual Systemd Services"
LICENSE = "CLOSED"

# Inherit systemd class to automate service enabling
inherit systemd

# Source files - GitHub + local service files
SRC_URI = "git://github.com/HanaTIL/RFID_Project.git;protocol=https;branch=main "


# To be replaced with the actual latest commit hash
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"
TARGET_CFLAGS += "-I${S}/include"


# 3. Dependencies
DEPENDS = ""
RDEPENDS:${PN} = "sqlite3 python3-core python3-sqlite3 python3-netclient \
                  connman connman-client wireless-regdb-static"

# 4. Systemd configuration
SYSTEMD_SERVICE:${PN} = "rfid_hw.service rfid_logic.service rfid_setup.service"
SYSTEMD_AUTO_ENABLE = "enable"

# 5. Compilation: 
do_compile() {
    oe_runmake
}

# 6. Installation: This replaces all your manual 'sudo cp' commands
do_install() {
    # Create necessary directories in the target image
    install -d ${D}${libdir}
    install -d ${D}${bindir}
    install -d ${D}${systemd_system_unitdir}

    # Install the shared library to /usr/lib
    install -m 0755 ${S}/libmfrc522.so ${D}${libdir}/

    # Install the two binaries to /usr/bin
    install -m 0755 ${S}/admin_tool ${D}${bindir}/
    install -m 0755 ${S}/rfid_daemon ${D}${bindir}/

    # Install Python components 
    install -m 0755 ${S}/gateKeeper.py ${D}${bindir}/
    install -m 0755 ${S}/createDatabase.py ${D}${bindir}/

    # Install the systemd service files to /lib/systemd/system/
    install -m 0644 ${S}/rfid_setup.service ${D}${systemd_system_unitdir}/
    install -m 0644 ${S}/rfid_hw.service ${D}${systemd_system_unitdir}/
    install -m 0644 ${S}/rfid_logic.service ${D}${systemd_system_unitdir}
}

# 7. Packaging: Tell Yocto to include the .so file in the main package
FILES:${PN} += "${libdir}/libmfrc522.so"


# Tell Yocto NOT to move the .so file into the -dev package.
# We want it in the main package (${PN}) so it's on the SD card.
FILES_SOLIBSDEV = ""
INSANE_SKIP:${PN} += "dev-so"

# Tell Yocto that this package provides its own shared library requirement.
PRIVATE_LIBS = "libmfrc522.so"

