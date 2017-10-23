/**
 * Project 5
 * 
 * Program asks user for a birth year. Reads
 * in a .txt file from the SSA of
 * the most popular names for the particular year
 * the user has entered. It then asks the user
 * if they would like to search for a name, enter
 * a rank (program will print out name at that rank), 
 * or print out a list of the top (1-20, user's choice) names for that
 * year.
 *
 * @author Samantha Montgomery
 * @version 5
 * Lab Section: Friday at 7:30am
 */

import java.util.*;
import java.io.*;

public class Proj5 {
	
	public static String[] fnames; //will hold the female names in order of popularity
	public static String[] mnames; //will hold the male names in order of popularity
	public static Scanner s;
	
	public static void main(String[] args) throws IOException
	{
		s = new Scanner(System.in); //initializing our scanner
		
		getYear(); //
		
		char choice = 'a';
		do {
			System.out.print("Enter (n)ame, (r)ank, (t)op, or (q)uit: ");
			choice = (s.nextLine().toLowerCase()).charAt(0);
			
			switch (choice)
			{
			case 'n':
				nameOption();
				break;
			case 'r':
				rankOption();
				break;
			case 't':
				topOption();
				break;
			case 'q':
				break;
			default:
				System.out.println();
				System.out.println("Error: that is not a valid choice.");
				break;
			}
			System.out.println();
		} while (choice != 'q');
		
	}
	
	public static void getYear() throws IOException
	{
		/*
		 * This method will ask the user for a year within
		 * our range. It then checks the yearArray to see
		 * if it is in range, and if it is, it then
		 * puts that year in a larger String ("names\\yobXXXX.txt")
		 * XXXX is where the year will go. Finally,
		 * it calls the readNamesData() method, which sets up
		 * the mnames and fnames arrays with lines from our selected
		 * file.
		 */
		
		int[] yearArray = new int[137];
		int yearCur = 1880;
		
		for (int i = 0; i < 137; i++) //setting up an int array with all possible years
		{
			yearArray[i] = yearCur;
			yearCur++;
		}
		
		boolean isYear = false; //whether or not the user's year is in our array
		int year = 0; //variable for what user enters
		String yearStr = ""; //string for what user enters
		while (!isYear)
		{
			System.out.print("Enter a birth year (1880-2016): ");
			year = Integer.parseInt(s.nextLine());
			yearStr = Integer.toString(year);
			
			System.out.println();
			
			for (int i = 0; i < 137; i++)
			{
				if (year == yearArray[i]) 
				{
					isYear = true;
					break;
				}  
			}
			if (!isYear) System.out.println("Sorry, that is not a year we have."); //after checking entire array of years and we still haven't found it
		}
		
		
		StringBuilder sb = new StringBuilder("names\\yob");
		sb.append(yearStr);
		sb.append(".txt");
		String manipulatedYear = sb.toString();
		
		readNamesData(manipulatedYear); //passing year file path to method that creates the female and male arrays of names
	}
	
	public static int numNames(String filename, char sex) throws IOException
	{
		/* this method numNames opens the input file filename and returns a count
		 *  of how many names have the given sex (M or F).
		 *  
		 *  @param filename: what year of birth the user entered
		 *  @param sex: the gender that we're looking at
		 *  
		 *  @return the total number of names the sex has for that year
		*/
		
		Scanner inFile = new Scanner(new File(filename)); //accessing our year of birth file
		
		int totalNames = 0; //how many names are in the file?
		
		while(inFile.hasNext()) //while there is still lines left  
		{
			String line = inFile.nextLine();
			
			String[] pieces = line.split(","); //splits the line into a String array by commas
			if (pieces[1].charAt(0) == sex) totalNames++; //if 'M' or 'F' add one to count of names
		}
		
		inFile.close();
		return totalNames; //how many names the gender has
	}
	
	public static void readNamesData(String filename) throws IOException
	{
		/* This method gets a count of how many male and female names are in the input file filename, 
		 * and allocates that amount of space for mnames and fnames. Then, it opens the 
		 * input file filename and reads each name in order into mnames and fnames 
		 * (separating by sex).
		 * 
		 * @param filename: what year the user entered
		 */
		
		/*these method calls will return how many female and male names we have
		 in the .txt file for the year given.*/
		int femaleTotal = numNames(filename, 'F');
		int maleTotal = numNames(filename, 'M');
		int overallTotal = femaleTotal + maleTotal; //how many overall
		
		//allocating space for arrays of female names and male names
		fnames = new String[femaleTotal]; 
		mnames = new String[maleTotal];
		
		Scanner inFile = new Scanner(new File(filename));
		
		for (int i = 0; i < overallTotal; i++)
		{

			if (i < femaleTotal) 
			{
				fnames[i] = inFile.nextLine();
			}
			else if (i > femaleTotal && i < overallTotal) 
			{
				for (int j = 0; j < maleTotal; j++)
				{
					mnames[j] = inFile.nextLine();
				}
				break;
			}
		}	
		
		inFile.close();
	}
	
