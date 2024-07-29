package model;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonListParser {
	public static List<String> parseTopToJson(List<LeaderBoard> top) {
		List<String> jsonTop = new ArrayList<>();
		StringWriter jsonPlayer = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();
		for(LeaderBoard player : top) {
			try {
				mapper.writeValue(jsonPlayer, player);
			}  catch (IOException e) {
				e.printStackTrace();
			}
			jsonTop.add(jsonPlayer.toString());
			jsonPlayer = new StringWriter();
		}
		return jsonTop;
	}
	
	public static List<LeaderBoard> parseJsonToTop(List<String> jsonTop) {
		List<LeaderBoard> top = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		for(String jsonPlayer : jsonTop) {
			try {
				LeaderBoard player = mapper.readValue(jsonPlayer, LeaderBoard.class);
				top.add(player);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return top;
	}
}
