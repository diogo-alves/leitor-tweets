/*
 * @author: Diogo Alves <diogo.alves.ti@gmail.com>
 */

package br.com.leitortweets;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class TweetAdapter  extends ArrayAdapter<Tweet> {

	private Context context;
	private ArrayList<Tweet> tweets;

	public TweetAdapter(Context context, int viewResourceId, ArrayList<Tweet> tweets) {
		super(context, viewResourceId, tweets);
		this.context = context;
		this.tweets = tweets;
	}
	
	@Override
	public View getView(int posicao, View view, ViewGroup parent){

		/* 
		 * Caso queira otimizar este código aconselho ler este artigo:
		 * www.piwai.info/android-adapter-good-practices
		 */

		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.tweet_adapter, parent, false);
		}

		Tweet tweet = tweets.get(posicao);

		if (tweet != null) {

			TextView nome = (TextView) view.findViewById(R.id.nome);
			TextView usuario = (TextView) view.findViewById(R.id.usuario);
			ImageView imagem = (ImageView) view.findViewById(R.id.imagem_perfil);
			TextView mensagem = (TextView) view.findViewById(R.id.mensagem);
			TextView data = (TextView) view.findViewById(R.id.data);

			nome.setText(tweet.getNome());
			usuario.setText("@" + tweet.getUsuario());
			BitmapManager.getInstance().loadBitmap(tweet.getUrlImagemPerfil(), imagem);
			mensagem.setText(tweet.getMensagem());
			data.setText(tweet.getData());
		}

		return view;
	}
}

