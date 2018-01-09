package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenuScreen implements Screen
{
    final Drop _game;

    OrthographicCamera _camera;

    public MainMenuScreen(final Drop game)
    {
        _game = game;

        _camera = new OrthographicCamera();
        _camera.setToOrtho(false,800,480);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _camera.update();
        _game.batch.setProjectionMatrix(_camera.combined);

        _game.batch.begin();
        _game.font.draw(_game.batch, "Welcome to Drop!!", 100, 150);
        _game.font.draw(_game.batch, "Tap anywhere to begin!", 100, 100);
        _game.batch.end();

        if (Gdx.input.isTouched())
        {
            _game.setScreen(new GameScreen(_game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }
}
