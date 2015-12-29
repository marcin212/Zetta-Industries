package com.obj.parser.mtl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
//import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import com.obj.Material;
import com.obj.WavefrontObject;
import com.obj.parser.LineParser;

public class MaterialFileParser extends LineParser {

	Hashtable<String,Material> materials = new Hashtable<String,Material>();
	private WavefrontObject object;
	private MtlLineParserFactory parserFactory = null;
	
	public MaterialFileParser(WavefrontObject object )
	{
		this.object = object;
		this.parserFactory = new MtlLineParserFactory(object);
	}
	
	@Override
	public void incoporateResults(WavefrontObject wavefrontObject) {
		// Material are directly added by the parser, no need to do anything here...
	}

	@Override
	public void parse() {
		String filename  = words[1];
		
		String pathToMTL = object.getContextfolder() + filename;

		InputStream fileInput = this.getClass().getResourceAsStream(pathToMTL);
		if (fileInput == null)
			// Could not find the file in the jar.
			try
			{
				File file = new File(pathToMTL);
				if (file.exists())
					fileInput = new FileInputStream(file);
			}
			catch(Exception e2)
			{
				e2.printStackTrace();
			}
		
		String currentLine = null;
		try 
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(fileInput));
		
			currentLine = null;
			while((currentLine = in.readLine()) != null)
			{
				
				LineParser parser = parserFactory.getLineParser(currentLine) ;
				parser.parse();
				parser.incoporateResults(object);
			}
			
			if (in != null)
				in.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error on line:"+currentLine);
			throw new RuntimeException("Error parsing :'"+pathToMTL+"'");
		}
		
	}

	
}