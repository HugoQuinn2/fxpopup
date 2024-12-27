package io.github.hugoquinn2.fxpopup.service;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import org.w3c.dom.Element;

public class SVGLoader {

    /**
     * Loads an SVG file from the resources and converts it into a string containing the path data.
     *
     * @param resourcePath the path to the SVG file within the resources directory
     * @return a string containing the SVG path data, or an empty string if no valid paths are found
     */
    public static String loadSVGFromResources(String resourcePath) {
        StringBuilder svgContent = new StringBuilder();

        try (InputStream inputStream = SVGLoader.class.getResourceAsStream(resourcePath)) {

            // If input stream is null, the SVG file was not found
            if (inputStream == null)
                throw new NullPointerException("SVG file not found: " + resourcePath);

            // Parse the SVG file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            // Extract <path> elements and append their 'd' attributes to svgContent
            NodeList pathNodes = document.getElementsByTagName("path");
            for (int i = 0; i < pathNodes.getLength(); i++) {
                Element pathElement = (Element) pathNodes.item(i);
                String pathData = pathElement.getAttribute("d");
                if (!pathData.isEmpty()) {
                    svgContent.append(pathData).append(" ");
                }
            }

            // Extract <circle> elements and convert them to path data
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

            // Extract <rect> elements and convert them to path data
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

            // Extract <ellipse> elements and convert them to path data
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

        // Return the final SVG path data as a string
        return svgContent.toString().trim();
    }
}
