package systems;

import main.StringAlong;
import processing.core.PApplet;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;

import ddf.minim.AudioPlayer;
import ddf.minim.AudioSample;
import ddf.minim.Minim;

public class SoundSystem extends EntitySystem
{
	PApplet parent;
	AudioSample pickupsound;
	AudioSample cordaquebra;
	AudioSample aranha;
	AudioSample baternoteto;
	AudioSample tocomorrendo;
	AudioSample carretel;
	AudioPlayer level1;
	AudioPlayer level2;

	public SoundSystem ()
	{
		this.parent = StringAlong.getInstance();
	}

	@Override
	protected void initialize ()
	{
		Minim minim = new Minim(parent);
		level1 = minim.loadFile("sound/fase1.mp3", 8192);
		level2 = minim.loadFile("sound/fase2.mp3", 8192);
		pickupsound = minim.loadSample("sound/pickup.wav");
		cordaquebra = minim.loadSample("sound/corda.wav");
		aranha = minim.loadSample("sound/aranha.wav");
		baternoteto = minim.loadSample("sound/baternoteto.wav");
		tocomorrendo = minim.loadSample("sound/stomp.wav");
		carretel = minim.loadSample("sound/carretel2.wav");
	}
	
	public void play (String file)
	{
		if(file.equals("pickup")) pickupsound.trigger();
		if(file.equals("cordaquebra")) cordaquebra.trigger();
		if(file.equals("aranha")) aranha.trigger();
		if(file.equals("baternoteto")) baternoteto.trigger();
		if(file.equals("tocomorrendo")) tocomorrendo.trigger();
		if(file.equals("carretel")) carretel.trigger();
		if(file.equals("level1")) level1.loop();
		if(file.equals("level2")) level1.loop();
	}
	
	public void close ()
	{
		level1.pause();
		level2.pause();
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}
}