package io.github.hugoquinn2.fxpopup.service;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import java.util.stream.Collectors;

public class SVGLoader {
    public static String loadSVGFromResources(String resourcePath) {
        StringBuilder svgContent = new StringBuilder();

        try (InputStream inputStream = SVGLoader.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                System.err.println("Archivo no encontrado: " + resourcePath);
                return "";
            }

            // Parsear el archivo SVG
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            // Obtener todos los elementos <path>
            NodeList pathNodes = document.getElementsByTagName("path");

            // Construir el contenido de los paths
            for (int i = 0; i < pathNodes.getLength(); i++) {
                Element pathElement = (Element) pathNodes.item(i);
                String pathData = pathElement.getAttribute("d");

                // Agregar el contenido de cada path a la cadena
                if (svgContent.length() > 0) {
                    svgContent.append(" ");
                }
                svgContent.append(pathData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return svgContent.toString();
    }
}
