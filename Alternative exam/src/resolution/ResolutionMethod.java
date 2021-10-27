// Alternative exam -- 4 term
// © Matthew Sobolewski and Victor Stepovik
// "A GIANT OF THOUHT, A FATHER OF RUSSIAN RES(V)OLUTION"
// Resolution method version beta 0.30
// Updates:
// -- try/catch/finally added for the main class (with lists of potential commands)
// -- each iteration demonstration added (only for "all number" method)
// -- idz function added
// -- semantic resolution minor bug fixes
// -- output function major update
// -- file output for predicate added
// -- old examples deleted, new files with examples added
// -- main class minor bug fixes

package resolution;

import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Main class "ResolutionMethod"
 * @author MatmanBJ
 * @version beta 0.30
 */
public class ResolutionMethod
{
	private static List<String> logicList = Arrays.asList("statement", "predicate");
	private static List<String> inputList = Arrays.asList("console", "file");
	private static List<String> flagList = Arrays.asList("-l", "-s");
	private static List<String> treatmentList = Arrays.asList("saturation", "preference", "strikeout", "strategies_demonstration", "all", "all_number", "all_find", "idz", "all_unique", "semantic");
	private static List<String> treatmentListPredicate = Arrays.asList("all_unique");
	private static List<String> outputList = Arrays.asList("console", "file", "console file", "file console", "all", "none");
	
	public static boolean isNumber (String str)
	{
	    if (str == null || str.isEmpty())
	    {
	    	return false;
	    }
	    for (int i = 0; i < str.length(); i++)
	    {
	        if (!Character.isDigit(str.charAt(i)))
	        {
	        	return false;
	        }
	    }
	    return true;
	}
	
	public static void StrategiesDemonstration (ResolutionFunction localLocalRF) throws Exception // resolution strategy demonstrations
	{
		// I STOPPED HERE
		ResolutionFunction localStrategy;
		System.out.println("---------- ALL UNIQUE STRATEGY ----------");
		ResolutionDisjunct.SetMaxID();
		localStrategy = new ResolutionFunction ();
		localStrategy.ResolutionFileInput("example_theory.txt");
		localStrategy.ResolutionAllUnique();
		localStrategy.GetFunction();
		System.out.println("---------- SATURATION STRATEGY ----------");
		ResolutionDisjunct.SetMaxID();
		localStrategy = new ResolutionFunction ();
		localStrategy.ResolutionFileInput("example_theory.txt");
		localStrategy.ResolutionSaturation();
		localStrategy.GetFunction();
		System.out.println("---------- PREFERENCE STRATEGY ----------");
		ResolutionDisjunct.SetMaxID();
		localStrategy = new ResolutionFunction ();
		localStrategy.ResolutionFileInput("example_theory.txt");
		localStrategy.ResolutionPreference();
		localStrategy.GetFunction();
		System.out.println("---------- STRIKEOUT STRATEGY ----------");
		ResolutionDisjunct.SetMaxID();
		localStrategy = new ResolutionFunction ();
		localStrategy.ResolutionFileInput("example_theory.txt");
		localStrategy.ResolutionStrikeout();
		localStrategy.GetFunction();
	}
	
	public static ResolutionFunction TreatmentMain (String localString, ResolutionFunction localRF) throws Exception
	{
		if (localString.equals("saturation"))
		{
			localRF.ResolutionSaturation();
		}
		else if (localString.equals("preference"))
		{
			localRF.ResolutionPreference();
		}
		else if (localString.equals("strikeout"))
		{
			localRF.ResolutionStrikeout();
		}
		else if (localString.equals("strategies_demonstration"))
		{
			StrategiesDemonstration(localRF);
		}
		else if (localString.equals("all"))
		{
			localRF.ResolutionAll();
		}
		else if (localString.equals("all_number"))
		{
			Scanner inputScanner = new Scanner(System.in);
			System.out.println("Please write number of iterations:");
			int localIters = inputScanner.nextInt();
			System.out.println("Current number of iterations: " + localIters);
			
			localRF.ResolutionAllNumber(localIters);
		}
		else if (localString.equals("all_find"))
		{
			localRF.ResolutionAllFind();
		}
		else if (localString.equals("idz"))
		{
			localRF.ResolutionIDZ();
		}
		else if (localString.equals("all_unique"))
		{
			localRF.ResolutionAllUnique();
		}
		else if (localString.equals("semantic"))
		{
			localRF.ResolutionSemantic();
		}
		else if (localString.equals("semantic_test"))
		{
			// NOTHING YET
		}
		else
		{
			System.out.println("Incorrect type of treatment! Nothing is done with function");
		}
		return localRF;
	}
	
