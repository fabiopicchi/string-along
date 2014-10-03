package constants;

public enum GameObject {
	PEROLA ("perola"),
	CHAVE ("chave"),
	CAIXA ("caixa"),
	PORTA ("porta"),
	ESPINHO ("espinho"),
	TOCO ("toco"),
	TOCOPULA ("tocoPula"),
	ARANHA ("aranha"),
	CARRETEL ("carretel"),
	PARTE1 ("parte1"),
	PARTE2 ("parte2"),
	PARTE3 ("parte3"),
	TOCOBLOCK ("tocoblock"),
	PLATAFORMAMOVEL ("plataformamovel"),
	PLATAFORMAQUECAI ("plataformaquecai"),
	PLATAFORMATRANSPONIVEL ("plataformatransponivel");
	
	private String code;
	
	private GameObject (String code)
	{
		this.code = code;
	}
	
	public String getCode ()
	{
		return code;
	}
}
