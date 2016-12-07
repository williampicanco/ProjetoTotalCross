package br.com.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("contato")
public class Contato {

	 @XStreamAsAttribute
	 private String nome;

	  @XStreamAsAttribute
	  private String fone;

	  @XStreamAsAttribute
	  private String sexo;

	  public Contato()
	  {
		  this(null, null, null);
	  }
	  
	  public Contato(String nome, String fone, String sexo) {
		    this.nome = nome;
		    this.fone = fone;
		    this.sexo = sexo;
		  }
	  
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFone() {
		return fone;
	}

	public void setFone(String fone) {
		this.fone = fone;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
}
