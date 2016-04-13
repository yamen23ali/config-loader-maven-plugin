package org.zlounge.phoenix.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfigsComposer {

    /**
     * Label configs with their related profiles
     * 
     * @param configs
     *            : ArrayList<HashMap<String, String>> List of configs
     * @return HashMap<String, HashMap<String, String>> : Key as profile name ,
     *         value as related configs
     */
    private HashMap<String, HashMap<String, String>> getProfiledConfigs(ArrayList<HashMap<String, String>> configs) {
	HashMap<String, HashMap<String, String>> profiledConfigs = new HashMap<String, HashMap<String, String>>();

	for (HashMap<String, String> config : configs) {
	    String profileName = "default";
	    if (config.containsKey("spring.profiles")) {
		profileName = config.get("spring.profiles");
	    }

	    profiledConfigs.put(profileName, config);
	}

	return profiledConfigs;
    }

    /**
     * Merge 2 lists of configs into one HashMap with profiles as keys
     * 
     * @param mainConfigs
     *            ArrayList< HashMap< String, String > > The main configs that
     *            will override the others
     * @param secondaryConfigs
     *            ArrayList< HashMap< String, String > > The secondary configs
     * @return HashMap< String, HashMap< String, String > > final merged configs
     *         with profiles as keys
     */
    public HashMap<String, HashMap<String, String>> mergeConfigs(ArrayList<HashMap<String, String>> mainConfigs,
	    ArrayList<HashMap<String, String>> secondaryConfigs) {
	HashMap<String, HashMap<String, String>> profiledMainConfigs = getProfiledConfigs(mainConfigs);
	HashMap<String, HashMap<String, String>> profiledSecondaryConfigs = getProfiledConfigs(secondaryConfigs);
	HashMap<String, HashMap<String, String>> mergedConfig = new HashMap<String, HashMap<String, String>>();

	// fill in the secondary first
	for (Map.Entry<String, HashMap<String, String>> entry : profiledSecondaryConfigs.entrySet()) {
	    mergedConfig.put(entry.getKey(), entry.getValue());
	}

	// fill in the main
	for (Map.Entry<String, HashMap<String, String>> entry : profiledMainConfigs.entrySet()) {
	    // fill the new entries directly
	    String key = entry.getKey();
	    if (!mergedConfig.containsKey(key)) {
		mergedConfig.put(entry.getKey(), entry.getValue());
	    } else {
		// overwrite existing secondary configs
		for (Map.Entry<String, String> configsEntry : entry.getValue().entrySet()) {
		    mergedConfig.get(key).put(configsEntry.getKey(), configsEntry.getValue());
		}
	    }
	}

	return mergedConfig;
    }

}
