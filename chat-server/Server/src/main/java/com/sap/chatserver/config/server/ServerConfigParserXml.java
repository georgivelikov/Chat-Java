package com.sap.chatserver.config.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sap.chatserver.exception.ServerConfigurationException;

public class ServerConfigParserXml implements ServerConfigParser {

	@Override
	public Map<String, String> getProperties(String location) throws ServerConfigurationException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(location);
			return generatePropertiesMap(document);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new ServerConfigurationException(MessageConfig.CONFIG_PARSE_EXC.getContent(), e);
		}
	}

	private Map<String, String> generatePropertiesMap(Document document) {
		Map<String, String> properties = new HashMap<>();
		Node root = document.getFirstChild();
		NodeList configurations = root.getChildNodes();
		for (int i = 0; i < configurations.getLength(); i++) {
			Node configProperty = configurations.item(i);
			if (configProperty.getNodeType() == Node.ELEMENT_NODE) {
				Element configElement = (Element) configProperty;
				String name = configElement.getTagName();
				String value = configElement.getTextContent();
				properties.put(name.toLowerCase(), value.toLowerCase());
			}
		}

		return properties;
	}

}