	public static ResolutionFunction TreatmentMainPredicate (String localString, ResolutionFunction localRF) throws Exception
	{
		if (localString.equals("all_unique"))
		{
			localRF.ResolutionAllUniquePredicate();
		}
		else
		{
			System.out.println("Incorrect type of treatment! Nothing is done with function");
		}
		return localRF;
	}
	
	public static ResolutionFunction InputMain (String localType)
	{
		ResolutionFunction cons = new ResolutionFunction ();
		if (localType.equals("console"))
		{
			System.out.println("---------- CONSOLE INPUT ----------");
			cons = new ResolutionFunction("INPUT");
		}
		else if (localType.equals("file"))
		{
			System.out.println("---------- FILE INPUT ----------");
			Scanner inputScanner = new Scanner(System.in);
			System.out.println("Please write file name(<file name>):");
			String localFS = inputScanner.nextLine();
			cons.ResolutionFileInput(localFS);
		}
		else
		{
			System.out.println("Incorrect type of input! Default input from console");
		}
		return cons;
	}
	
	public static ResolutionFunction InputMainPredicate (String localType)
	{
		ResolutionFunction cons = new ResolutionFunction ();
		if (localType.equals("console"))
		{
			System.out.println("---------- CONSOLE INPUT ----------");
			cons = new ResolutionFunction(1);
		}
		else if (localType.equals("file"))
		{
			System.out.println("---------- FILE INPUT ----------");
			Scanner inputScanner = new Scanner(System.in);
			System.out.println("Please write file name(<file name>):");
			String localFS = inputScanner.nextLine();
			cons.ResolutionFileInputPredicate(localFS);
		}
		else
		{
			System.out.println("Incorrect type of input! Default input from console");
		}
		return cons;
	}
	
	public static void OutputMain (ResolutionFunction localOutput, String localType)
	{
		if (localType.equals("console"))
		{
			System.out.println("---------- CONSOLE OUTPUT ----------");
			localOutput.GetFunction();
		}
		else if (localType.equals("file"))
		{
			System.out.println("---------- CHOOSED FILE OUTPUT ----------");
			Scanner outputScanner = new Scanner(System.in);
			System.out.println("Please write file name and specification (<file name> <specification>):");
			String localFS = outputScanner.nextLine();
			String localFSArray [] = localFS.split("\\s");
			if (localFSArray[1].equals("readable"))
			{
				localOutput.ResolutionFileReadable("readable_" + localFSArray[0]);
			}
			else if (localFSArray[1].equals("unreadable"))
			{
				localOutput.ResolutionFileUnreadable("unreadable_" + localFSArray[0]);
			}
			else
			{
				System.out.println("Incorrect type of output");
			}
		}
		else if (localType.equals("console file") || localType.equals("file console"))
		{
			System.out.println("---------- CONSOLE AND CHOOSED FILE OUTPUT ----------");
			localOutput.GetFunction();
			Scanner outputScanner = new Scanner(System.in);
			System.out.println("Please write file name and specification (<file name> <specification>):");
			String localFS = outputScanner.nextLine();
			String localFSArray [] = localFS.split("\\s");
			if (localFSArray[1].equals("readable"))
			{
				localOutput.ResolutionFileReadable("readable_" + localFSArray[0]);
			}
			else if (localFSArray[1].equals("unreadable"))
			{
				localOutput.ResolutionFileUnreadable("unreadable_" + localFSArray[0]);
			}
			else
			{
				System.out.println("Incorrect type of output");
			}
		}
		else if (localType.equals("all"))
		{
			System.out.println("---------- CONSOLE AND ALL FILES OUTPUT ----------");
			localOutput.GetFunction();
			Scanner outputScanner = new Scanner(System.in);
			System.out.println("Please write file name (<file name>):");
			String localFS = outputScanner.nextLine();
			localOutput.ResolutionFileReadable("readable_" + localFS);
			localOutput.ResolutionFileUnreadable("unreadable_" + localFS);
		}
		else if (localType.equals("none"))
		{}
		else
		{
			System.out.println("Incorrect type of output");
		}
	}
	
