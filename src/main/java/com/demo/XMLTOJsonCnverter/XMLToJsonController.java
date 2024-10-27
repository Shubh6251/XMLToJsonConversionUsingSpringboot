package com.demo.XMLTOJsonCnverter;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/convert")
public class XMLToJsonController {

    @PostMapping(value = "/xml-to-json", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> convertXmlToJson(@RequestBody String xmlContent) {
        try {
            // Convert XML to JSON
            JSONObject jsonObject = XMLToJsonUtility.convertXmlToJsonWithTotalScore(xmlContent);
            // Return JSON as String
            return ResponseEntity.ok(jsonObject.toString(4)); // Pretty print JSON with indentation
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"error\":\"Conversion failed\"}");
        }
    }
}

