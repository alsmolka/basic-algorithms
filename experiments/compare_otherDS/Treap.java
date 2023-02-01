import java.util.Random;
//uses sections of the code for treap implementation from teaching materials from the course from Florida International University
//modified to update the version to Java 11
//full code avaiable: https://users.cs.fiu.edu/~weiss/dsaajava2/code/Treap.java

/**
 * Implements a treap.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss
 */
public class Treap<AnyType extends Comparable<? super AnyType>>
{
    /**
     * Construct the treap.
     */
    public Treap( )
    {
        nullNode = new TreapNode<AnyType>( null );
        nullNode.left = nullNode.right = nullNode;
        nullNode.priority = Integer.MAX_VALUE;
        root = nullNode;
    }

    /**
     * Insert into the tree. Does nothing if x is already present.
     * @param x the item to insert.
     */
    public void insert( AnyType x )
    {
        root = insert( x, root );
    }

  

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return true if x is found.
     */
    public boolean contains( AnyType x )
    {
        TreapNode<AnyType> current = root;
        nullNode.element = x;

        for( ; ; )
        {
            int compareResult = x.compareTo( current.element );
            
            if( compareResult < 0 )
                current = current.left;
            else if( compareResult > 0 ) 
                current = current.right;
            else
                return current != nullNode;
        }
    }

   
    /**
     * Internal method to insert into a subtree.
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private TreapNode<AnyType> insert( AnyType x, TreapNode<AnyType> t )
    {
        if( t == nullNode )
            return new TreapNode<AnyType>( x, nullNode, nullNode );
            
        int compareResult = x.compareTo( t.element );
        
        if( compareResult < 0 )
        {
            t.left = insert( x, t.left );
            if( t.left.priority < t.priority )
                t = rotateWithLeftChild( t );
        }
        else if( compareResult > 0  )
        {
            t.right = insert( x, t.right );
            if( t.right.priority < t.priority )
                t = rotateWithRightChild( t );
        }
        // Otherwise, it's a duplicate; do nothing

        return t;
    }

    



    /**
     * Rotate binary tree node with left child.
     */
    private TreapNode<AnyType> rotateWithLeftChild( TreapNode<AnyType> k2 )
    {
        TreapNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        return k1;
    }

    /**
     * Rotate binary tree node with right child.
     */
    private TreapNode<AnyType> rotateWithRightChild( TreapNode<AnyType> k1 )
    {
        TreapNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        return k2;
    }

    private static class TreapNode<AnyType>
    {
            // Constructors
        TreapNode( AnyType theElement )
        {
            this( theElement, null, null );
        }

        TreapNode( AnyType theElement, TreapNode<AnyType> lt, TreapNode<AnyType> rt )
        {
            element  = theElement;
            left     = lt;
            right    = rt;
            priority = rd.nextInt( );
        }

            // Friendly data; accessible by other package routines
        AnyType            element;      // The data in the node
        TreapNode<AnyType> left;         // Left child
        TreapNode<AnyType> right;        // Right child
        int                priority;     // Priority

        private static Random rd = new Random( );
    }
    
    private TreapNode<AnyType> root;
    private TreapNode<AnyType> nullNode;
 


}
