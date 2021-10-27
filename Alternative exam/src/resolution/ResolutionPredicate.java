package resolution;

import java.util.ArrayList;

/**
 * Resolution predicate class.
 * @author MatmanBJ
 * @version beta 0.30
 */
public class ResolutionPredicate
{
	// -------------------------------
	// ---------- VARIABLES ----------
	// -------------------------------
	
	private boolean denial; // if TRUE -- no denial, if FALSE -- inverse
	private String name;
	private ArrayList<ResolutionTerm> terms = new ArrayList<ResolutionTerm>();
	
	// -----------------------------
	// ---------- METHODS ----------
	// -----------------------------
	
	// ---------- CONSTRUCTORS ----------
	
	public ResolutionPredicate () // default constructor
	{}
	
	public ResolutionPredicate (boolean localDenial, String localName) // special constructor
	{
		denial = localDenial;
		name = new String(localName);
	}
	
	public ResolutionPredicate (ResolutionPredicate localPredicate) // deep copy constructor
	{
		denial = localPredicate.GetDenial();
		name = new String(localPredicate.GetName());
		for (ResolutionTerm p : localPredicate.GetTerms())
		{
			terms.add(new ResolutionTerm(p));
		}
	}
	
	// ---------- SETTERS ----------
	
	public void SetDenial (boolean localDenial)
	{
		denial = localDenial;
	}
	public void SetName (String localName)
	{
		name = new String(localName);
	}
	public void SetTerms (ResolutionTerm localResolutionTerm)
	{
		terms.add(localResolutionTerm);
	}
	
	// ---------- GETTERS ----------
	
	public boolean GetDenial ()
	{
		return denial;
	}
	public String GetName ()
	{
		return name;
	}
	public ArrayList<ResolutionTerm> GetTerms ()
	{
		return terms;
	}
	
	// ---------- METHODS ----------
	
	public void change (ResolutionTerm [] localTerm)
	{
		for (int i = 0; i < this.GetTerms().size(); i++)
		{
			this.GetTerms().get(i).change(localTerm);
		}
	}
	
	public static boolean unification (ResolutionPredicate localPredicateThis, ResolutionDisjunct localDisjunctThis, ResolutionPredicate localPredicate, ResolutionDisjunct localDisjunct)
	{
		boolean localEquality;
		if (localPredicateThis.GetName().equals(localPredicate.GetName()) && localPredicateThis.GetTerms().size() == localPredicate.GetTerms().size())
		{
			localEquality = true;
			ResolutionTerm [] localT = null;
			
			for (int x = 0; x < localPredicateThis.GetTerms().size(); x++)
			{
				localEquality = localPredicateThis.GetTerms().get(x).pseudoEquals(localPredicate.GetTerms().get(x));
				
				if (localEquality == true)
				{
					localT = localPredicateThis.GetTerms().get(x).unification(localPredicate.GetTerms().get(x), localDisjunctThis, localDisjunct);
					
					if (localT != null)
					{
						if (localT.length == 10)
						{
							localEquality = false;
							x = localPredicateThis.GetTerms().size();
						}
						else if (localT[0].equals(localT[1]) == false)
						{
							for (ResolutionPredicate localPredicateLoop : localDisjunctThis.GetPredicates())
							{
								localPredicateLoop.change(localT);
							}
							for (ResolutionPredicate localPredicateLoop : localDisjunct.GetPredicates())
							{
								localPredicateLoop.change(localT);
							}
						}
					}
				}
				else
				{
					x = localPredicateThis.GetTerms().size();
				}
			}
		}
		else
		{
			localEquality = false;
		}
		return localEquality;
	}
	
	public boolean preContrary (Object localObject)
	{
		ResolutionPredicate localPredicate = (ResolutionPredicate) localObject;
		return ((this.GetName().equals(localPredicate.GetName())) && (this.GetTerms().size() == localPredicate.GetTerms().size()) && (this.GetDenial() != localPredicate.GetDenial()));
	}
	
	public boolean pseudoEquals (Object localObject)
	{
		ResolutionPredicate localPredicate = (ResolutionPredicate) localObject;
		boolean localEquality = true;
		if ((this.GetName().equals(localPredicate.GetName())) && (this.GetTerms().size() == localPredicate.GetTerms().size()))
		{
			for (int z = 0; z < localPredicate.GetTerms().size(); z++)
			{
				boolean localEkwality = this.GetTerms().get(z).pseudoEquals(localPredicate.GetTerms().get(z));
				if (!localEkwality)
				{
					localEquality = localEkwality;
					z = localPredicate.GetTerms().size();
				}
			}
		}
		else
		{
			localEquality = false;
		}
		return localEquality;
	}
	
	public boolean sizeEquals (ResolutionPredicate localPredicate)
	{
		boolean localEquality = true;
		if ((this.GetName().equals(localPredicate.GetName())) && (this.GetTerms().size() == localPredicate.GetTerms().size()))
		{
			localEquality = true;
		}
		else
		{
			localEquality = false;
		}
		return localEquality;
	}
	
	@Override
	public String toString ()
	{
		String localStringDisjunct = "";
		String inverse;
		if (this.GetDenial() == true)
		{
			inverse = "";
		}
		else
		{
			inverse = "!";
		}
		localStringDisjunct = localStringDisjunct + inverse + this.GetName();
		localStringDisjunct = localStringDisjunct + "(" + ResolutionDisjunct.localToStringPredicate("", this.GetTerms()) + ")";
		return "";
	}
	
	@Override
	public boolean equals (Object localObject)
	{
		ResolutionPredicate localPredicate = (ResolutionPredicate) localObject;
		boolean localEquality = true;
		if ((this.GetName().equals(localPredicate.GetName())) && (this.GetTerms().size() == localPredicate.GetTerms().size()))
		{
			for (int z = 0; z < localPredicate.GetTerms().size(); z++)
			{
				boolean localEkwality = this.GetTerms().get(z).equals(localPredicate.GetTerms().get(z));
				if (!localEkwality)
				{
					localEquality = localEkwality;
					z = localPredicate.GetTerms().size();
				}
			}
		}
		else
		{
			localEquality = false;
		}
		return localEquality;
	}
}
