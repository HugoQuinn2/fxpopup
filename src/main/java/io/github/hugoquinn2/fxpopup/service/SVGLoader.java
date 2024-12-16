package io.github.hugoquinn2.fxpopup.service;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import org.w3c.dom.Element;

public class SVGLoader {

    public static String loadSVGFromResources(String resourcePath) {
        StringBuilder svgContent = new StringBuilder();

        try (InputStream inputStream = SVGLoader.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                System.err.println("Archivo no encontrado: " + resourcePath);
                return "";
            }

            // Pars svg file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            // Extract elements <path>
            NodeList pathNodes = document.getElementsByTagName("path");
            for (int i = 0; i < pathNodes.getLength(); i++) {
                Element pathElement = (Element) pathNodes.item(i);
                String pathData = pathElement.getAttribute("d");
                if (!pathData.isEmpty()) {
                    svgContent.append(pathData).append(" ");
                }
            }

            // Extract elements <circle>
            NodeList circleNodes = document.getElementsByTagName("circle");
            for (int i = 0; i < circleNodes.getLength(); i++) {
                Element circleElement = (Element) circleNodes.item(i);
                String cx = circleElement.getAttribute("cx");
                String cy = circleElement.getAttribute("cy");
                String r = circleElement.getAttribute("r");

                if (!cx.isEmpty() && !cy.isEmpty() && !r.isEmpty()) {
                    double radius = Double.parseDouble(r);
                    String circlePath = String.format("M%s,%s m-%s,0 a%s,%s 0 1,0 %s,0 a%s,%s 0 1,0 -%s,0",
                            cx, cy, r, r, r, 2 * radius, r, r, 2 * radius);
                    svgContent.append(circlePath).append(" ");
                }
            }

            // Extract elements <rect>
            NodeList rectNodes = document.getElementsByTagName("rect");
            for (int i = 0; i < rectNodes.getLength(); i++) {
                Element rectElement = (Element) rectNodes.item(i);
                String x = rectElement.getAttribute("x");
                String y = rectElement.getAttribute("y");
                String width = rectElement.getAttribute("width");
                String height = rectElement.getAttribute("height");

                if (!x.isEmpty() && !y.isEmpty() && !width.isEmpty() && !height.isEmpty()) {
                    String rectPath = String.format("M%s,%s h%s v%s h-%s z", x, y, width, height, width);
                    svgContent.append(rectPath).append(" ");
                }
            }

            // Extract elements <ellipse>
            NodeList ellipseNodes = document.getElementsByTagName("ellipse");
            for (int i = 0; i < ellipseNodes.getLength(); i++) {
                Element ellipseElement = (Element) ellipseNodes.item(i);
                String cx = ellipseElement.getAttribute("cx");
                String cy = ellipseElement.getAttribute("cy");
                String rx = ellipseElement.getAttribute("rx");
                String ry = ellipseElement.getAttribute("ry");

                if (!cx.isEmpty() && !cy.isEmpty() && !rx.isEmpty() && !ry.isEmpty()) {
                    String ellipsePath = String.format("M%s,%s m-%s,0 a%s,%s 0 1,0 %s,0 a%s,%s 0 1,0 -%s,0",
                            cx, cy, rx, rx, ry, 2 * Double.parseDouble(rx), rx, ry, 2 * Double.parseDouble(rx));
                    svgContent.append(ellipsePath).append(" ");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return svgContent.toString().trim();
    }
}

