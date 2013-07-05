/*
 * @author: Diogo Alves <diogo.alves.ti@gmail.com>
 */

package br.com.leitortweets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

public class Tweet {
	
	private String nome;
	private String usuario;
	private String urlImagemPerfil;
	private String mensagem;
	private String dataEnvio;
	
	public Tweet() {
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUrlImagemPerfil() {
		return urlImagemPerfil;
	}

	public void setUrlImagemPerfil(String url) {
		this.urlImagemPerfil = url;
	}

	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public String getData() {
		return dataEnvio;
	}
	
	public void setData(String data) {
		String dataSemTimeZone = removerTimeZone(data);
		this.dataEnvio = formatarData(dataSemTimeZone);
	}
	
	private String formatarData(String data){
		String strData = null;
		TimeZone tzUTC = TimeZone.getTimeZone("UTC");
		SimpleDateFormat formatoEntrada = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US);
		formatoEntrada.setTimeZone(tzUTC);
		SimpleDateFormat formatoSaida = new SimpleDateFormat("EEE, dd/MM/yy, 'às' HH:mm");
		
		try {
			strData = formatoSaida.format(formatoEntrada.parse(data));
		} catch (ParseException e) {
		Log.e("Erro parser data", Log.getStackTraceString(e));
		}
		return strData;
	}
	
	/**
	 * Remove, utilizando uma expressão regular, a timezone da data, o que evita a demora (possível bug?)
	 * ao fazer o parser da data usando o método simpleDateFormat.
	 * @param data a data ainda com a timezone (Ex.: "Sun Jun 16 13:25:23 +0000 2013", onde "+0000"
	 * é a timezone)
	 * 
	 * @return a data sem a timezone (Ex.: "Sun Jun 16 13:25:23 2013")
	 * 
	 * @see {@link java.util.regex.Pattern}
	 */
	private String removerTimeZone(String data){
		// busca na string e remove o padrão " (+ ou -)dddd" Ex: " +3580"
		return data.replaceFirst("(\\s[+|-]\\d{4})", "");
	}

}
