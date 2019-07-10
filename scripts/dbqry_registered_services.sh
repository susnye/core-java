#!/bin/bash
mysql -u root -p -e "USE arrowhead;select sr.id, sr.end_of_validity, sr.metadata, sr.udp, sr.version, sr.service_uri, sr.arrowhead_service_id, ahserv.service_definition, sr.provider_system_id, ahsys.system_name, ahsys.address, ahsys.port from arrowhead_system ahsys inner join (service_registry sr inner join arrowhead_service ahserv on sr.arrowhead_service_id=ahserv.id) on ahsys.id=sr.provider_system_id;"