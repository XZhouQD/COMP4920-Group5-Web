package scpsolver.constraints;

/**
 * Classes implementing this interface can be exported as part
 * of a CPLEX or GMPL problem description.
 * 
 * An introduction to the CPLEX file format can be found
 * at {@linkplain http://lpsolve.sourceforge.net/5.1/CPLEX-format.htm},
 * the description of the GMPL file format can be found in the
 * GLPK distribution.
 * 
 * @author schober
 *
 */
public interface Convertable {

	
	/**
	 * Creates a CPLEX description of the given instance.
	 * 
	 * @return a CPLEX description of the given instance
	 */
	public StringBuffer convertToCPLEX();

	
	/**
	 * Creates a GMPL description of the given instance.
	 * 
	 * @return a GMPL description of the given instance
	 */

	public StringBuffer convertToGMPL();
}
