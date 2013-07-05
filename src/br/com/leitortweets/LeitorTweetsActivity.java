/*
 * @author: Diogo Alves <diogo.alves.ti@gmail.com>
 */

package br.com.leitortweets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class LeitorTweetsActivity extends Activity {

	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leitor);
		final String termoDeBusca = getIntent().getStringExtra("TERMO_DE_BUSCA");
		listView = (ListView) findViewById(R.id.lista_tweets);

		// Fazendo as requisições
		new BuscaTweetsTask().execute(termoDeBusca);
	}

	/**
	 * Popula a listview com a lista de tweets retirados da api do twitter.
	 * @param tweets lista contendo os tweets.
	 */
	private void popularListView(ArrayList<Tweet> tweets){
		TweetAdapter  tweetAdapter = new TweetAdapter(this, R.layout.tweet_adapter, tweets);
		listView.setAdapter(tweetAdapter);
	}

	/**
	 * Classe que recupera os tweets numa thread separada da UI Thread (thread principal)
	 * herdando os métodos da classe AsyncTask. Faz uso da versão 1.1 da api do Twitter no modo
	 * application-only authentication (não necessita de autenticação do usuário).
	 * 
	 * @see 
	 * {@link android.os.AsyncTask}<br>
	 * <a href="https://dev.twitter.com/docs/auth/application-only-auth">
	 * Documentação da api v1.1 do Twitter - application-only authentication
	 * </a>
	 */
	private class BuscaTweetsTask extends AsyncTask<String, Void, ArrayList<Tweet>>{

		private ProgressDialog progressDialog;
		private static final String URL_BASE = "https://api.twitter.com";
		private static final String URL_BUSCA = URL_BASE + "/1.1/statuses/user_timeline.json?screen_name=";
		private static final String URL_AUTH = URL_BASE + "/oauth2/token";

		// Registre sua app no endereço https://dev.twitter.com/apps/new e obtenha as chaves de consumo.
		private static final String CONSUMER_KEY = "<Sua CONSUMER KEY aqui>";
		private static final String CONSUMER_SECRET = "<Sua CONSUMER SECRET aqui>";

		/**
		 * Requisita o token de acesso para utilizar a api, passando na requisição as chaves de consumo da api.
		 * @return um token de acesso à api em formato JSON ou null, caso tenha ocorrido um erro.
		 */
		private String autenticarApp(){

			HttpURLConnection conexao = null;
			OutputStream os = null;
			BufferedReader br = null;
			StringBuilder resposta = null;

			try {
				URL url = new URL(URL_AUTH);
				conexao = (HttpURLConnection) url.openConnection();
				conexao.setRequestMethod("POST");
				conexao.setDoOutput(true);
				conexao.setDoInput(true);

				// codificando as chaves de consumo da api com a codificação base64.
				String credenciaisAcesso = CONSUMER_KEY + ":" + CONSUMER_SECRET;
				String autorizacao = "Basic " + Base64.encodeToString(credenciaisAcesso.getBytes(), Base64.NO_WRAP);
				String parametro = "grant_type=client_credentials";

				// enviando as credenciais de acesso no cabeçalho da requisição
				conexao.addRequestProperty("Authorization", autorizacao);
				conexao.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				conexao.connect();
				
				// enviando o parametro granty_type no corpo da requisição
				os = conexao.getOutputStream();
				os.write(parametro.getBytes());
				os.flush();
				os.close();

				// recuperando a resposta do servidor (token de acesso  em JSON - obrigatório para utilizar a api)
				br = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
				String linha;
				resposta = new StringBuilder();

				while ((linha = br.readLine()) != null){            
					resposta.append(linha);	
				}

				Log.d("Código resposta POST", String.valueOf(conexao.getResponseCode()));
				Log.d("Resposta JSON - token de acesso", resposta.toString());

			} catch (Exception e) {
				Log.e("Erro POST", Log.getStackTraceString(e));
				
			}finally{
				if (conexao != null) {
					conexao.disconnect();
				}
			}
			return resposta.toString();
		}

		/**
		 * Exibe um ProgressDialog na UI thread antes do processamento doInBacground.
		 */
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			progressDialog = new ProgressDialog(LeitorTweetsActivity.this);
			progressDialog.setMessage("Carregando tweets. Aguarde...");
			progressDialog.show();
		}

		/**
		 * Busca os tweets mais recentes da timeline de um usuário. Método roda em segundo plano.
		 * 
		 * @param param  O termo a buscar no twitter - nesse caso, o usuário
		 * @return 
		 * <li>uma lista com os últimos tweets retirados da timeline do usuário.</li>
		 */
		@Override
		protected ArrayList<Tweet> doInBackground(String... param) {

			String termoDeBusca = param[0];
			ArrayList<Tweet> tweets = new ArrayList<Tweet>();
			HttpURLConnection conexao = null;
			BufferedReader br = null;

			try {
				URL url = new URL(URL_BUSCA + termoDeBusca);
				conexao = (HttpURLConnection) url.openConnection();
				conexao.setRequestMethod("GET");

				// utilizando o token de acesso (formato JSON)
				String jsonString = autenticarApp();
				JSONObject jsonAcesso = new JSONObject(jsonString);
				String tokenPortador = jsonAcesso.getString("token_type") + " " + 
						jsonAcesso.getString("access_token");

				conexao.setRequestProperty("Authorization", tokenPortador);
				conexao.setRequestProperty("Content-Type", "application/json");
				conexao.connect();

				// recuperando os tweets da api
				br = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

				String linha;
				StringBuilder resposta = new StringBuilder();

				while ((linha = br.readLine()) != null){            
					resposta.append(linha);	
				}
				
				Log.d("Código resposta GET", String.valueOf(conexao.getResponseCode()));
				Log.d("Resposta JSON", resposta.toString());
				
				JSONArray jsonArray = new JSONArray(resposta.toString());
				JSONObject jsonObject;

				for (int i = 0; i < jsonArray.length(); i++) {
					
					jsonObject = (JSONObject) jsonArray.get(i);
					Tweet tweet = new Tweet();

					tweet.setNome(jsonObject.getJSONObject("user").getString("name"));
					tweet.setUsuario(jsonObject.getJSONObject("user").getString("screen_name"));
					tweet.setUrlImagemPerfil(jsonObject.getJSONObject("user").getString("profile_image_url"));
					tweet.setMensagem(jsonObject.getString("text"));
					tweet.setData(jsonObject.getString("created_at"));

					tweets.add(i, tweet);
				}

			} catch (Exception e) {
				Log.e("Erro GET: ", Log.getStackTraceString(e));        

			}finally {
				if(conexao != null){
					conexao.disconnect();
				}
			}
			return tweets;
		}

		/**
		 * Atualiza a listview (na UI Thread) com os a lista de tweets recuperada pelo método 
		 * doInBackground. Mostra uma mensagem de erro caso a lista esteja vazia.
		 * 
		 * @param tweets
		 * a lista retornada pelo processamento doInBackground.
		 */
		@Override
		protected void onPostExecute(ArrayList<Tweet> tweets){
			progressDialog.dismiss();

			if (tweets.isEmpty()) {
				Toast.makeText(LeitorTweetsActivity.this, "Não foi possível recuperar os tweets! ",
						Toast.LENGTH_SHORT).show();
			} else {
				popularListView(tweets);
				Toast.makeText(LeitorTweetsActivity.this, "Tweets carregados!",
						Toast.LENGTH_SHORT).show();
			}
		}


	}


}
