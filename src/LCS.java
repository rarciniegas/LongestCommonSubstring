import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/*
 * Class MyString is a class implemented to get around issues in the class String
 * that let the algorithm work in an efficient way.
 * 
 */

/**
 *
 * @author Roberto Arciniegas
 */
class MyString implements Comparable<MyString>
{
    public MyString( String str )
	{
            original = str;
            startIdx = 0;
            len = str.length( );
	}
    
    /**
     * compare to MyString rhs
     * 
     * @param rhs   MyString to compare to
     * @return  a negative integer, zero, or a positive integer as
     * this object is less than, equal to, or greater than
     * the specified object.
     */
    public int compareTo( MyString rhs )
    {
        int li = startIdx;
        int ri = rhs.startIdx;

        int le = startIdx + len;
        int re = rhs.startIdx + rhs.len;

        String lo = original;
        String ro = rhs.original;
       
        while( li < le && ri < re && lo.charAt( li ) == ro.charAt( ri ) )   // traverses the strings
        {                                                                   //comparing char by char
            li++; ri++;
        }
       
        if ( li == le )
            {
                if ( ri == re )
                    return 0;                                               // equal
                else return -1;                                             // less than
            }
        if( ri == re )
            return 1;                                                       // greater than
       
        return lo.charAt( li ) - ro.charAt( ri );
    }
    
    /**
     * Returns the char at the position given by idx
     * 
     * @param idx   the index of the char value to be returned
     * @return  the specified char value
     */
    public char charAt( int idx )
    {
        return original.charAt( startIdx + idx );
    }
    
    /**
     * 
     * @return  length of MyString
     */
    public int length(  )
    {
        return len;
    }

    /**
     * Finds the substring  starting at si
     * 
     * @param si the beginning index, inclusive.   
     * @return the specified substring.
     */
    public MyString substring( int si )
    {
        MyString tmp = new MyString( original, startIdx + si, length( ) - si ); 
        return tmp;
    }  
    
    /**
     * Finds the substring  starting at si with length slen
     * 
     * @param si the beginning index, inclusive. 
     * @param slen the length of the substring.
     * @return the specified substring.
     */
    public MyString substring( int si, int slen )
    {
        MyString tmp = new MyString( original, startIdx + si, slen ); 
        return tmp;
    } 
    
    private MyString( String source, int start, int newLen )
    {
        original = source;
        startIdx = start;
        len = newLen;
       
    }
    
    /**
     * Converts MyString to String
     * 
     * @return MyString converted to String
     */
    public String toString( )
    {
        return original.substring( startIdx, startIdx + len );
    }

    private String original;
    private int startIdx;
    private int len;
}

/*
 * Class LCS finds the longest common substring in two text files.
 * The program receives the two files from the command line.
 */

/**
 * 
 * @author Roberto Arciniegas
 */
public class LCS
{
    static int splitIndex;  // index where the first text ends and the second starts
    
    /**
     * Finds the number of consecutive char equal in lhs and rhs from th start of each
     * 
     * @param lhs
     * @param rhs
     * @return the number of chars equal from the start of each lhs and rhs
     */
    public static int longestPrefix( MyString lhs, MyString rhs )
    {
        int len = 0;
        
        while( len < lhs.length( ) && len < rhs.length( )
              && lhs.charAt( len ) == rhs.charAt( len ) ) ++len;
        
        return len;
    
    }
    
    /**
     * Opens file fileName read its contents, removes extra spaces and returns a String
     * 
     * @param fileName file to be read into the String
     * @return String with the contents of the file
     * @throws IOException - If an I/O error occurs
     */
    public static String catFileAsString( String fileName ) throws IOException
    {
        try
        {
            BufferedReader in = new BufferedReader( new FileReader( fileName ) );

            StringBuilder sb = new StringBuilder( );

            String before = null;
            String after = null;
            while( ( before = in.readLine( ) ) != null )
            {
                after = before.trim( ).replaceAll( " +", " " );		
                sb.append( after + " " );
            }
        return sb.toString( );
        }
        catch ( IOException exception )
        {
            System.out.println( exception );
        }
        return "";
    }
    
    /**
     * Process the String to find the longest common substring
     * 
     * @param s1 the two files to process as one MyString
     */
    public static void solve( MyString s1 )
    {
        MyString [ ] suffixes = new MyString[s1.length()];  
      
        int [ ] LCP = new int[ s1.length( ) ];
        
        long startTime = System.currentTimeMillis();
        
        for( int i = 0; i < s1.length( ); i++ )                     // creates suffixes
            suffixes[ i ] = s1.substring( i );
        
        Arrays.sort( suffixes );                                    // sorts suffixes

        for( int i = 1; i < s1.length( ); i++ )                     // find number of chars matching
            if (((splitIndex > suffixes[ i ].length() ) && (splitIndex < suffixes[ i - 1].length() )) 
                    || ((splitIndex < suffixes[ i ].length() ) && (splitIndex > suffixes[ i - 1].length() )))
                LCP[ i ] = longestPrefix( suffixes[ i ], suffixes[ i - 1 ] );
            else 
                LCP[ i ] = 0;
        
        int maxLCPIndex = 0;
        
        for( int i = 1; i < LCP.length; i++ )                       // finds the longest
            if( LCP[ i ] > LCP[ maxLCPIndex ] )
                maxLCPIndex = i;
        
        long endTime = System.currentTimeMillis();
        
        if (LCP[ maxLCPIndex ] > 30 )                               // prints results
            System.out.println( "The longest common substring is " + LCP[ maxLCPIndex] + " characters '" 
                    + suffixes[ maxLCPIndex ].substring(0, LCP[ maxLCPIndex ]).substring(0, 30) + "...'");
        else
            System.out.println( "The longest common substring is " + LCP[ maxLCPIndex] + " characters '" 
                    + suffixes[ maxLCPIndex ].substring(0, LCP[ maxLCPIndex ]) + "'");
        long elapsedTime = endTime - startTime;
        
        System.out.println("elapsed time: " + elapsedTime + " milliseconds." );
    }   
            
    
    /**
     * Gets 2 file names from the command line
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        if ( args.length == 2)
        {
            StringBuilder sb = new StringBuilder(catFileAsString(args[0])); //reads file 0 into sb
            splitIndex = sb.length();
            sb.append('#');
            sb.append(catFileAsString(args[1]));                            // appends file 1 to sb
            String s1 = sb.toString();
            splitIndex = sb.length() - splitIndex;                          
            solve( new MyString(s1));                                       // solves the LCS
        }
        else
        {
            System.out.println("Input error. Usage : <java Main file1 file2>");
        }
    }
}

