package com.obj.parser.obj;

import com.obj.Vertex;
import com.obj.WavefrontObject;
import com.obj.parser.LineParser;

public class VertexParser extends LineParser {

	Vertex vertex = null;
	
	public VertexParser() {
	}

	@Override
	public void parse() {
		vertex = new Vertex();
		
		try
		{
			vertex.setX(Float.parseFloat(words[1]));
			vertex.setY(Float.parseFloat(words[2]));
			vertex.setZ(Float.parseFloat(words[3]));
		}
		catch(Exception e)
		{
			throw new RuntimeException("VertexParser Error");
		}
	}

	@Override
	public void incoporateResults(WavefrontObject wavefrontObject) 
	{
		vertex.setX((vertex.getX()+wavefrontObject.translate.getX())*wavefrontObject.xScale);
		vertex.setY((vertex.getY()+wavefrontObject.translate.getY())*wavefrontObject.yScale);
		vertex.setZ((vertex.getZ()+wavefrontObject.translate.getZ())*wavefrontObject.zScale);
		wavefrontObject.getVertices().add(vertex);
	}

}
