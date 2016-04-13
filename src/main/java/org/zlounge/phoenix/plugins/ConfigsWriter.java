package org.zlounge.phoenix.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigsWriter {
    /**
     * Write the config HashMap to Yaml file
     * 
     * @param profileConfig
     *            : HashMap<String, String> the profile config
     * @param fileName
     *            : String output file name
     * @throws IOException
     */
    private void write(HashMap<String, String> profileConfig, String fileName) throws IOException {
	String dataJson = new ObjectMapper().writeValueAsString(profileConfig);

	YAMLFactory yamlFactory = new YAMLFactory();
	ObjectMapper mapper = new ObjectMapper(yamlFactory);
	ObjectNode root = (ObjectNode) mapper.readTree(dataJson);
	FileOutputStream outputStream = new FileOutputStream(fileName, true);

	yamlFactory.createGenerator(outputStream).writeObject(root);

	// add new line to append more profiles
	outputStream.write(System.getProperty("line.separator").getBytes());
	outputStream.flush();
    }

    /**
     * Write all configs to YAML file
     * 
     * @param configs
     *            : HashMap< String, HashMap< String, String >> profiled configs
     *            to write
     * @param fileName
     *            : String
     * @throws IOException
     */
    public void writeToYaml(HashMap<String, HashMap<String, String>> configs, String fileName) throws IOException {
	// make sure to clear the file first
	FileOutputStream outputStream = new FileOutputStream(fileName);
	outputStream.write("".getBytes());
	outputStream.flush();

	for (Map.Entry<String, HashMap<String, String>> entry : configs.entrySet()) {
	    write(entry.getValue(), fileName);
	}

	outputStream.close();
    }

}
