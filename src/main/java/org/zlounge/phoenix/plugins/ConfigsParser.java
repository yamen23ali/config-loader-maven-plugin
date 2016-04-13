package org.zlounge.phoenix.plugins;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class ConfigsParser {

    /**
     * Extract configs related to profiles out of the JSON returned from the
     * config server
     * 
     * @param json
     *            : String JSON returned from the config server
     * @return ArrayList<HashMap<String,String>> : containing all configs for
     *         profiles
     * @throws JsonProcessingException
     * @throws IOException
     */
    public ArrayList<HashMap<String, String>> getConfigsFromJson(String json)
	    throws JsonProcessingException, IOException {
	
	Map<String, Object> map = new ObjectMapper().readValue(json, new TypeReference<HashMap<String, Object>>() {
	});

	ArrayList<HashMap<String, Object>> content = (ArrayList<HashMap<String, Object>>) map.get("propertySources");

	ArrayList<HashMap<String, String>> configs = new ArrayList<HashMap<String, String>>();

	for (HashMap<String, Object> node : content) {
	    configs.add((HashMap<String, String>) node.get("source"));
	}

	return configs;
    }

    /**
     * Read Configs from YAML file and parse them as ArrayList of one level
     * HasMaps
     * 
     * @param fileName
     *            : String
     * @return ArrayList< HashMap< String, String > >: All YAML configs
     * @throws IOException
     */
    public ArrayList<HashMap<String, String>> getConfigsFromYAML(String fileName) throws IOException {
	Yaml yaml = new Yaml();
	ArrayList<HashMap<String, String>> configs = new ArrayList<HashMap<String, String>>();

	try {

	    Iterable<Object> iterable = yaml.loadAll(new FileReader(new File(fileName)));

	    for (Object config : iterable) {
		HashMap<String, String> flatConfigs = new HashMap<String, String>();
		formatConfigs(flatConfigs, (HashMap<Object, Object>) config, "");

		configs.add(flatConfigs);
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}

	return configs;
    }

    /**
     * Format the configs in recursive way, make them one level HashMap ( i.e :
     * not nested )
     * 
     * @param flatConfgis
     *            : HashMap< String, String > configs after flattened
     * @param configs
     *            : HashMap< Object, Object > nested configs Hash map
     * @param rootKey
     *            : String the parent key of the current map entry
     */
    private void formatConfigs(HashMap<String, String> flatConfgis, HashMap<Object, Object> configs, String rootKey) {
	for (Map.Entry<Object, Object> entry : configs.entrySet()) {
	    String key = formatKey(entry.getKey(), rootKey);
	    Object value = entry.getValue();

	    if (value instanceof String || value instanceof Number) {
		flatConfgis.put(key, value.toString());
	    } else if (value instanceof Map) {
		formatConfigs(flatConfgis, (HashMap<Object, Object>) value, key);
	    } else if (value instanceof ArrayList) {
		formatConfigs(flatConfgis, (ArrayList) value, key);
	    } else {
		System.out.println(value.getClass());
	    }
	}
    }

    /**
     * Format the configs in recursive way, make them one level HashMap ( i.e :
     * not nested )
     * 
     * @param flatConfgis
     *            : HashMap< String, String > configs after flattened
     * @param configs
     *            : ArrayList of objects ( values or more nested configs )
     * @param rootKey
     *            : String the parent key of the current map entry
     */
    private void formatConfigs(HashMap<String, String> flatConfgis, ArrayList configs, String rootKey) {
	int index = 0;
	for (Object entry : configs) {

	    String key = formatKey(index, rootKey);

	    if (entry instanceof String || entry instanceof Number) {

		flatConfgis.put(key, entry.toString());
	    } else if (entry instanceof Map) {
		formatConfigs(flatConfgis, (HashMap<Object, Object>) entry, key);
	    } else if (entry instanceof ArrayList) {
		formatConfigs(flatConfgis, (ArrayList) entry, key);
	    }

	    index++;
	}
    }

    /**
     * Get the right YAML format for the key
     * 
     * @param key
     *            : String the current map key
     * @param rootKey
     *            : String the parent key
     * @return String formated key
     */
    private String formatKey(Object key, String rootKey) {
	if (rootKey.isEmpty()) {
	    return key.toString();
	}

	if (key instanceof Number) {
	    return String.format("%s[%s]", rootKey, key);
	} else {
	    return String.format("%s.%s", rootKey, key);
	}
    }

}
