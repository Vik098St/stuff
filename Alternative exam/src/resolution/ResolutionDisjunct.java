package resolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;

/**
 * This class represents disjuncts, that containing "classic" statement-logic variables or predicates.
 * Variables and predicates can be sorted (in alphabet order) and refreshed (deleted repeated vars/preds, changed to 1 or empty disjunct).
 * A disjunct containing an information about id, contrary parents, list of vars/preds (in order).
 * @author MatmanBJ
 * @version beta 0.30
 */
public class ResolutionDisjunct implements Comparable<ResolutionDisjunct>
{
	// -------------------------------
	// ---------- VARIABLES ----------
	// -------------------------------
	
	private boolean one; // if TRUE -- disjunct = 1
	private boolean empty; // if TRUE -- disjunct = empty (0)
	private static int maxID = 0;
	private int id;
	private int [] parents = new int [2];
	private String contrary; // the contrary variable of parents
	private ArrayList<ResolutionVariable> variables = new ArrayList<ResolutionVariable>();
	private ArrayList<ResolutionPredicate> predicates = new ArrayList<ResolutionPredicate>();
	
	// -----------------------------
	// ---------- METHODS ----------
	// -----------------------------
	
	// ---------- CONSTRUCTOR ----------
	
	public ResolutionDisjunct () // default constructor
	{
		one = false;
		empty = false;
		id = maxID + 1;
		maxID = maxID + 1;
		parents[0] = 0;
		parents[1] = 0;
		contrary = new String("0");
	}
	
	public ResolutionDisjunct (ArrayList<ResolutionVariable> localResolutionVariable) // special constructor for statements
	{
		one = false;
		empty = false;
		variables.addAll(localResolutionVariable);
		id = maxID + 1;
		maxID = maxID + 1;
		parents[0] = 0;
		parents[1] = 0;
		contrary = new String("0");
	}
	
	public ResolutionDisjunct (ArrayList<ResolutionPredicate> localResolutionPredicate, int localInt) // special constructor for predicates
	{
		one = false;
		empty = false;
		predicates.addAll(localResolutionPredicate);
		id = maxID + 1;
		maxID = maxID + 1;
		parents[0] = 0;
		parents[1] = 0;
		contrary = new String("0");
	}
	
	public ResolutionDisjunct (ResolutionDisjunct localDisjunct) // deep copy constructor
	{
		one = localDisjunct.GetOne();
		empty = localDisjunct.GetEmpty();
		for (ResolutionVariable p : localDisjunct.GetVariables())
		{
			variables.add(new ResolutionVariable(p));
		}
		for (ResolutionPredicate p : localDisjunct.GetPredicates())
		{
			predicates.add(new ResolutionPredicate(p));
		}
		id = localDisjunct.GetID();
		parents[0] = localDisjunct.GetParents()[0];
		parents[1] = localDisjunct.GetParents()[1];
		contrary = new String(localDisjunct.GetContrary());
	}
	
	// ---------- SETTERS ----------
	
	public void SetOne (boolean localOne)
	{
		one = localOne;
	}
	public void SetEmpty (boolean localEmpty)
	{
		empty = localEmpty;
	}
	public static void SetMaxID ()
	{
		maxID = 0;
	}
	public void SetParents (int [] localParents)
	{
		parents[0] = localParents[0];
		parents[1] = localParents[1];
	}
	public void SetContrary (String localContrary)
	{
		contrary = new String(localContrary);
	}
	public void SetVariables (ResolutionVariable localVariables)
	{
		variables.add(localVariables);
	}
	public void SetPredicates (ResolutionPredicate localPredicates)
	{
		predicates.add(localPredicates);
	}
	
	// ---------- GETTERS ----------
	
	public boolean GetOne ()
	{
		return one;
	}
	public boolean GetEmpty ()
	{
		return empty;
	}
	public int GetMaxID ()
	{
		return maxID;
	}
	public int GetID ()
	{
		return id;
	}
	public int [] GetParents ()
	{
		return parents;
	}
	public String GetContrary ()
	{
		return contrary;
	}
	public ArrayList<ResolutionVariable> GetVariables ()
	{
		return variables;
	}
	public ArrayList<ResolutionPredicate> GetPredicates ()
	{
		return predicates;
	}
	
	// ---------- METHODS ----------
	
	/**
	 * This method sort variables in the disjunct in alphabet order.
	 * It can be applied to current disjunct.
	 */
	public void Sort ()
	{
		Collections.sort(variables, new Comparator<ResolutionVariable>()
		{
		    public int compare(ResolutionVariable v1, ResolutionVariable v2)
		    {
		        return v1.GetName().compareTo(v2.GetName());
		    }
		});
	}
	
