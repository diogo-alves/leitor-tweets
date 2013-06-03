package br.com.leitortweets;

import java.util.ArrayList;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class TweetAdapter  extends ArrayAdapter<Tweet> {


	private ArrayList<Tweet> tweets;

	public TweetAdapter(Context context, int textViewResourceId, ArrayList<Tweet> tweets) {
		super(context, textViewResourceId, tweets);
		this.tweets = tweets;
	}

	@Override
	public View getView(int posicao, View convertView, ViewGroup parent){

		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) 
					getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.lista_tweets_adapter, null);
		}


		Tweet tweet = tweets.get(posicao);

		if (tweet != null) {

			TextView nome = (TextView) view.findViewById(R.id.nome);
			TextView usuario = (TextView) view.findViewById(R.id.usuario);
			ImageView imagem = (ImageView) view.findViewById(R.id.imagem_perfil);
			TextView mensagem = (TextView) view.findViewById(R.id.mensagem);
			TextView data = (TextView) view.findViewById(R.id.data);

			
			if(nome != null){
				nome.setText(tweet.getNome());
			}
			if(usuario != null){
				usuario.setText("@" + tweet.getUsuario());
			}
			if(imagem != null){
				BitmapManager.getInstance().loadBitmap(tweet.getImagem_url(), imagem);
			}
			if (mensagem != null){
				mensagem.setText(tweet.getMensagem());
				mensagem.setMovementMethod(LinkMovementMethod.getInstance());
			}
			if (data != null){
				
				data.setText(tweet.getData());
			}

		}

		return view;

	}

}