	public static void OutputMainPredicate (ResolutionFunction localOutput, String localType)
	{
		if (localType.equals("console"))
		{
			System.out.println("---------- CONSOLE OUTPUT ----------");
			localOutput.GetFunctionPredicate();
		}
		else if (localType.equals("file"))
		{
			System.out.println("---------- CHOOSED FILE OUTPUT ----------");
			Scanner outputScanner = new Scanner(System.in);
			System.out.println("Please write file name and specification (<file name> <specification>):");
			String localFS = outputScanner.nextLine();
			String localFSArray [] = localFS.split("\\s");
			if (localFSArray[1].equals("readable"))
			{
				localOutput.ResolutionFileReadablePredicate("readable_" + localFSArray[0]);
			}
			else if (localFSArray[1].equals("unreadable"))
			{
				localOutput.ResolutionFileUnreadablePredicate("unreadable_" + localFSArray[0]);
			}
			else
			{
				System.out.println("Incorrect type of output");
			}
		}
		else if (localType.equals("console file") || localType.equals("file console"))
		{
			System.out.println("---------- CONSOLE AND CHOOSED FILE OUTPUT ----------");
			localOutput.GetFunctionPredicate();
			Scanner outputScanner = new Scanner(System.in);
			System.out.println("Please write file name and specification (<file name> <specification>):");
			String localFS = outputScanner.nextLine();
			String localFSArray [] = localFS.split("\\s");
			if (localFSArray[1].equals("readable"))
			{
				localOutput.ResolutionFileReadablePredicate("readable_" + localFSArray[0]);
			}
			else if (localFSArray[1].equals("unreadable"))
			{
				localOutput.ResolutionFileUnreadablePredicate("unreadable_" + localFSArray[0]);
			}
			else
			{
				System.out.println("Incorrect type of output");
			}
		}
		else if (localType.equals("all"))
		{
			System.out.println("---------- CONSOLE AND ALL FILES OUTPUT ----------");
			localOutput.GetFunctionPredicate();
			Scanner outputScanner = new Scanner(System.in);
			System.out.println("Please write file name (<file name>):");
			String localFS = outputScanner.nextLine();
			localOutput.ResolutionFileReadablePredicate("readable_" + localFS);
			localOutput.ResolutionFileUnreadablePredicate("unreadable_" + localFS);
		}
		else
		{
			System.out.println("Incorrect type of output");
		}
	}
	
