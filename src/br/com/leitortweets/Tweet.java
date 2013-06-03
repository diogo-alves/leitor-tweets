package br.com.leitortweets;

public class Tweet {
	
	private String nome;
	private String usuario;
	private String urlImagemPerfil;
	private String mensagem;
	private String dataEnvio;
	

	public Tweet() {
	}

	/**
	 * @param nome
	 * @param usuario
	 * @param urlImagemPerfil
	 * @param mensagem
	 * @param dataEnvio
	 */
	public Tweet(String nome, String usuario, String url,
			String mensagem, String data) {
		this.nome = nome;
		this.usuario = usuario;
		this.urlImagemPerfil = url;
		this.mensagem = mensagem;
		this.dataEnvio = data;
	}
	



	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}
	
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	/**
	 * @return the urlImagemPerfil
	 */
	public String getImagem_url() {
		return urlImagemPerfil;
	}

	/**
	 * @param urlImagemPerfil the urlImagemPerfil to set
	 */
	public void setImagem_url(String imagem_url) {
		this.urlImagemPerfil = imagem_url;
	}

	/**
	 * @return the mensagem
	 */
	public String getMensagem() {
		return mensagem;
	}
	
	/**
	 * @param mensagem the mensagem to set
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	/**
	 * @return the dataEnvio
	 */
	public String getData() {
		return dataEnvio;
	}
	
	/**
	 * @param dataEnvio the dataEnvio to set
	 */
	public void setData(String data) {
		this.dataEnvio = data;
	}
	

}
