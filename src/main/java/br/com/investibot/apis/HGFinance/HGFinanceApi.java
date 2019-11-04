package br.com.investibot.apis.HGFinance;

import java.io.IOException;

import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HGFinanceApi {
	private String hgFinanceToken;
	private final OkHttpClient httpClient = new OkHttpClient();
	private final String baseUrl = "https://api.hgbrasil.com/finance/";
	
	public HGFinanceApi(String token) {
		this.hgFinanceToken = token;
		
		
	}
	
	public String getData(String info) {
		try {
			String url = baseUrl + "taxes?key="+ hgFinanceToken;
			return sendGet(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Erro";
		}
		
		
	}
	
	private String sendGet(String url) throws Exception {
		
        Request request = new Request.Builder()
                .url(url).build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String retorno = response.body().string();
    		System.out.println(retorno);
    		    		
            return retorno;
        }

    }
}
