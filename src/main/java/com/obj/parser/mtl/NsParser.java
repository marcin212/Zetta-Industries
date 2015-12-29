package com.obj.parser.mtl;

import com.obj.Material;
import com.obj.WavefrontObject;
import com.obj.parser.LineParser;


public class NsParser extends LineParser {

	float ns;
	
	@Override
	public void incoporateResults(WavefrontObject wavefrontObject) 
	{
		Material currentMaterial = wavefrontObject.getCurrentMaterial() ;
		currentMaterial.setShininess( ns );

	}

	@Override
	public void parse() 
	{
		try
		{
			ns = Float.parseFloat( words[1] );
		}
		catch(Exception e)
		{
			throw new RuntimeException("VertexParser Error");
		}
	}

}
