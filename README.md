# Arrowhead Framework G4.0

## Installing Arrowhead on a Ubuntu Server using Debian Packages

### 1. Install Ubuntu Server (18.04)

Do a normal installation of Ubuntu server, remember to update:

`sudo apt update && sudo apt dist-upgrade`

### 2. Install MySQL

Install:

`sudo apt install mysql-server`

Check if running:

`sudo netstat -tap | grep mysql`

### 3. Install Java

To install Oracle Java, add the repository first:

`sudo add-apt-repository ppa:linuxuprising/java`

`sudo apt update`

`sudo apt install oracle-java10-installer`

Check Java version:

`java -version`

### 4. Build Arrowhead Debian Packages

Do this on your local Arrowhead development machine.

`git clone https://github.com/arrowhead-f/core-java.git -b feature/debian`

Build:

`mvn package`

Copy to server:

```bash
scp common/target/arrowhead-common_4.0_all.deb \
    authorization/target/arrowhead-authorization_4.0_all.deb \
    serviceregistry_sql/target/arrowhead-serviceregistry-sql_4.0_all.deb \
    gateway/target/arrowhead-gateway_4.0_all.deb \
    eventhandler/target/arrowhead-eventhandler_4.0_all.deb \
    gatekeeper/target/arrowhead-gatekeeper_4.0_all.deb \
    orchestrator/target/arrowhead-orchestrator_4.0_all.deb \
    X.X.X.X:~/
```

### 5. Install Arrowhead Core Debian Packages

`sudo dpkg -i arrowhead-*.deb`

Currently they're not added to the default runlevel, so they will not restart on reboot unless added manually.

### 6. Hints

Log files (log4j) are available in: `/var/log/arrowhead/*`

Output from systems are available with: `journalctl -u arrowhead-*.service`

Restart services: `sudo systemctl restart arrowhead-\*.service`

Configuration and certificates are in: `/etc/arrowhead`

Generated passwords can be found in `/var/cache/debconf/passwords.dat`

Mysql database: `sudo mysql -u root`, to see the Arrowhead tables:

```SQL
use arrowhead;
show tables;
```

`apt purge` can be used to remove configuration files, database, log files, etc. Use `sudo apt purge arrowhead-\*` to
remove everything arrowhead related.

If you need to generate a new certificate for an application system, signed with the cloud certificate:
`sudo ahcert PATH SYSTEM_NAME`, e.g. `sudo ahcert ~/ SecureTemperatureSensor`.

For the provider and consumer example in the client skeletons, the script `sudo ah_quickstart_gen` can be used to
generate the necessary certificates and database entries. It will also output the certificate/keystore password. Note,
this script should only be used for test clouds on a clean installation.

For clouds installed in detached mode, a certificate for a second cloud can be generated with
`sudo ahcert_cloud ./ CLOUD_NAME`, e.g. `sudo ahcert_cloud ./ testcloud2`.
