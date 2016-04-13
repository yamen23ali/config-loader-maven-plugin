package org.zlounge.phoenix.plugins;

import java.util.Base64;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class ConfigsClient {
    WebTarget webTarget;

    Client client;

    /**
     * Get configs from spring cloud config server as JSON string
     * 
     * @param configServerUrl
     *            : String the config server url
     * @param clientName
     *            : String The client name calling the config server
     * @param profile
     *            : String the profile to get from the config server
     * @param name
     *            : String the user name in basic auth needed to access the
     *            config server
     * @param password
     *            : String the password in basic auth needed to access the
     *            config server
     * @return String the config returned from the config server as JSON string
     */
    public String getConfigs(String configServerUrl, String clientName, String profile, String name, String password) {

	String url = String.format("%s/%s/%s/", configServerUrl, clientName, profile);
	String authHeader = String.format("Basic %s", encode64(name, password));

	client = ClientBuilder.newClient();

	String response = client.target(url).request().header("Authorization", authHeader)
		.header("Accept", "application/json").get(String.class);

	return response;
    }

    /**
     * Get 64 base encoding
     * 
     * @param name
     * @param password
     * @return
     */
    private String encode64(String name, String password) {
	String inputString = String.format("%s:%s", name, password);
	Base64.Encoder encoder = Base64.getEncoder();
	byte[] encodedBytes = encoder.encode(inputString.getBytes());
	return new String(encodedBytes);
    }
}
