package srgdeob;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * a simple program to convert joined.srg to joined.deob.srg which allows for deobfuscation with use of programs like re-caf
 * @author jredfox
 */
public class SrgDeob {
	
	public static Map<String,String> f = new HashMap(10000);
	public static Map<String,String> m = new HashMap(10000);
	
	public static void main(String[] args) throws IOException
	{
		loadMCP();
		File srg = new File(args.length == 0 ? "joined.srg" : args[0]).getAbsoluteFile();
		BufferedReader br = new BufferedReader(new FileReader(srg));
		String line = br.readLine();
		File out = new File("joined.deob.srg");
		BufferedWriter outwriter = new BufferedWriter(new FileWriter(out));
		while(line != null)
		{
			if(line.startsWith("PK:") || line.startsWith("CL:"))
			{
				
			}
			else if(line.startsWith("FD:"))
			{
				String field = line.split("\\s+")[2];
				String[] arr = field.split("/");
				field = arr[arr.length-1];
				String deob = f.containsKey(field) ? f.get(field) : field;
				line = line.replace(field, deob);
			}
			else if(line.startsWith("MD:"))
			{
				String method = line.split("\\s+")[3];
				String[] arr = method.split("/");
				method = arr[arr.length-1];
				String deob = m.containsKey(method) ? m.get(method) : method;
				line = line.replace(method, deob);
			}
			outwriter.write(line + System.lineSeparator());
			outwriter.flush();
			line = br.readLine();
		}
		br.close();
		outwriter.close();
		System.out.println("finished");
	}

	@SuppressWarnings("rawtypes")
	public static void loadMCP() throws IOException 
	{
		parseCSV(new File("fields.csv"), f);
		parseCSV(new File("methods.csv"), m);
	}

	private static void parseCSV(File csv, Map<String, String> map) throws IOException 
	{
		BufferedReader r = new BufferedReader(new FileReader(csv));
		String line = r.readLine();
		while(line != null)
		{
			map.put(line.split(",")[0], line.split(",")[1]);
			line = r.readLine();
		}
		r.close();
	}

}
