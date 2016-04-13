package org.zlounge.phoenix.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which loads config from config server and merge them with local configs
 *
 * @goal load
 * 
 * @phase process-sources
 */
public class ConfigsLoaderMojo extends AbstractMojo {
    /**
     * @parameter expression="${outputFile}"
     *            default-value="src/main/resources/application.yml"
     */
    private String outputFile;

    /**
     * @parameter expression="${configServerUrl}"
     *            default-value="http://127.0.0.1:8888/config"
     */
    private String configServerUrl;

    /**
     * @parameter expression="${clientName}"
     *            default-value="mavenPluginConfigLoader"
     */
    private String clientName;

    /**
     * @parameter expression="${profile}" default-value="default"
     */
    private String profile;

    /**
     * @parameter expression="${user}" default-value="user"
     */
    private String user;

    /**
     * @parameter expression="${password}" default-value="pass"
     */
    private String password;

    /**
     * @parameter expression="${localConfigFile}"
     *            default-value="src/main/resources/local.yml"
     */
    private String localConfigFile;

    /**
     * @parameter expression="${overrideLocal}" default-value=false
     */
    private boolean overrideLocal;

    public void execute() throws MojoExecutionException {
	getLog().info("Start Loading from : " + configServerUrl);

	ConfigsClient client = new ConfigsClient();
	ConfigsParser parser = new ConfigsParser();
	ConfigsComposer composer = new ConfigsComposer();
	ConfigsWriter configsWriter = new ConfigsWriter();

	try {
	    String response = client.getConfigs(configServerUrl, clientName, profile, user, password);

	    getLog().info("Got response from configs server");

	    ArrayList<HashMap<String, String>> remoteConfigs = parser.getConfigsFromJson(response);
	    getLog().info("Parsed server configs");

	    ArrayList<HashMap<String, String>> localConfigs = parser.getConfigsFromYAML(localConfigFile);
	    getLog().info("Parsed local configs");

	    if (overrideLocal) {
		configsWriter.writeToYaml(composer.mergeConfigs(remoteConfigs, localConfigs), outputFile);
		getLog().info("Wrote merged configs ( local not overrided ) ");
	    } else {
		configsWriter.writeToYaml(composer.mergeConfigs(localConfigs, remoteConfigs), outputFile);
		getLog().info("Wrote merged configs ( local overrided ) ");
	    }

	} catch (Exception e) {
	    getLog().error("Can't load configs " + e.getMessage());
	    throw new MojoExecutionException(e.getMessage());
	}
    }
}
