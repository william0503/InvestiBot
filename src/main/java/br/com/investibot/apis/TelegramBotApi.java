package br.com.investibot.apis;

import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import br.com.investibot.apis.HGFinance.HGFinanceApi;

public class TelegramBotApi {
	
	private TelegramBot bot;
	private HGFinanceApi finApi;
	
	private GetUpdatesResponse updatesResponse;			
	private SendResponse sendResponse;			
	private BaseResponse baseResponse;
	
	public TelegramBotApi(String telegramBotToken, String hgFinanceToken) {
		//Criação do objeto bot com as informações de acesso
		bot = new TelegramBot(telegramBotToken);
		finApi = new HGFinanceApi(hgFinanceToken); 
	}
	
	public void IniciarBot() {
		
		//controle de off-set, isto é, a partir deste ID será lido as mensagens pendentes na fila
		int m=0;
		int iniciado = 0;
		//loop infinito pode ser alterado por algum timer de intervalo curto
		while (true){
			
			//executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(m));
			
			//lista de mensagens
			List<Update> updates = updatesResponse.updates();

			//análise de cada ação da mensagem
			for (Update update : updates) {
				String mensagemAtual = update.message().text();
				
				//atualização do off-set
				m = update.updateId()+1;
				
				System.out.println("Recebendo mensagem:"+ mensagemAtual);
								
				if(mensagemAtual.equalsIgnoreCase("/start")) {										
					Escrever(update, "Olá! Vou te ajudar a obter informações sobre indicadores financeiros!");
					Escrever(update, "Qual informação você deseja saber? (ex. cdi)");
					iniciado = 1;
				}
				else {					
					if(iniciado == 1) {
						String retorno = finApi.getData(mensagemAtual);
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),retorno));

						//verificação de mensagem enviada com sucesso
						System.out.println("Mensagem Enviada?" +sendResponse.isOk());

					}
					else {
						//envio da mensagem de resposta
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Não entendi..."));
						
						//verificação de mensagem enviada com sucesso
						System.out.println("Mensagem Enviada?" +sendResponse.isOk());
					}
					
				}
			}
		}
	}
	
	private void Escrever(Update update, String texto) {
		//envio de "Escrevendo" antes de enviar a resposta
		baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
		
		//verificação de ação de chat foi enviada com sucesso
		System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());
		
		//envio da mensagem de resposta
		sendResponse = bot.execute(new SendMessage(update.message().chat().id(),texto));
		
		//verificação de mensagem enviada com sucesso
		System.out.println("Mensagem Enviada?" +sendResponse.isOk());
	}
}
