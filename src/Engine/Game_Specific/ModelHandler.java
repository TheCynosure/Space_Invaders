package Engine.Game_Specific;

import Engine.Model;
import Engine.ResourceHandler;

/**
 * Created by Jack on 3/23/2016.
 */
public class ModelHandler {

    private Model[] enemies;
    private Model player;
    private Model projectile;

    public ModelHandler() {
        //Loading all the Models we need
        enemies = new Model[3];
        enemies[0] = ResourceHandler.OBJ_LOADER.loadObjModel("res/models/enemies/Space_Invader_0.obj", "res/textures/blank_tex.png", "PNG");
        enemies[1] = ResourceHandler.OBJ_LOADER.loadObjModel("res/models/enemies/Space_Invader_1.obj", "res/textures/blank_tex.png", "PNG");
        enemies[2] = ResourceHandler.OBJ_LOADER.loadObjModel("res/models/enemies/Space_Invader_2.obj", "res/textures/blank_tex.png", "PNG");
        player = ResourceHandler.OBJ_LOADER.loadObjModel("res/models/ship.obj", "res/textures/blank_tex.png", "PNG");
        projectile = ResourceHandler.OBJ_LOADER.loadObjModel("res/models/box.obj", "res/textures/blank_tex.png", "PNG");
    }

    public void scaleEnemies(float scale) {
        for(Model model: enemies) {
            model.scaleXY(scale);
        }
    }

    public Model[] getEnemies() {
        return enemies;
    }

    public Model getPlayer() {
        return player;
    }

    public Model getProjectile() {
        return projectile;
    }
}
