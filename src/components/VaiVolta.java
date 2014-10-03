package components;

import org.jbox2d.common.Vec2;

import com.artemis.Component;

public class VaiVolta extends Component {
	private int intervalo;
	private float altura;
	private int tInicial;
	private float timer;
	private boolean started;
	private boolean vai;
	private float vVai;
	private float vVolta;
	private Vec2 posicaoInicial;
	private float proporcao;
	private String direcao;
	
	public float getPosicaoInicial() {
		if (direcao.equals("hor"))
			return posicaoInicial.x;
		else if (direcao.equals("ver"))
			return posicaoInicial.y;
		else return 0;
	}

	public void setPosicaoInicial(Vec2 posicaoInicial) {
		this.posicaoInicial = posicaoInicial;
	}
	
	public float getProporcao () 
	{
		return proporcao;
	}
	public String getDirecao () 
	{
		return direcao;
	}

	public VaiVolta (float altura, int intervalo, int tInicial, String tipo, int dir)
	{
		this.intervalo = intervalo;
		this.altura = altura * 86;
		this.tInicial = tInicial;
		timer = 0;
		started = false;
		vai = true;
		
		if(dir == 0){
			direcao = "hor";
		}else if (dir == 1){
			direcao = "ver";
		}
		if(tipo.equals("aranha")){
			proporcao = 0.75f;
		}else if(tipo.equals("plataforma")){
			proporcao = 0.5f;
		};
		vVai = this.altura / (intervalo * proporcao);
		vVolta = this.altura / (intervalo * (1.0f-proporcao));
	}
	
	public float getvVai() {
		return vVai;
	}

	public float getvVolta() {
		return vVolta;
	}

	public boolean isVai() {
		return vai;
	}

	public float getTimer () 
	{
		return timer;
	}
	
	public void updateTimer (int timeElapsed)
	{
		timer += timeElapsed;
		
		if (started == false && timer >= tInicial)
		{
			timer = 0;
			started = true;
		}
		
		else if ((timer >= (intervalo * proporcao)) && vai)
		{
			timer = (intervalo * proporcao);
			vai = !vai;
		}
		
		else if (timer >= intervalo)
		{
			timer = 0;
			vai = !vai;
		}
	}

	public int getIntervalo() {
		return intervalo;
	}

	public float getAltura() {
		return altura;
	}
	public boolean isStarted() {
		return started;
	}
}
