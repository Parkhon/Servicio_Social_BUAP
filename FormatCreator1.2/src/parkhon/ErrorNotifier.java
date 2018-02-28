package parkhon;

public class ErrorNotifier 
{
	/*
	 * This is the error notifier. It notifies the user when they have
	 * done something incorrect, or a file they are trying to use is not
	 * compatible with the Form Creator.
	 * 
	 * Errors are always bound to a single input window, and they can be
	 * immediately reported. So there is no need for complex error holding
	 * structure.
	 * 
	 * This class can serve errors statically, and be reused to report any
	 * number of errors later on.
	 */
	//Attributes
	private static String errorMessage = "";
	private static boolean errorsExist;
	
	//-------------------------------------------
	//-------------------------------------------
	//Constructor
	//-------------------------------------------
	//-------------------------------------------
	//Methods
	public static void resetErrorMessage()
	{
		errorMessage = "";
	}
	public static void pushErrorMessage(String errorMessage)
	{
		//Set the new error message.
		ErrorNotifier.errorMessage += System.lineSeparator()+ errorMessage;
		errorsExist = true;
	}
	public static String popErrorMessage()
	{
		errorsExist = false;
		return ErrorNotifier.errorMessage;
	}
	public static boolean isErrorsExist()
	{
		return errorsExist;
	}
	//-------------------------------------------
	//-------------------------------------------
}