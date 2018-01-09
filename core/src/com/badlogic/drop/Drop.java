package com.badlogic.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Drop extends ApplicationAdapter {

    // Textures
	private Texture _dropImage;
	private Texture _bucketImage; // 64 x 64
    private Rectangle _bucket;

	// Audio
	private Sound _dropSound;
	private Music _rainMusic;

	// Sprites
    private OrthographicCamera _camera;
    private SpriteBatch _batch;

    private Array<Rectangle> _raindrops;
    private long lastDropTime;


    @Override
	public void create () {
        // load images for droplet and bucket
		_dropImage = new Texture(Gdx.files.internal("droplet.png"));
		_bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		// load sound for droplet and rain music
        _dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        _rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        // start the playback of background music
        _rainMusic.setLooping(true);
        _rainMusic.play();

        // create camera
        _camera = new OrthographicCamera();
        _camera.setToOrtho(false, 800, 480);

        // create sprite batch
        _batch = new SpriteBatch();

        // instantiate the bucket
        _bucket = new Rectangle();
        _bucket.x = 800 / 2 - 64 / 2; // Centering
        // By default, all rendering in libgdx (and OpenGL) is performed with the y-axis pointing upwards.
        _bucket.y = 20;
        _bucket.width = 64;
        _bucket.height = 64;

        // create raindrop
        _raindrops = new Array<Rectangle>();
        spawnRaindrop();
	}

	@Override
    public void render()
    {
        // clear screen with dark blue color
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _camera.update();


        // spritebatch rendering
        _batch.setProjectionMatrix(_camera.combined);
        _batch.begin(); // begin
        // render bucket
        _batch.draw(_bucketImage, _bucket.x, _bucket.y);
        // render raindrops
        for (Rectangle raindrop : _raindrops)
            _batch.draw(_dropImage, raindrop.x, raindrop.y);
        _batch.end(); //end

        // Move the bucket
        if (Gdx.input.isTouched())
        {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(),Gdx.input.getY(), 0);
            _camera.unproject(touchPos);
            _bucket.x = touchPos.x - 64 / 2;
        }

        // Keyboard movements
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
            _bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
            _bucket.x += 200 * Gdx.graphics.getDeltaTime();

        // Window limitations
        if (_bucket.x < 0)
            _bucket.x = 0;
        if (_bucket.x > 800 - 64)
            _bucket.x = 800 - 64;

        // if enough time has passed; spawn drop
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
            spawnRaindrop();

        // iterate through raindrops
        //  + move raindrop down
        //  + remove if below screen
        Iterator<Rectangle> iter = _raindrops.iterator();
        while (iter.hasNext())
        {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0)
                iter.remove();
            if (raindrop.overlaps(_bucket))
            {
                _dropSound.play();
                iter.remove();
            }
        }
    }

    // spawns a raindrop in a random location
    private void spawnRaindrop()
    {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        _raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    // cleanup after game is closed
    @Override
    public void dispose()
    {
        _dropImage.dispose();
        _bucketImage.dispose();
        _dropSound.dispose();
        _rainMusic.dispose();
        _batch.dispose();
    }
}
