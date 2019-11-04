package br.com.investibot.apis.HGFinance;

public enum Format {
	Json,
	Debug;
	
	public String getFormat() {
		return String.valueOf(this);
	}
}
