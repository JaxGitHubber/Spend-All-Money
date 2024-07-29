package model;

public class PriceParser {
	public static String parseToPrice(Long number) {
		String price = Long.toString(number);
		String priceWithpoints = "";
	    int step = 1;
	      for(int i = price.length()-1; i >= 0; i--) {
	        if(step == 3 && i-1 != -1) {
	            step = 1;
	            priceWithpoints = price.charAt(i) + priceWithpoints;
	            priceWithpoints = "." + priceWithpoints;
	        } else {
	        	priceWithpoints = price.charAt(i) + priceWithpoints;
	            step++;
	        }
	      }
	      return priceWithpoints + "$";
	}
	
	public static Long parseToLong(String price) {
		return Long.parseLong(price.replace("$", "").replace(".", ""));
	}
}
