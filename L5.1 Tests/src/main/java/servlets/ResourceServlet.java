package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import resources.ResourceServer;
import resources.TestResource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

public class ResourceServlet extends HttpServlet {
    private final ResourceServer resourceServer;

    public ResourceServlet(ResourceServer resourceServer) {
        this.resourceServer = resourceServer;
    }

    public TestResource parseXMLFile(String filePath) {
        TestResource resource = new TestResource();
        try {
            File file = new File(filePath);
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            String name = doc.getElementsByTagName("name").item(0).getTextContent();
            int age = Integer.parseInt(doc.getElementsByTagName("age").item(0).getTextContent());

            resource = new TestResource(name, age);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resource;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");

        if (path != null) {
            // Парсим XML файл и сохраняем результат
            TestResource resource = parseXMLFile(path);
            resourceServer.setResource(resource);

            response.getWriter().println("Resource loaded");
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.getWriter().println("Invalid request");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
