package com.obj;

import com.obj.Vertex;
import com.obj.parser.LineParser;

public class NormalParser extends LineParser {

	Vertex vertex = null;
	
	public NormalParser() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void parse() 
	{
		vertex = new Vertex();
		
		try
		{
			vertex.setX(Float.parseFloat(words[1]));
			vertex.setY(Float.parseFloat(words[2]));
			vertex.setZ(Float.parseFloat(words[3]));
		}
		catch(Exception e)
		{
			throw new RuntimeException("NormalParser Error");
		}

	}

	@Override
	public void incoporateResults(WavefrontObject wavefrontObject) {
		wavefrontObject.getNormals().add(vertex);
		
	}

}
