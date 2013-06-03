package br.com.leitortweets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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

		new BuscaTweetsTask().execute(termoDeBusca);
	}

	private void popularListView(ArrayList<Tweet> lista){
		TweetAdapter  tweetAdapter = new TweetAdapter(this, R.layout.lista_tweets_adapter, lista);
		listView.setAdapter(tweetAdapter);
	}


	private String formatarData(String data){
		String strdata = null;
		try {
			strdata = new SimpleDateFormat("EEE, dd 'de' MMMM 'de' yyyy, 'às' HH:mm:ss")
			.format(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US)
			.parse(data));
		} catch (ParseException e) {
			Log.e("parser data", Log.getStackTraceString(e));
		}
		return strdata;
	}


	private class BuscaTweetsTask extends AsyncTask<String, Void, ArrayList<Tweet>>{
		
		private ProgressDialog progressDialog;
		private static final String URL_TWITTER = "http://search.twitter.com/search.json?q=from:";

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			progressDialog = new ProgressDialog(LeitorTweetsActivity.this);
			progressDialog.setMessage("Carregando tweets. Aguarde...");
			progressDialog.show();
		}

		@Override
		protected ArrayList<Tweet> doInBackground(String... param) {

			String termoDeBusca = param[0];
			ArrayList<Tweet> tweets = new ArrayList<Tweet>();
			HttpURLConnection conexao = null;
			BufferedReader br = null;

			try {
				URL url = new URL(URL_TWITTER + termoDeBusca);
				conexao = (HttpURLConnection) url.openConnection();

				Log.i("conexao - TwitterAPI", "conectou!");

				conexao.setRequestProperty("Content-Type", "application/json; charset=utf-8");
				conexao.setConnectTimeout(30 * 1000);
				conexao.connect();

				br = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

				String linha;
				JSONObject jsonObject;
				JSONArray jsonArray;
				while ((linha = br.readLine()) != null){            
					//Log.i("json", entrada);
					jsonObject = new JSONObject(linha);
					jsonArray = jsonObject.getJSONArray("results");

					for (int i = 0; i < jsonArray.length(); i++) {
						jsonObject = (JSONObject) jsonArray.get(i);

						Tweet tweet = new Tweet();
						
						tweet.setNome(jsonObject.getString("from_user_name"));
						tweet.setUsuario(jsonObject.getString("from_user"));
						tweet.setImagem_url(jsonObject.getString("profile_image_url"));
						tweet.setMensagem(jsonObject.getString("text"));
						tweet.setData(formatarData(jsonObject.getString("created_at").substring(0, 24)));

						tweets.add(i, tweet);
					}
				}

			} catch (MalformedURLException e) {
				Log.e("MalformedURLException: ", Log.getStackTraceString(e));        

			} catch (IOException e) {
				Log.e("IOException: ", Log.getStackTraceString(e));        

			} catch (JSONException e) {
				Log.e("JSONException: ", Log.getStackTraceString(e));   

			}finally {
				if(br != null){
					try{
						br.close();
						Log.i("BufferedReader", "fechou!");
					} catch (IOException e) {
						Log.e("br.close(): ", Log.getStackTraceString(e));        
					} 
				}

				conexao.disconnect();
				Log.i("conexao - twitterAPI", "desconectou!");
			}

			return tweets;
		}

		@Override
		protected void onPostExecute(ArrayList<Tweet> tweets){
			progressDialog.dismiss();

			if (tweets.isEmpty()) {
				Toast.makeText(getApplicationContext(), "Erro ao carregar os tweets! ",
						Toast.LENGTH_SHORT).show();
			} else {
				popularListView(tweets);
				Toast.makeText(getApplicationContext(), "Tweets carregados!",
						Toast.LENGTH_SHORT).show();
			}
		}


	}


}