	public static int getRank(String name, char sex)
	{
		/* This method returns the popularity rank of name, 
		 * where 1 is the rank of the  
		 * most popular name (remember that mnames and fnames are ordered by popularity). 
		 * If sex is 'M', look in mnames. 
		 * If it is 'F', look in fnames.
		 * 
		 * @param name: String that the user wants to search for
		 * @param sex: the gender that we're checking
		 * 
		 * @return: the int position of the name in the sex array
		 */
		
		int rank = 0;
		
		if (sex == 'M') //checking male names array for the name
		{
			for (int i = 0; i < mnames.length; i++)
			{
				String[] onlyMaleName = mnames[i].split(",");
				
				if (onlyMaleName[0].equals(name))
				{
					rank = i+1; //set rank to line of our array
					return rank;
				}
			}
		}
		
		if (sex == 'F') //checking female names array for the name
		{
			for (int i = 0; i < fnames.length; i++)
			{
				String[] onlyFemaleName = fnames[i].split(",");
				
				if (onlyFemaleName[0].equals(name))
				{
					rank = i+1; //set rank to line of our array
					return rank;
				}
			}
		}
		
		return rank; // if rank is still zero, we haven't found it in our array
		
	}
	
	public static void nameOption() 
	{ 
		/*if the user wants to search for a name, this is the method
		 * (calls getRank to find the ranking of the name for m and f) 
		 */
		
		System.out.print("Enter name: ");
		String name = s.nextLine();
		
		//this fixes the names if they have a mixed case
		StringBuilder sb = new StringBuilder(name);
		for (int i = 0; i < name.length(); i++)
		{
			if (i == 0) sb.setCharAt(i, Character.toUpperCase(name.charAt(i)));
			else sb.setCharAt(i, Character.toLowerCase(name.charAt(i)));
		}
		name = sb.toString(); //name now has first letter capitalized, rest are lowercase
		
		System.out.println();
		
		int femaleRank = getRank(name, 'F'); //find name in array, return rank value for females
		int maleRank = getRank(name, 'M'); //find name in array, return rank value for males
		
		if (femaleRank != 0)
		{
			System.out.println(name + " is ranked " + femaleRank + " for females.");
		} 
		else if (femaleRank == 0)
		{
			System.out.println(name + " is not ranked for females.");
		}
		
		if (maleRank != 0)
		{
			System.out.println(name + " is ranked " + maleRank + " for males.");
		}
		else if (maleRank == 0)
		{
			System.out.println(name + " is not ranked for males.");
		}
	}
	
	public static void rankOption() 
	{
		/*user types in a number, this method will print out the name at that rank
		 * for both m and f.
		 */
		
		System.out.print("Enter rank: ");
		int rank = Integer.parseInt(s.nextLine());
		
		System.out.println();
		
		if (rank <= 0 || rank > fnames.length)
		{
			System.out.println("Error: that rank is invalid for females.");
		}
		else
		{
			String[] onlyFemaleName = fnames[rank - 1].split(",");
			System.out.println(onlyFemaleName[0] + " is the #" + rank + " female name.");
		}
		
		if (rank <= 0 || rank > mnames.length)
		{
			System.out.println("Error: that rank is invalid for males.");
		}
		else
		{
			String[] onlyMaleName = mnames[rank - 1].split(",");
			System.out.println(onlyMaleName[0] + " is the #" + rank + " male name.");
		}
	}
	
	public static void topOption()
	{
		/* User will select how many names they want to print out. 
		 * This method will print out the top names for their number choice.
		 */
		
		System.out.print("Enter size of top list (1 - 20): ");
		int listSize = Integer.parseInt(s.nextLine());
		
		System.out.println();
		
		if (listSize > 0 && listSize <= 20) //user enters proper listSize
		{
			//this prints out the female names in a list
			System.out.println("Top " + listSize + " female names: ");
			for (int i = 1; i <= listSize; i++)
			{
				String[] onlyFemaleName = fnames[i - 1].split(","); //splits line in file to just the name
				System.out.println("	(" + i + ") " + onlyFemaleName[0]);
			}
			System.out.println();
			
			//this prints out the male names in a list 
			System.out.println("Top " + listSize + " male names: ");
			for (int i = 1; i <= listSize; i++)
			{
				String[] onlyMaleName = mnames[i - 1].split(","); //splits line in file to just the name
				System.out.println("	(" + i + ") " + onlyMaleName[0]);
			}
			
		} 
		else //user enters list size not in our range of (1-20)
		{
			System.out.println("Error: invalid list size.");
		}

	}

}
