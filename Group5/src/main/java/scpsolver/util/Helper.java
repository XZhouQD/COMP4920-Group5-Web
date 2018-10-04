package scpsolver.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Helper {

	
	public  void extractRessource() {
		try 

	      {

	         BufferedReader objBin = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("bla.so")));

	         if (objBin != null) 

	         {

	            String strLine = null;

	            while ((strLine = objBin.readLine()) != null)

	            {

	               System.out.println(strLine);

	            }

	            objBin.close();

	         } 

	         else 

	         {

	            System.out.println("Error: Unable to retrieve InputStream");

	         }

	      }

	      catch (Exception e) 

	      {

	         System.out.println(e);

	      }


		
	}
}
