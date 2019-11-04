package br.com.investibot;

import br.com.investibot.apis.TelegramBotApi;

public class App 
{
	private static String telegramBotToken = "TELEGRAM_BOT_TOKEN_HERE";
	private static String hgFinanceToken = "HGFINANCE_TOKEN_HERE";
    public static void main( String[] args )
    {
    	
    	TelegramBotApi botApi = new TelegramBotApi(telegramBotToken, hgFinanceToken);
    	botApi.IniciarBot();
    }
}
