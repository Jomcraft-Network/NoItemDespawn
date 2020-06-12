/* 
 *      NoItemDespawn - 1.15.2 <> Idea and codedesign by PT400C - Mod's main class
 *      © Jomcraft Network 2020
 */
package de.pt400c.noitemdespawn;

import java.io.DataOutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.config.ModConfig;
import de.pt400c.noitemdespawn.config.NIDConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(value = NoItemDespawn.MODID)
public class NoItemDespawn {
 
	public static final String MODID = "noitemdespawn";
	public static final Logger log = LogManager.getLogger(NoItemDespawn.MODID);
	public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";
	public static final String VERSION = "2.0.2";
	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public static NoItemDespawn instance;

	public NoItemDespawn() throws Exception {
		instance = this;
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NIDConfig.COMMON_SPEC);
		MinecraftForge.EVENT_BUS.register(new EventHooksFML());
		
		(new Thread() {

			@Override
			public void run() {
				try {
					sendCount();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		}).start();
	}
	
	public static NoItemDespawn getInstance() {
		return instance;
	}

	public static void sendCount() throws Exception {
		String url = "https://apiv1.jomcraft.net/count";
		String jsonString = "{\"id\":\"NoItemDespawn\", \"code\":" + RandomStringUtils.random(32, true, true) + "}"; 
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(jsonString);

		wr.flush();
		wr.close();
		con.getResponseCode();
		//if (resCode < HttpsURLConnection.HTTP_BAD_REQUEST) {
		//	result = con.getInputStream();
		//} else {
		//	result = con.getErrorStream();
		//}
		/*
		BufferedReader in = new BufferedReader(new InputStreamReader(result));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		//String JSON = response.toString();
		in.close();*/
		con.disconnect();
		

	}
}

class RequestJSON { 
	   private String id; 
	   private String code; 
	   public RequestJSON(){} 
	   
	   public String getCode() { 
	      return this.code;
	   }
	   
	   public RequestJSON setID(String id) { 
	      this.id = id;
	      return this;
	   } 
	   
	   public String getID() { 
	      return this.id;
	   }
	   
	   public RequestJSON setCode(String code) { 
	      this.code = code;
	      return this;
	   }
	}