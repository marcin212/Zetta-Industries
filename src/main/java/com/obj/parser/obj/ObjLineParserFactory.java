package com.obj.parser.obj;

//import java.util.Hashtable;
import com.obj.LineParserFactory;
import com.obj.NormalParser;
import com.obj.WavefrontObject;
import com.obj.parser.CommentParser;
import com.obj.parser.mtl.MaterialFileParser;


public class ObjLineParserFactory extends LineParserFactory{


	
	public ObjLineParserFactory(WavefrontObject object)
	{
		this.object = object;
		parsers.put("v",new VertexParser());
		parsers.put("vn",new NormalParser());
		parsers.put("vp",new FreeFormParser());
		parsers.put("vt",new TextureCooParser());
		parsers.put("f",new FaceParser(object));
		parsers.put("#",new CommentParser());
		parsers.put("mtllib",new MaterialFileParser(object));
		parsers.put("usemtl",new MaterialParser());
		parsers.put("o",new GroupParser());
	}

	
}