	/**
	 * This method set current disjunct "in perfect order":
	 * it deletes duplicate variables,
	 * mark the disjunct empty (if there is no variables)
	 * or make it equal "1" (if there are !variable + variable).
	 */
	public void Refresh ()
	{
		for (int i = 0; i < variables.size() - 1; i++)
		{
			for (int j = i + 1; j < variables.size(); j++)
			{
				if ((variables.get(i).GetName().equals(variables.get(j).GetName())) && (variables.get(i).GetDenial() == variables.get(j).GetDenial()))
				{
					variables.remove(j);
					j = j - 1;
				}
				else if ((variables.get(i).GetName().equals(variables.get(j).GetName())) && (variables.get(i).GetDenial() != variables.get(j).GetDenial()))
				{
					variables.removeAll(variables);
					this.SetOne(true);
				}
			}
		}
		if (this.GetOne() == false && variables.size() == 0)
		{
			this.SetEmpty(true);
		}
	}
	
	/**
	 * This method sort predicates in the disjunct in alphabet order.
	 * It can be applied to current disjunct.
	 */
	public void SortPredicate ()
	{
		Collections.sort(predicates, new Comparator<ResolutionPredicate>()
		{
		    public int compare(ResolutionPredicate v1, ResolutionPredicate v2)
		    {
		        return v1.toString().compareTo(v2.toString());
		    }
		});
	}
	
	/**
	 * This method set current disjunct "in perfect order":
	 * it deletes duplicate predicates,
	 * mark the disjunct empty (if there is no predicates)
	 * or make it equal "1" (if there are !predicate(...) + predicate(...)).
	 * Pay attention: predicates with equal name, but different terms are different too!
	 */
	public void RefreshPredicate ()
	{
		for (int i = 0; i < predicates.size() - 1; i++)
		{
			for (int j = i + 1; j < predicates.size(); j++)
			{
				if ((predicates.get(i).equals(predicates.get(j))) && (predicates.get(i).GetDenial() == predicates.get(j).GetDenial()))
				{
					predicates.remove(j);
					j = j - 1;
				}
				else if ((predicates.get(i).equals(predicates.get(j))) && (predicates.get(i).GetDenial() != predicates.get(j).GetDenial()))
				{
					predicates.removeAll(predicates);
					this.SetOne(true);
				}
			}
		}
		if (this.GetOne() == false && predicates.size() == 0)
		{
			this.SetEmpty(true);
		}
	}
	
	/**
	 * ???
	 */
	public void Factorization ()
	{
		for (int i = 0; i < variables.size() - 1; i++)
		{
			for (int j = i + 1; j < variables.size(); j++)
			{
				if (variables.get(i).equals(variables.get(j)))
				{
					variables.remove(j);
					j = j - 1;
				}
				else if ((variables.get(i).GetName().equals(variables.get(j).GetName()))
						&& (variables.get(i).GetDenial() != variables.get(j).GetDenial()))
				{
					variables.removeAll(variables);
					this.SetOne(true);
				}
			}
		}
		if (this.GetOne() == false && variables.size() == 0)
		{
			this.SetEmpty(true);
		}
	}
	
	@Override
	public int compareTo (ResolutionDisjunct localDisjunct)
	{
		return 0;
	}
	
	public String toOutputStringOnly (int localIndex)
	{
		String localOutputStringDisjunct;
		localOutputStringDisjunct = "№" + String.valueOf(localIndex) + " (id " + String.valueOf(id) + ")" + ": (" + String.valueOf(parents[0]) + ", " + String.valueOf(parents[1]) + ", " + String.valueOf(contrary) + ") " + this.toString();
		return localOutputStringDisjunct;
	}
	
	public String toOutputString (int localIndex)
	{
		return toOutputStringOnly(localIndex) + "\n";
	}
	
	public String toOutputStringOnlyPredicate (int localIndex)
	{
		String localOutputStringDisjunct;
		localOutputStringDisjunct = "№" + String.valueOf(localIndex) + " (id " + String.valueOf(id) + ")" + ": (" + String.valueOf(parents[0]) + ", " + String.valueOf(parents[1]) + ", " + String.valueOf(contrary) + ") " + this.toStringPredicate();
		return localOutputStringDisjunct;
	}
	
	public String toOutputStringPredicate (int localIndex)
	{
		return toOutputStringOnlyPredicate(localIndex) + "\n";
	}
	
