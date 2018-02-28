package parkhon.data_structures;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;

public class FormOutputManager 
{
	/*
	 * This class handles all runtime form creations and retrievals.
	 * 
	 * It overseer the runtime resource folder "outputs", creating it if need be and 
	 * writing and reading resources as necessary.
	 */
	//Attributes
	private static String workingDirectoryPath;
	private static File workingDirectory;
	//---------------------------------------------------------------
	//---------------------------------------------------------------
	//Initialization
	public static void initialize()
	{
		//Working Directory:
		workingDirectoryPath = "tmp";
		workingDirectory = new File(workingDirectoryPath);
		workingDirectory.mkdirs();	//Creating the tmp directory.
	}
	//---------------------------------------------------------------
	//---------------------------------------------------------------
	//Methods
	public static String getResourceURL(String filename)
	{
		String url = "";	//Output preparation
		if(workingDirectory != null)
		{
			File target = new File(workingDirectory.getAbsoluteFile() + "/" + filename);	//The new file.
			if(target != null)
			{
				url = target.getAbsolutePath();
			}
			else
			{
				//ERROR File does not exist
			}
			//url = workingDirectory.
		} else
		{
			//ERROR
		}
		return url;
	}
	public static boolean writeFile(String filename, String contents)
	{
		boolean methodSuccess = false;
		File target = new File(workingDirectory.getAbsolutePath() + "/" + filename);
		try {
			Formatter formatter = new Formatter(target);
			formatter.format("%s", contents);
			formatter.close();
			methodSuccess = true;
		} catch (FileNotFoundException e) {
			//ERROR File not found
			// TODO Auto-generated catch block
			methodSuccess = false;
			e.printStackTrace();
		}
		return methodSuccess;
	}
	//---------------------------------------------------------------
	//---------------------------------------------------------------
}
