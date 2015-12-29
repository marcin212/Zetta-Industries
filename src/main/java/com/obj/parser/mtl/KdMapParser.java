package com.obj.parser.mtl;


import com.obj.Texture;
import com.obj.TextureLoader;
import com.obj.Material;
import com.obj.WavefrontObject;
import com.obj.parser.LineParser;

public class KdMapParser extends LineParser {

	private Texture texture = null;
	private WavefrontObject object = null;
	private String texName;
	
	public KdMapParser(WavefrontObject object)
	{
		this.object = object;
	}
	
	@Override
	public void incoporateResults(WavefrontObject wavefrontObject) {
		
		if (texture != null)
		{
			Material currentMaterial = wavefrontObject.getCurrentMaterial() ;
			currentMaterial.texName = texName;
			currentMaterial.setTexture(texture);
		}
	}

	@Override
	public void parse() {
		String textureFileName = words[words.length-1];
		texName = textureFileName;
		String pathToTextureBinary = object.getContextfolder() +  textureFileName;
		texture = TextureLoader.instance().loadTexture(pathToTextureBinary);
	}

}
