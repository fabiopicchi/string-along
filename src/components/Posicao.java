package components;

import org.jbox2d.common.Vec2;

import com.artemis.Component;

public class Posicao extends Component {
	private Vec2 posicao;

	public Posicao(Vec2 posicao) {
		this.posicao = posicao;
	}

	public Vec2 getPosicao() {
		return posicao;
	}

	public void setPosicao(Vec2 posicao) {
		this.posicao = posicao;
	}
}
