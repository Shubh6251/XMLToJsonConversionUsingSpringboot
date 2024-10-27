package com.demo.XMLTOJsonCnverter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLToJsonUtility {
    private static final Logger logger = LoggerFactory.getLogger(XMLToJsonUtility.class);

    /**
     * Converts XML to JSON and adds TotalMatchScore.
     *
     * @param xmlContent The XML content as a string.
     * @return JSON object with TotalMatchScore field added.
     */
    public static JSONObject convertXmlToJsonWithTotalScore(String xmlContent) {
        try {
            // Convert XML to JSON
            JSONObject jsonObject = XML.toJSONObject(xmlContent);
            logger.info("XML converted to JSON successfully.");

            // Calculate the TotalMatchScore
            int totalScore = calculateTotalScore(jsonObject);
            logger.info("TotalMatchScore calculated: {}", totalScore);

            // Add TotalMatchScore to JSON
            JSONObject matchSummary = new JSONObject();
            matchSummary.put("TotalMatchScore", totalScore);
            jsonObject.put("MatchSummary", matchSummary);

            return jsonObject;

        } catch (Exception e) {
            logger.error("Error converting XML to JSON: {}", e.getMessage());
            throw new RuntimeException("Failed to convert XML to JSON with total score", e);
        }
    }

    /**
     * Calculates the sum of scores in the JSON object under MatchDetails.Match.Score.
     *
     * @param jsonObject The JSON object.
     * @return Sum of scores.
     */
    private static int calculateTotalScore(JSONObject jsonObject) {
        int totalScore = 0;

        try {
            // Navigate to MatchDetails.Match
            JSONObject resultBlock = jsonObject.optJSONObject("Response")
                    .optJSONObject("ResultBlock")
                    .optJSONObject("MatchDetails");

            if (resultBlock != null) {
                Object matchObject = resultBlock.get("Match");

                if (matchObject instanceof JSONArray) {
                    // Multiple Match entries
                    for (int i = 0; i < ((JSONArray) matchObject).length(); i++) {
                        JSONObject match = ((JSONArray) matchObject).getJSONObject(i);
                        totalScore += match.optInt("Score", 0);
                    }
                } else if (matchObject instanceof JSONObject) {
                    // Single Match entry
                    totalScore += ((JSONObject) matchObject).optInt("Score", 0);
                }
            }
        } catch (Exception e) {
            logger.error("Error calculating total score: {}", e.getMessage());
            throw new RuntimeException("Failed to calculate total score", e);
        }

        return totalScore;
    }
}

