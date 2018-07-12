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

When/if Debian packages get integrated, you should use <https://github.com/hegeduscs/arrowhead>,
but until then, get the source from my personal GitHub:

`git clone https://github.com/eudyptula/arrowhead.git -b debian --depth 1`

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

`apt purge` can be used to remove configuration files for a fresh installation,
but database and log files are currently kept, and should be removed manually.