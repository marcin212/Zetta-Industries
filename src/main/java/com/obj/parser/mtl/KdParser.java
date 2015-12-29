package com.obj.parser.mtl;

import com.obj.Vertex;
import com.obj.Material;
import com.obj.WavefrontObject;
import com.obj.parser.LineParser;


public class KdParser extends LineParser {

	Vertex kd = null;
	
	@Override
	public void incoporateResults(WavefrontObject wavefrontObject) {
		Material currentMaterial = wavefrontObject.getCurrentMaterial() ;
		currentMaterial.setKd(kd);

	}

	@Override
	public void parse() {
		kd = new Vertex();
		
		try
		{
			kd.setX(Float.parseFloat(words[1]));
			kd.setY(Float.parseFloat(words[2]));
			kd.setZ(Float.parseFloat(words[3]));
		}
		catch(Exception e)
		{
			throw new RuntimeException("VertexParser Error");
		}
	}

}
