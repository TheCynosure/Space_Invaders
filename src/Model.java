import org.lwjgl.opengl.*;

/**
 * Created by john_bachman on 3/11/16.
 */
public class Model {
    private int vaoID;
    private int vertexCount;
    private int indicesBufferID;
    private Texture texture;

    public Model(int vaoID, int indicesBufferID, int vertexCount, Texture texture) {
        this.indicesBufferID = indicesBufferID;
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
        this.texture = texture;
    }

    public void render() {
        //Bind the VAO so we can access the Vertices
        GL30.glBindVertexArray(vaoID);
        //Bind the first attribute because that's were we put the vertices.
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        //Bind the Indices which are in a separate VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBufferID);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER ,GL11.GL_NEAREST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        //Draw the elements using the indices to access the correct vertices.
        GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);
        //Unbind the Indices
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        //Unbind the vertex vbo
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        //Unbind the VAO
        GL30.glBindVertexArray(0);
    }

    public int getVaoID() {
        return vaoID;
    }
}
