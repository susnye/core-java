/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.core.systemregistry;

import eu.arrowhead.common.DatabaseManager;
import eu.arrowhead.common.database.ArrowheadDevice;
import eu.arrowhead.common.database.ArrowheadSystem;
import eu.arrowhead.common.database.SystemRegistryEntry;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.exception.DataNotFoundException;
import eu.arrowhead.common.misc.registry_interfaces.RegistryService;
import java.util.HashMap;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response.Status;

public class SystemRegistryService implements RegistryService<SystemRegistryEntry> {

	private final DatabaseManager databaseManager;

  public SystemRegistryService() {
		databaseManager = DatabaseManager.getInstance();
	}

	public SystemRegistryEntry lookup(final long id) throws EntityNotFoundException, ArrowheadException {
		final SystemRegistryEntry returnValue;

		try {
			Optional<SystemRegistryEntry> optional = databaseManager.get(SystemRegistryEntry.class, id);
			returnValue = optional.orElseThrow(() -> {
				return new DataNotFoundException("The requested entity does not exist", Status.NOT_FOUND.getStatusCode());
			});
		} catch (final ArrowheadException e) {
			throw e;
		} catch (Exception e) {
			throw new ArrowheadException(e.getMessage(), Status.NOT_FOUND.getStatusCode(), e);
		}

		return returnValue;
	}

	public SystemRegistryEntry publish(final SystemRegistryEntry entity) throws ArrowheadException {
		final SystemRegistryEntry returnValue;

		try {
			//WARNING: how should the consumer know about arrowhead system id, to be provided as providedSystem in
			// SystemRegistryEntity?? Without id provided system cannot be resolved!
			entity.setProvidedSystem(resolve(entity.getProvidedSystem()));
			entity.setProvider(resolve(entity.getProvider()));
			returnValue = databaseManager.save(entity);
		} catch (final ArrowheadException e) {
			throw e;
		} catch (Exception e) {
			throw new ArrowheadException(e.getMessage(), Status.NOT_FOUND.getStatusCode(), e);
		}

		return returnValue;
	}

	public SystemRegistryEntry unpublish(final SystemRegistryEntry entity) throws EntityNotFoundException, ArrowheadException {
		final SystemRegistryEntry returnValue;

		try {
			databaseManager.delete(entity);
			returnValue = entity;
		} catch (final ArrowheadException e) {
			throw e;
		} catch (Exception e) {
			throw new ArrowheadException(e.getMessage(), Status.NOT_FOUND.getStatusCode(), e);
		}
		return returnValue;
	}

	protected ArrowheadSystem resolve(final ArrowheadSystem providedSystem) {
		final ArrowheadSystem returnValue;


		if (providedSystem.getId() != null) {
			Optional<ArrowheadSystem> optional = databaseManager.get(ArrowheadSystem.class, providedSystem.getId());
			returnValue = optional.orElseThrow(() -> new ArrowheadException("ProvidedSystem does not exist", Status.BAD_REQUEST.getStatusCode()));
		} else {

			//TODO: system name of SystemRegistry entry should be unique? Then this code below is not necessary...
			if(providedSystem.getSystemName() != null){
				System.out.println("System registry entry, provider system name: "+providedSystem.getSystemName());

				HashMap<String, Object> restrictionMap = new HashMap<>();
				restrictionMap.put("systemName", providedSystem.getSystemName());
				final ArrowheadSystem ahSys = databaseManager.get(ArrowheadSystem.class, restrictionMap);

				if(ahSys == null){
					System.out.println("System registry entry, provider system not found in arrowhead system store, "
										   + "saving as new arrowhead system");
					returnValue = databaseManager.save(providedSystem);
				}
				else{
					System.out.println("System registry entry, provider system found in arrowhead system store");
					returnValue = ahSys;
				}

			}
			else{
				System.out.println("System registry entry, provider system not found in arrowhead system store, "
									   + "saving as new arrowhead system");

				returnValue = databaseManager.save(providedSystem);
			}


//			returnValue = databaseManager.save(providedSystem);
		}

		return returnValue;
	}

	protected ArrowheadDevice resolve(final ArrowheadDevice provider) {
		final ArrowheadDevice returnValue;

		if (provider.getId() != null) {
			Optional<ArrowheadDevice> optional = databaseManager.get(ArrowheadDevice.class, provider.getId());
			returnValue = optional.orElseThrow(() -> new ArrowheadException("Provider does not exist", Status.BAD_REQUEST.getStatusCode()));
		} else {
			//TODO: device name of ArrowheadDevice should be unique? Then this code below is not necessary...
			if(provider.getDeviceName() != null){
				System.out.println("Device registry entry, provider device name: "+provider.getDeviceName());
				HashMap<String, Object> restrictionMap = new HashMap<>();

				restrictionMap.put("deviceName", provider.getDeviceName());
				final ArrowheadDevice ahDev = databaseManager.get(ArrowheadDevice.class, restrictionMap);

				if(ahDev == null){
					System.out.println("Device registry entry, provider device not found in arrowhead device store, "
										   + "saving as new arrowhead device");
					returnValue = databaseManager.save(provider);
				}
				else{
					System.out.println("Device registry entry, provider device found in arrowhead device store");
					returnValue = ahDev;
				}
			}
			else{
				System.out.println("Device registry entry, provider device not found in arrowhead device store, "
									   + "saving as new arrowhead device");

				returnValue = databaseManager.save(provider);
			}
		}

		return returnValue;
	}
}