	public String toReadableString ()
	{
		String localStringDisjunct = "";
		String localEmpty = "";
		String localDenial = "!";
		if (one == false && empty == false)
		{
			int j = 1;
			for (ResolutionVariable localVariable : variables)
			{
				String inverse;
				if (localVariable.GetDenial() == true)
				{
					inverse = localEmpty;
				}
				else
				{
					inverse = localDenial;
				}
				localStringDisjunct = localStringDisjunct + inverse + localVariable.GetName();
				if (j < variables.size())
				{
					localStringDisjunct = localStringDisjunct + " + ";
				}
				j = j + 1;
			}
		}
		else if (one == true)
		{
			localStringDisjunct = localStringDisjunct + "1";
		}
		else if (empty == true)
		{
			localStringDisjunct = localStringDisjunct + "□";
		}

		return localStringDisjunct;
	}
	
	public String toReadableStringPredicate ()
	{
		String localStringDisjunct = "";
		String localLeftBrace = "(";
		String localRightBrace = ")";
		String localEmpty = "";
		String localDenial = "!";
		if (one == false && empty == false)
		{
			int j = 1;
			for (ResolutionPredicate localPredicate : predicates)
			{
				String inverse;
				if (localPredicate.GetDenial() == true)
				{
					inverse = localEmpty;
				}
				else
				{
					inverse = localDenial;
				}
				localStringDisjunct = localStringDisjunct + inverse + localPredicate.GetName();
				localStringDisjunct = localStringDisjunct + localLeftBrace + ResolutionDisjunct.localToStringPredicate("", localPredicate.GetTerms()) + localRightBrace;
				if (j < predicates.size())
				{
					localStringDisjunct = localStringDisjunct + " + ";
				}
				j = j + 1;
			}
		}
		else if (one == true)
		{
			localStringDisjunct = localStringDisjunct + "1";
		}
		else if (empty == true)
		{
			localStringDisjunct = localStringDisjunct + "□";
		}
		
		return localStringDisjunct;
	}
	
	public static String localToStringPredicate (String localLine, ArrayList<ResolutionTerm> localResolutionTerms)
	{
		String localLocalLeftBrace = "(";
		String localLocalRightBrace = ")";
		String localSplitterComma = ",";
		String localSplitterDotComma = ";";
		for (int zhuk = 0; zhuk < localResolutionTerms.size(); zhuk++)
		{
			localLine = localLine + localResolutionTerms.get(zhuk).GetName();
			if (localResolutionTerms.get(zhuk).GetTerm() == ResolutionTerm.GetFunction())
			{
				localLine = localLine + localLocalLeftBrace + ResolutionDisjunct.localToStringPredicate("", localResolutionTerms.get(zhuk).GetTerms()) + localLocalRightBrace;
			}
			
			if (zhuk < localResolutionTerms.size() - 1)
			{
				localLine = localLine + localSplitterComma;
			}
		}
		
		return localLine;
	}
	
	public String toStringPredicate ()
	{
		String localStringDisjunct = "";
		String localLeftBrace = "(";
		String localRightBrace = ")";
		String localEmpty = "";
		String localDenial = "!";
		if (one == false && empty == false)
		{
			int j = 1;
			for (ResolutionPredicate localPredicate : predicates)
			{
				String inverse;
				if (localPredicate.GetDenial() == true)
				{
					inverse = localEmpty;
				}
				else
				{
					inverse = localDenial;
				}
				localStringDisjunct = localStringDisjunct + inverse + localPredicate.GetName();
				localStringDisjunct = localStringDisjunct + localLeftBrace + ResolutionDisjunct.localToStringPredicate("", localPredicate.GetTerms()) + localRightBrace;
				if (j < predicates.size())
				{
					localStringDisjunct = localStringDisjunct + " + ";
				}
				j = j + 1;
			}
		}
		else if (one == true)
		{
			localStringDisjunct = localStringDisjunct + "1 (~ true)";
		}
		else if (empty == true)
		{
			localStringDisjunct = localStringDisjunct + "□ (~ false)";
		}
		
		return localStringDisjunct;
	}
	
	@Override
	public String toString ()
	{
		String localStringDisjunct = "";
		String localEmpty = "";
		String localDenial = "!";
		if (one == false && empty == false)
		{
			int j = 1;
			for (ResolutionVariable localVariable : variables)
			{
				String inverse;
				if (localVariable.GetDenial() == true)
				{
					inverse = localEmpty;
				}
				else
				{
					inverse = localDenial;
				}
				localStringDisjunct = localStringDisjunct + inverse + localVariable.GetName();
				if (j < variables.size())
				{
					localStringDisjunct = localStringDisjunct + " + ";
				}
				j = j + 1;
			}
		}
		else if (one == true)
		{
			localStringDisjunct = localStringDisjunct + "1 (~ true)";
		}
		else if (empty == true)
		{
			localStringDisjunct = localStringDisjunct + "□ (~ false)";
		}

		return localStringDisjunct;
	}
	
}
