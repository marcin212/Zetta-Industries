package com.obj.parser.mtl;

//import java.util.Hashtable;
import com.obj.LineParserFactory;
//import com.obj.NormalParser;
import com.obj.WavefrontObject;
import com.obj.parser.CommentParser;
//import com.obj.parser.DefaultParser;
//import com.obj.parser.LineParser;
import com.obj.parser.mtl.KdMapParser;
import com.obj.parser.mtl.KdParser;
//import com.obj.parser.mtl.MaterialFileParser;
import com.obj.parser.mtl.MaterialParser;




public class MtlLineParserFactory extends LineParserFactory
{
	public MtlLineParserFactory(WavefrontObject object)
	{
		this.object = object;
		parsers.put("newmtl",new MaterialParser());
		parsers.put("Ka",new KaParser());
		parsers.put("Kd",new KdParser());
		parsers.put("Ks",new KsParser());
		parsers.put("Ns",new NsParser());		
		parsers.put("map_Kd",new KdMapParser(object));
		parsers.put("#",new CommentParser());
	}
	
	
		
	
		
	
}
