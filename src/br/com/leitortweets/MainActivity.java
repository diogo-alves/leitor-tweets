/*
 * @author: Diogo Alves <diogo.alves.ti@gmail.com>
 */

package br.com.leitortweets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener{

	private static final String CORINTHIANS = "sitecorinthians";
	private static final String FLAMENGO = "CR_flamengo";
	private static final String SAO_PAULO = "SaoPauloFC";
	private static final String VASCO = "crvascodagama";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		findViewById(R.id.btn_corinthians).setOnClickListener(this);
		findViewById(R.id.btn_flamengo).setOnClickListener(this);
		findViewById(R.id.btn_sao_paulo).setOnClickListener(this);
		findViewById(R.id.btn_vasco).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.btn_corinthians:
			buscarTweets(CORINTHIANS);
			break;

		case R.id.btn_flamengo:
			buscarTweets(FLAMENGO);
			break;

		case R.id.btn_sao_paulo:
			buscarTweets(SAO_PAULO);
			break;

		case R.id.btn_vasco:
			buscarTweets(VASCO);
			break;

		}
	} 

	private void buscarTweets(String termo){
		Intent i = new Intent(getApplicationContext(), LeitorTweetsActivity.class);
		i.putExtra("TERMO_DE_BUSCA", termo);
		startActivity(i);
	}
}