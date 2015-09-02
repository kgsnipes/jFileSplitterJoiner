package fileop.ops;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.io.Files;

public class FileOP {
	
	static int parts=20;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if(args[0].equals("split"))
		{
			parts=Integer.parseInt(args[1]);
			fileSplit(args[2]);
			
		}
		else if(args[0].equals("join"))
		{
			parts=Integer.parseInt(args[1]);
			fileJoin(args[2]);
		}
	}
	
	public static void fileSplit(String filepath) throws IOException 
	{
		
		File file=new File(filepath);
		long fileSize=file.length();
		
		int onePart= (int)(fileSize/parts);
		String destPath=file.getParentFile().getPath()+"\\splits\\";
		new File(destPath).mkdir();
		String fileName=file.getName().substring(0, file.getName().lastIndexOf("."));
		RandomAccessFile fin=new RandomAccessFile(filepath,"r");
		try{
		for(int i=1;i<=parts;i++)
		{
			System.out.println(destPath+fileName+"_"+i+".bak");
			File f=new File(destPath+fileName+"_"+i+".bak");
			byte a[]=new byte[onePart];
			fin.readFully(a, 0, onePart);
			Files.write(a, f);
			
		}
		int diff=(int) (fileSize-(onePart*parts));
		if(diff>0)
		{
			parts++;
			System.out.println(destPath+fileName+"_"+(parts)+".bak");
			File f=new File(destPath+fileName+"_"+(parts)+".bak");
			byte a[]=new byte[diff];
			fin.readFully(a, 0, diff);
			Files.write(a, f);
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		finally
		{
			new ObjectMapper().writeValue(new File("fileop.json"), new FileTransformationDTO(file.getName(), parts));
			if(fin!=null)
				fin.close();
		}
		
	}
	
	public static void fileJoin(String folderPath)throws IOException
	{
		if(new File(folderPath+"\\fileop.json").exists())
		{
			fileJoinWithJson(folderPath);
		}
		else
		{
			fileJoinWithoutJson(folderPath);
		}
	}
	
	
	public static void fileJoinWithJson(String folderPath) throws IOException
	{
		FileTransformationDTO filedto=new ObjectMapper().readValue(new File(folderPath+"\\fileop.json"), FileTransformationDTO.class);
		FileOutputStream output = new FileOutputStream(folderPath+"\\"+filedto, true);
		try {
			for(int i=1;i<=parts;i++)
			{
				String filename=fileAvailable(folderPath,i);
				if(filename!=null)
				{
					System.out.println("Writing file :"+ filename);
					 output.write(Files.toByteArray(new File(folderPath+"\\"+filename)));
				}
			}
		} finally {
		   output.close();
		}
		
	}
	
	public static void fileJoinWithoutJson(String folderPath) throws IOException
	{
		
		FileOutputStream output = new FileOutputStream(folderPath+"\\joinedFile.joined", true);
		try {
			for(int i=1;i<=parts;i++)
			{
				String filename=fileAvailable(folderPath,i);
				if(filename!=null)
				{
					System.out.println("Writing file :"+ filename);
					 output.write(Files.toByteArray(new File(folderPath+"\\"+filename)));
				}
			}
		} finally {
		   output.close();
		}
		
	}
	
	public static String fileAvailable(String folderPath,int part)
	{
		File folder=new File(folderPath);
		List<String> fileList=Arrays.asList(folder.list());
		for(String s:fileList)
		{
			if(s.lastIndexOf("_"+part+".bak")!=-1)
			{
				return s;
			}
		}
		return null;
	}

}
