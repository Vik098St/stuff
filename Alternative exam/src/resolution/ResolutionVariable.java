package resolution;

/**
 * Resolution variable class.
 * @author MatmanBJ
 * @version beta 0.30
 */
public class ResolutionVariable
{
	// -------------------------------
	// ---------- VARIABLES ----------
	// -------------------------------
	
	private boolean denial; // if TRUE -- no denial, if FALSE -- inverse
	private String name;
	
	// -----------------------------
	// ---------- METHODS ----------
	// -----------------------------
	
	// ---------- CONSTRUCTORS ----------
	
	public ResolutionVariable () // default constructor
	{}
	
	public ResolutionVariable (boolean localDenial, String localName) // special constructor
	{
		denial = localDenial;
		name = new String(localName);
	}
	
	public ResolutionVariable (ResolutionVariable localVariable) // deep copy constructor
	{
		denial = localVariable.GetDenial();
		name = new String(localVariable.GetName());
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
	
	// ---------- GETTERS ----------
	
	public boolean GetDenial ()
	{
		return denial;
	}
	public String GetName ()
	{
		return name;
	}
	
	// ---------- METHODS ----------
	
	@Override
	public boolean equals (Object localObject) // also i need to override a "hashCode" method, but i don't need it
	{
        if (localObject == null)
        {
            return false;
        }
        
        if (localObject.getClass() != this.getClass())
        {
            return false;
        }
        
		final ResolutionVariable localResolutionVariable = (ResolutionVariable) localObject;
		
		if (!this.name.equals(localResolutionVariable.name) || this.denial != localResolutionVariable.denial)
		{
			return false;
		}
		
		return true;
	}
}