	public static void main(String[] args)
	{
		Scanner inputScanner = new Scanner(System.in);
		int key = 1;
		String typeLogic;
		String typeInput;
		String typeOutput;
		String typeTreatment;
		ResolutionFunction localFunction;
		
		while (key != 0)
		{
			System.out.println("Greek alphabet to copy: " + ResolutionTerm.GetGreekAlphabet());
			System.out.println("Special symbol to copy: " + "□\n");
			
			ResolutionDisjunct.SetMaxID();
			
			//ResolutionFunction.SetLogRegime(false);
			//ResolutionFunction.SetLogType("none");
			//ResolutionFunction.SetSafeRegime(false);
			//ResolutionFunction.SetSafeType(1000);
			ResolutionFunction.SetSafeNumber(0);
			
			try
			{
				System.out.println("Please write type of logic (<type>):");
				typeLogic = inputScanner.nextLine();
				if (logicList.contains(typeLogic) == false)
				{
					throw new Exception("Wrong type of logic!");
				}
				
				System.out.println("Please write type of input (<type>):");
				typeInput = inputScanner.nextLine();
				if (inputList.contains(typeInput) == false)
				{
					throw new Exception("Wrong type of input!");
				}
				
				System.out.println("Please write type of treatment (<type>):");
				String localTypeTreatment [] = inputScanner.nextLine().split("\\s");
				typeTreatment = localTypeTreatment[0];
				if (localTypeTreatment.length == 1)
				{
					//typeTreatment = inputScanner.nextLine();
					if ((treatmentList.contains(typeTreatment) == false) && (treatmentListPredicate.contains(typeTreatment) == false))
					{
						throw new Exception("Wrong type of treatment for your logic type!");
					}
				}
				else
				{
					if ((treatmentList.contains(typeTreatment) == false) && (treatmentListPredicate.contains(typeTreatment) == false))
					{
						throw new Exception("Wrong type of treatment for your logic type!");
					}
					if (localTypeTreatment[1].equals(flagList.get(0)) && (localTypeTreatment[2].equals("console") || localTypeTreatment[2].equals("file") || localTypeTreatment[2].equals("all") || localTypeTreatment[2].equals("none")))
					{
						if (localTypeTreatment[2].equals("none") == false)
						{
							ResolutionFunction.SetLogRegime(true);
							ResolutionFunction.SetLogType(localTypeTreatment[2]);
						}
						else
						{
							ResolutionFunction.SetLogRegime(false);
							ResolutionFunction.SetLogType(localTypeTreatment[2]);
						}
					}
					else
					{
						throw new Exception("Wrong type of flag/flags or wrong type of flag/flags order!");
					}
					if (localTypeTreatment[3].equals(flagList.get(1)) && (Integer.parseInt(localTypeTreatment[4])) == (int)(Integer.parseInt(localTypeTreatment[4])) /*ResolutionMethod.isNumber(localTypeTreatment[4])*/)
					{
						if (localTypeTreatment[4].equals("-1") == false)
						{
							ResolutionFunction.SetSafeRegime(true);
							ResolutionFunction.SetSafeType(Integer.parseInt(localTypeTreatment[4]));
						}
						else
						{
							ResolutionFunction.SetSafeRegime(false);
							ResolutionFunction.SetSafeType(Integer.parseInt(localTypeTreatment[4]));
						}
					}
					else
					{
						throw new Exception("Wrong type of flag/flags or wrong type of flag/flags order!");
					}
				}
				
				System.out.println("Please write type of output (<type 1> or <type 1> <type 2> or all):");
				typeOutput = inputScanner.nextLine();
				if (outputList.contains(typeOutput) == false)
				{
					throw new Exception("Wrong type of output!");
				}
				
				if (typeLogic.equals("statement"))
				{
					localFunction = InputMain(typeInput);
					try
					{
						localFunction = TreatmentMain(typeTreatment, localFunction);
					}
					catch (Exception excp)
					{
						System.out.println("\n---------- SAFE REGIME ----------");
						System.out.println(excp.getMessage());
						System.out.println("------------------------------\n");
					}
					OutputMain(localFunction, typeOutput);
				}
				else if (typeLogic.equals("predicate"))
				{
					localFunction = InputMainPredicate(typeInput);
					try
					{
						localFunction = TreatmentMainPredicate(typeTreatment, localFunction);
					}
					catch (Exception excp)
					{
						System.out.println("\n---------- SAFE REGIME ----------");
						System.out.println(excp.getMessage());
						System.out.println("------------------------------\n");
					}
					OutputMainPredicate(localFunction, typeOutput);
				}
				
			}
			catch (Exception e)
			{
				System.out.println("\nException caught in ResolutionMethod class:");
				System.out.println(e.getMessage() + " Follow the instructions below to try again.\n");
			}
			finally
			{
				System.out.println("Do you want to input something else? Press 0 to NO, press else to YES:");
				key = inputScanner.nextInt();
				inputScanner.nextLine();
			}
		}
		
		inputScanner.close(); // be careful: it closes I/O system, so if you do it once, you can not use I/O again!
		System.out.println("---------- PROGRAM TERMINATED ----------");
	}
}
